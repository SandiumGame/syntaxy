package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;

public class UserQuerySnippet extends Snippet {
    @Override
    public String getText(ExecutionContext executionContext, Config config) {
        return executionContext.getUserQuery();
    }
}
