package org.sandium.syntaxy.backend;

import org.sandium.syntaxy.backend.llm.LlmOutputHandler;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

public class Backend {

    Bedrock bedrock;

    public Backend() {
        bedrock = new Bedrock();
    }

    private void converse(String hello) {
        bedrock.converse("In one sentence describe a Hello World program?", new LlmOutputHandler() {
            @Override
            public void onContent(String text) {
                System.out.print(text);
            }

            @Override
            public void onContentFinished() {
                System.out.println("\n\n--------------\n");
            }

            @Override
            public void onMetadata(int inputTokens, int outputTokens) {
                System.out.println("tokens: " + inputTokens + " " + outputTokens);
            }
        });
    }

    public static void main(String[] args) {
        Backend backend = new Backend();
        backend.converse("Hello");
    }

}
