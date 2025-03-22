package org.sandium.syntaxy.backend.config.agents;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.prompt.Prompt;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    private String id;
    private String model;
    private String title;
    private String description;
    private final List<Prompt> prompts;

    public Agent() {
        prompts = new ArrayList<>();
    }

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

    public List<Prompt> getPrompts() {
        return prompts;
    }

    public void execute(Conversation conversation, ExecutionContext executionContext, Bedrock bedrock) {

        // TODO process output callback

        System.out.println("Debug 1");
    }

    public void generatePrompt(Conversation conversation, ExecutionContext executionContext) {
        // TODO Add all prompts to conversation (only if conversation empty?)

    }

}
