package org.sandium.syntaxy.backend.config.agents;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

public class Agent {

    private String id;
    private String model;
    private String title;
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void execute(Conversation conversation, ExecutionContext executionContext, Bedrock bedrock) {

    }
}
