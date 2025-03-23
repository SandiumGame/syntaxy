package org.sandium.syntaxy.backend.config.agents;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.prompt.Prompt;
import org.sandium.syntaxy.backend.config.prompt.Snippet;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.llm.providers.BaseProvider;

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

    public void execute(Conversation conversation, ExecutionContext executionContext, BaseProvider provider) {

        // TODO process output callback
        generatePrompts(conversation, executionContext);
//        conversation.setModel();

        // provider.execute(conversation);
    }

    public void generatePrompts(Conversation conversation, ExecutionContext executionContext) {
        boolean continuedConversation = conversation.containsAgent(id) && (!conversation.getMessages().isEmpty());
        for (Prompt prompt : prompts) {
            if ((!continuedConversation && prompt.isOnInitialConversation())
                    || (continuedConversation && prompt.isOnContinuedConversation())) {

                StringBuilder builder = new StringBuilder();
                for (Snippet snippet : prompt.getSnippets()) {
                    builder.append(snippet.getText(executionContext));
                }

                Message message = conversation.addMessage();
                message.setMessageType(prompt.getType());
                message.setContent(builder.toString());
            }
        }

    }

}
