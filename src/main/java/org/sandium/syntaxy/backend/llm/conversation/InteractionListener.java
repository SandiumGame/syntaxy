package org.sandium.syntaxy.backend.llm.conversation;

public interface InteractionListener {

    default void messageAdded(Message message) {}

    default void finished(Interaction interaction) {}

    default void usageUpdated(Interaction interaction, long amountSpentNanos) {}

}
