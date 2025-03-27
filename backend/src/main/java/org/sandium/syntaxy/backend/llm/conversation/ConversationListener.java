package org.sandium.syntaxy.backend.llm.conversation;

public interface ConversationListener {

    default void interactionAdded(Interaction interaction) {}

}
