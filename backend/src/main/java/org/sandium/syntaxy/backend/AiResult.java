package org.sandium.syntaxy.backend;

import java.util.Collection;

public class AiResult {

    private Collection<AiResultListener> listeners;
    private StringBuilder content;
    private boolean contentFinished;
    private long amountSpentNanos;

    public AiResult(Collection<AiResultListener> listeners) {
        this.listeners = listeners;
        content = new StringBuilder();
    }

    public String getContent() {
        return content.toString();
    }

    public boolean isContentFinished() {
        return contentFinished;
    }

    public long getAmountSpentNanos() {
        return amountSpentNanos;
    }

    void addText(String text) {
        content.append(text);
    }

    void setContentFinished() {
        contentFinished = true;
    }

    void addUsage(long amountSpentNanos) {
        this.amountSpentNanos += amountSpentNanos;

        for (AiResultListener listener : listeners) {
            listener.usageUpdated(this, amountSpentNanos);
        }
    }
}
