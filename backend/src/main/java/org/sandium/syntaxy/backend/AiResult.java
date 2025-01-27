package org.sandium.syntaxy.backend;

public class AiResult {

    private AiResultListener listener;
    private StringBuilder content;
    private boolean contentFinished;
    private int inputTokens;
    private int outputTokens;

    public AiResult(AiResultListener listener) {
        this.listener = listener;
        content = new StringBuilder();
    }

    public String getContent() {
        return content.toString();
    }

    public boolean isContentFinished() {
        return contentFinished;
    }

    public int getInputTokens() {
        return inputTokens;
    }

    public int getOutputTokens() {
        return outputTokens;
    }

    void addText(String text) {
        content.append(text);
    }

    void setContentFinished() {
        contentFinished = true;
    }

    void addUsage(int inputTokens, int outputTokens) {
        inputTokens += inputTokens;
        outputTokens += outputTokens;
    }
}
