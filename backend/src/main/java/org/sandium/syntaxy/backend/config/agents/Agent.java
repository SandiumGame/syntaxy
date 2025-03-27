package org.sandium.syntaxy.backend.config.agents;

import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.config.prompt.Prompt;
import org.sandium.syntaxy.backend.config.prompt.Snippet;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.llm.providers.BaseProvider;

import java.util.ArrayList;
import java.util.List;

public class Agent {

    private final Config config;
    private String id;
    private String group;
    private String model;
    private String title;
    private String description;
    private final List<Prompt> prompts;
    private boolean showOutput;
    private boolean endConversation;
    private boolean routeToAgent;

    public Agent(Config config) {
        this.config = config;
        prompts = new ArrayList<>();
    }

    public Config getConfig() {
        return config;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public boolean isShowOutput() {
        return showOutput;
    }

    public void setShowOutput(boolean showOutput) {
        this.showOutput = showOutput;
    }

    public boolean isEndConversation() {
        return endConversation;
    }

    public void setEndConversation(boolean endConversation) {
        this.endConversation = endConversation;
    }

    public boolean isRouteToAgent() {
        return routeToAgent;
    }

    public void setRouteToAgent(boolean routeToAgent) {
        this.routeToAgent = routeToAgent;
    }

    public void execute(Conversation conversation, ExecutionContext executionContext, BaseProvider provider) {
        Interaction interaction = conversation.getCurrentInteraction();

        generatePrompts(interaction, executionContext);
        provider.execute(interaction);

        if (routeToAgent) {
            ArrayList<Message> messages = interaction.getMessages();
            Message message = messages.get(messages.size() - 1);

            Agent nextAgent = config.getAgent(message.getContent());
            if (nextAgent == null) {
                // TODO Handle error
                return;
            }

            conversation.copyCurrentInteraction();
            // TODO Remove temp prompts
            // TODO Separate window to debug conversations / view logs
            // TODO Add log output to conversation to say it is routing to next agent

            nextAgent.execute(conversation, executionContext, provider);
        }
    }

    private void generatePrompts(Interaction interaction, ExecutionContext executionContext) {
        boolean continuedConversation = interaction.containsAgent(id) && (!interaction.getMessages().isEmpty());
        for (Prompt prompt : prompts) {
            if ((!continuedConversation && prompt.isOnInitialConversation())
                    || (continuedConversation && prompt.isOnContinuedConversation())) {

                StringBuilder builder = new StringBuilder();
                for (Snippet snippet : prompt.getSnippets()) {
                    builder.append(snippet.getText(interaction, executionContext, config));
                }

                Message message = interaction.addMessage();
                message.setKeepInConversation(prompt.isKeepInConversation());
                message.setMessageType(prompt.getType());
                message.setContent(formatPrompt(builder));
            }
        }
    }

    private String formatPrompt(StringBuilder text) {
        // Remove leading space/newlines
        while (!text.isEmpty() && text.charAt(0) == ' ' || text.charAt(0) == '\n') {
            text.delete(0, 1);
        }

        // Remove trailing space/newlines
        while (!text.isEmpty() && text.charAt(text.length()-1) == ' ' || text.charAt(text.length()-1) == '\n') {
            text.delete(text.length()-1, text.length());
        }

        // Remove whitespace at start of line
        String s = text.toString();
        while(s.contains("\n "))
            s = s.replace("\n ", "\n");

        // Convert non-breaking spaces to spaces
        s = s.replace('\u00A0', ' ');

        return s;
    }

}
