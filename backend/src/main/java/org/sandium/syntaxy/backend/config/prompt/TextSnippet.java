package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;

public class TextSnippet extends Snippet {

    private final String text;

    public TextSnippet(String text) {
        // TODO Remove whitespace from start of lines
        // TODO Fix non breaking spaces
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getText(ExecutionContext executionContext, Config config) {
        return text;
    }
}

