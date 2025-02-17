package org.sandium.syntaxy.backend.llm.conversation;

public interface ConversationListener {

    default void messageAdded(Message message) {}

    default void finished(Conversation conversation) {}

    default void usageUpdated(Conversation result, long amountSpentNanos) {}

}
