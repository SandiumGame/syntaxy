package org.sandium.syntaxy.backend2.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.config.prompt.Snippet;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;

import java.util.Locale;

public class FileTextSnippet extends Snippet {
    @Override
    public String getText(Interaction interaction, ExecutionContext executionContext, Config config) {
        return executionContext.getUserLocale().getDisplayLanguage(Locale.ENGLISH);
    }
}