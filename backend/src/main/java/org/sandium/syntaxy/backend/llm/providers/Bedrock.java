package org.sandium.syntaxy.backend.llm.providers;

import org.sandium.syntaxy.backend.llm.Model;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.config.prompt.PromptType;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrock.BedrockClient;
import software.amazon.awssdk.services.bedrock.model.FoundationModelSummary;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsRequest;
import software.amazon.awssdk.services.bedrock.model.ListFoundationModelsResponse;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamResponseHandler;
import software.amazon.awssdk.services.bedrockruntime.model.SystemContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.TokenUsage;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Bedrock extends Provider {

    private final BedrockRuntimeAsyncClient client;

    public Bedrock() {
        super("bedrock");

        client = BedrockRuntimeAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

        BedrockClient bedrockClient = BedrockClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

        ListFoundationModelsResponse models = bedrockClient.listFoundationModels(ListFoundationModelsRequest.builder().build());
        for (FoundationModelSummary summary : models.modelSummaries()) {
            summary.responseStreamingSupported();
//            System.out.println(summary.modelName() + "\n " + summary.modelId() + "\n " + summary.modelArn());
        }
    }

    protected void verifyModel(Model model) {
        // TODO Need custom exception??
        System.out.println(model.getPattern());
        // TODO Set id in model
    }

    public void execute(Interaction interaction) {
        ArrayList<Message> messages = interaction.getMessages();
        ArrayList<SystemContentBlock> systemMessages = new ArrayList<>();
        ArrayList<software.amazon.awssdk.services.bedrockruntime.model.Message> bedrockMessages = new ArrayList<>();
        for (Message message : messages) {
            if (message.getMessageType() == PromptType.SYSTEM) {
                systemMessages.add(SystemContentBlock.builder()
                        .text(message.getContent())
                        .build());
            } else {
                bedrockMessages.add(software.amazon.awssdk.services.bedrockruntime.model.Message.builder()
                        .content(ContentBlock.fromText(message.getContent()))
                        .role(message.getMessageType() == PromptType.USER ? ConversationRole.USER : ConversationRole.ASSISTANT)
                        .build());
            }
        }

        Message response = interaction.addMessage();
        response.setMessageType(PromptType.ASSISTANT);

        Model model = interaction.getAgent().getModel();

        var responseStreamHandler = ConverseStreamResponseHandler.builder()
                .subscriber(ConverseStreamResponseHandler.Visitor.builder()
                        .onContentBlockDelta(chunk -> response.addContent(chunk.delta().text()))
                        .onContentBlockStop(stopEvent -> interaction.setFinished())
                        .onMetadata(metadataEvent -> {
                            TokenUsage usage = metadataEvent.usage();
                            if (usage != null) {
                                interaction.addUsage(usage.inputTokens() != null ? usage.inputTokens() : 0,
                                        usage.outputTokens() != null ? usage.outputTokens() : 0);
                            }
                        })
                        .build()
                ).onError(err ->
                        // TODO Handle errors
                        System.err.printf("Can't invoke '%s': %s", model.getName(), err.getMessage())
                ).build();

        try {
            client.converseStream(request -> request.modelId(model.getName())
                    .system(systemMessages.toArray(SystemContentBlock[]::new))
                    .messages(bedrockMessages.toArray(software.amazon.awssdk.services.bedrockruntime.model.Message[]::new))
//                    .inferenceConfig(config -> config
//                            .maxTokens(512))
                    , responseStreamHandler).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
