package org.sandium.syntaxy.backend.config.prompt;

import java.util.ArrayList;
import java.util.List;

public class Prompt {

    private final PromptType type;
    private final List<Snippet> snippets;

    public Prompt(PromptType type) {
        this.type = type;
        snippets = new ArrayList<>();
    }

    public PromptType getType() {
        return type;
    }

    public List<Snippet> getSnippets() {
        return snippets;
    }
}
