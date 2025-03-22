package org.sandium.syntaxy.backend.config.prompt;

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
}

