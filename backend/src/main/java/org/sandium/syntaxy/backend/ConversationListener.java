package org.sandium.syntaxy.backend;

public interface ConversationListener {

    default void contentUpdated(Conversation result) {}

    default void requestFinished(Conversation result) {}

    default void usageUpdated(Conversation result, long amountSpentNanos) {}

}
