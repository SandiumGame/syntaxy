package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;

public abstract class Snippet {

    public abstract String getText(Interaction interaction, ExecutionContext executionContext, Config config);
}
