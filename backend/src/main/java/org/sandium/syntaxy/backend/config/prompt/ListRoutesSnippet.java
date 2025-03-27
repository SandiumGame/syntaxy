package org.sandium.syntaxy.backend.config.prompt;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;

public class ListRoutesSnippet extends Snippet {

    private final String group;

    public ListRoutesSnippet(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }

    @Override
    public String getText(Interaction interaction, ExecutionContext executionContext, Config config) {
        StringBuilder builder = new StringBuilder();

        config.getAgentsByGroup(group).forEach(agent -> {
            builder.append("\u00A0\u00A0\u00A0")
                    .append(agent.getId())
                    .append(" - ")
                    .append(agent.getDescription())
                    .append("\n");
        });

        return builder.toString();
    }
}
