package org.sandium.syntaxy.backend.llm.providers;

import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.ConverseStreamResponseHandler;
import software.amazon.awssdk.services.bedrockruntime.model.Message;
import software.amazon.awssdk.services.bedrockruntime.model.TokenUsage;

import java.util.concurrent.ExecutionException;

public class Bedrock {

    private BedrockRuntimeAsyncClient client;

    public Bedrock() {
        client = BedrockRuntimeAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

//        BedrockClient bedrockClient = BedrockClient.builder()
//                .credentialsProvider(DefaultCredentialsProvider.create())
//                .region(Region.US_EAST_1)
//                .build();
//        bedrockClient.listFoundationModels()
    }

    public void execute(Interaction interaction) {
        var message = Message.builder()
                .content(ContentBlock.fromText(interaction.getQuery()))
                .role(ConversationRole.USER)
                .build();

        var responseStreamHandler = ConverseStreamResponseHandler.builder()
                .subscriber(ConverseStreamResponseHandler.Visitor.builder()
                        .onContentBlockDelta(chunk -> interaction.addContent(chunk.delta().text()))
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
                        System.err.printf("Can't invoke '%s': %s", interaction.getModel().getName(), err.getMessage())
                ).build();

        try {
            client.converseStream(request -> request.modelId(interaction.getModel().getName())
                    .messages(message)
                    .inferenceConfig(config -> config
                            // TODO Remove maxTokens
                            .maxTokens(512)
                    ), responseStreamHandler).get();
        } catch (ExecutionException | InterruptedException e) {
            // TODO Handle errors
            System.err.printf("Can't invoke '%s': %s", interaction.getModel().getName(), e.getCause().getMessage());
        }
    }

}
