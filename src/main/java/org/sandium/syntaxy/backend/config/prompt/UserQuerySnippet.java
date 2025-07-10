package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;

public class UserQuerySnippet extends Snippet {
    @Override
    public String getText(Interaction interaction, ExecutionContext executionContext, Config config) {
        return interaction.getUserQuery();
    }
}
