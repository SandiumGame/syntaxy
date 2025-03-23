package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;

public class UserQuerySnippet extends Snippet {
    @Override
    public String getText(ExecutionContext executionContext) {
        return executionContext.getUserQuery();
    }
}
