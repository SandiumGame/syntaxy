package org.sandium.syntaxy.backend;

public interface AiResultListener {

    default void contentUpdated(AiResult result) {}

    default void requestFinished(AiResult result) {}

    default void usageUpdated(AiResult result, long amountSpentNanos) {}

}
