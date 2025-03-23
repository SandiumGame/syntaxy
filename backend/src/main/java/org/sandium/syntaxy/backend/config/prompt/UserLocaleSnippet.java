package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;

import java.util.Locale;

public class UserLocaleSnippet extends Snippet {
    @Override
    public String getText(ExecutionContext executionContext) {
        return executionContext.getUserLocale().getDisplayLanguage(Locale.ENGLISH);
    }
}
