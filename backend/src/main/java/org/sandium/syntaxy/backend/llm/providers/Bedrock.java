package org.sandium.syntaxy.backend.llm.providers;

import org.sandium.syntaxy.backend.llm.LlmOutputHandler;
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

    private static final String MODEL = "anthropic.claude-3-haiku-20240307-v1:0";

    private int totalInputTokens;
    private int totalOutputTokens;
    private BedrockRuntimeAsyncClient client;

    public Bedrock() {
        client = BedrockRuntimeAsyncClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();
    }

    public void converse(String inputText, LlmOutputHandler handler) {
        var message = Message.builder()
                .content(ContentBlock.fromText(inputText))
                .role(ConversationRole.USER)
                .build();

        // TODO Construct in constructor
        var responseStreamHandler = ConverseStreamResponseHandler.builder()
                .subscriber(ConverseStreamResponseHandler.Visitor.builder()
                        .onContentBlockDelta(chunk -> handler.onContent(chunk.delta().text()))
                        .onContentBlockStop(stopEvent -> handler.onContentFinished())
                        .onMetadata(metadataEvent -> {
                            TokenUsage usage = metadataEvent.usage();
                            if (usage != null) {
                                handler.onMetadata(usage.inputTokens() != null ? usage.inputTokens() : 0, usage.outputTokens() != null ? usage.outputTokens() : 0);
                            }
                        })
                        .build()
                ).onError(err ->
                        System.err.printf("Can't invoke '%s': %s", MODEL, err.getMessage())
                ).build();

        try {
            client.converseStream(request -> request.modelId(MODEL)
                    .messages(message)
                    .inferenceConfig(config -> config
                            .maxTokens(512)
                            .temperature(0.5F)
                            .topP(0.9F)
                    ), responseStreamHandler).get();
        } catch (ExecutionException | InterruptedException e) {
            System.err.printf("Can't invoke '%s': %s", MODEL, e.getCause().getMessage());
        }
    }

}
