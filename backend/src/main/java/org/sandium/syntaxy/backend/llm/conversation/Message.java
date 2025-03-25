package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.config.prompt.PromptType;

import java.util.ArrayList;
import java.util.Collection;

public class Message {

    private final Conversation conversation;
    private Collection<MessageListener> listeners;
    private PromptType promptType;
    private boolean temporary;
    private final StringBuilder content;

    public Message(Conversation conversation) {
        this.conversation = conversation;
        listeners = new ArrayList<>();
        content = new StringBuilder();
    }

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public PromptType getMessageType() {
        return promptType;
    }

    public void setMessageType(PromptType promptType) {
        this.promptType = promptType;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String content) {
        this.content.replace(0, this.content.length(), content);
        for (MessageListener listener : listeners) {
            listener.contentAdded(this);
        }
    }

    public void addContent(String content) {
        this.content.append(content);
        for (MessageListener listener : listeners) {
            listener.contentAdded(this);
        }
    }
}
