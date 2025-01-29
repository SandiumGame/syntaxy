package org.sandium.syntaxy.backend.llm.conversation;

public interface ConversationListener {

    default void contentAdded(Interaction interaction) {}

    default void interactionFinished(Interaction interaction) {}

    default void usageUpdated(Conversation result, long amountSpentNanos) {}

}
