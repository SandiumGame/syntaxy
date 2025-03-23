package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;

public abstract class Snippet {

    public abstract String getText(ExecutionContext executionContext);
}
