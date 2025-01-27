package org.sandium.syntaxy.backend;

import org.sandium.syntaxy.backend.llm.LlmResultsHandler;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

import java.util.Collection;

public class AiAssistant {

    // TODO Needs to come from file
    private final long INPUT_TOKEN_COST = (long)(0.003 / 1000 * 1000000000L);
    private final long OUTPUT_TOKEN_COST = (long)(0.015 / 1000 * 1000000000L);

    /*
     * Categorize question
     *  - determines what information / execution scripts need to be run
     * 
     * TODO ability to prompt user to continue
     * TODO ability to mark that a conversation should end
     * TODO ability for user to pick the root script that should be run
     *
     * TODO Model file with aliases and pricing
     * 
     */

    private Bedrock bedrock;

    public AiAssistant() {
        bedrock = new Bedrock();
    }

    public void execute(String text, Collection<AiResultListener> listeners) {
        new Thread(() -> {
            AiResult result = new AiResult(listeners);

            bedrock.converse(text, new LlmResultsHandler() {
                @Override
                public void onContent(String text) {
                    result.addText(text);
                }

                @Override
                public void onContentFinished() {
                    result.setContentFinished();
                }

                @Override
                public void onMetadata(int inputTokens, int outputTokens) {
                    result.addUsage(inputTokens * INPUT_TOKEN_COST + outputTokens * OUTPUT_TOKEN_COST);
                    // TODO Remove println
                    System.out.println("tokens: " + inputTokens + " " + outputTokens);
                }
            });
        }).start();
    }

}
