package org.sandium.syntaxy.backend;

import org.sandium.syntaxy.backend.llm.LlmResultsHandler;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

public class AiAssistant {

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

    public void execute(String text, AiResultListener listener) {
        new Thread(() -> {
            AiResult result = new AiResult(listener);

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
                    result.addUsage(inputTokens, outputTokens);
                    System.out.println("tokens: " + inputTokens + " " + outputTokens);
                }
            });
        }).start();
    }

}
