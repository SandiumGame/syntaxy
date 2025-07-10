package org.sandium.syntaxy.backend.llm.conversation;

public interface MessageListener {

    default void contentAdded(Message message) {}

}
