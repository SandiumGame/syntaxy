package org.sandium.syntaxy.backend.llm.conversation;

public interface InteractionListener {

    default void contentAdded(Interaction interaction) {}

}
