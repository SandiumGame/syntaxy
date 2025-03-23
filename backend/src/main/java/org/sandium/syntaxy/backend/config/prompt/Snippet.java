package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;

public abstract class Snippet {

    public abstract String getText(ExecutionContext executionContext, Config config);
}
