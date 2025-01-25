package org.sandium.syntaxy.backend.llm;

public interface LlmOutputHandler {

    void onContent(String text);

    void onContentFinished();

    void onMetadata(int inputTokens, int outputTokens);
}
