package org.sandium.syntaxy.backend.config.prompt;

import java.util.ArrayList;
import java.util.List;

public class Prompt {

    private PromptType type;
    private boolean onInitialConversation;
    private boolean onContinuedConversation;
    private boolean keepInConversation;
    private final List<Snippet> snippets;

    public Prompt() {
        snippets = new ArrayList<>();
    }

    public PromptType getType() {
        return type;
    }

    public void setType(PromptType type) {
        this.type = type;
    }

    public boolean isOnInitialConversation() {
        return onInitialConversation;
    }

    public void setOnInitialConversation(boolean onInitialConversation) {
        this.onInitialConversation = onInitialConversation;
    }

    public boolean isOnContinuedConversation() {
        return onContinuedConversation;
    }

    public void setOnContinuedConversation(boolean onContinuedConversation) {
        this.onContinuedConversation = onContinuedConversation;
    }

    public boolean isKeepInConversation() {
        return keepInConversation;
    }

    public void setKeepInConversation(boolean keepInConversation) {
        this.keepInConversation = keepInConversation;
    }

    public List<Snippet> getSnippets() {
        return snippets;
    }
}
