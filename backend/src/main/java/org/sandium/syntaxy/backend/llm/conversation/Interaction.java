package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.llm.Model;

public class Interaction {

    private final Conversation conversation;
    private Model model;
    private String query;
    private StringBuilder content;
    private boolean finished;
    private long amountSpentNanos;

    public Interaction(Conversation conversation) {
        this.conversation = conversation;
        content = new StringBuilder();
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getAmountSpentNanos() {
        return amountSpentNanos;
    }

    public String getContent() {
        return content.toString();
    }

    public void addContent(String content) {
        this.content.append(content);
        for (ConversationListener listener : conversation.listeners) {
            listener.contentAdded(this);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;

        // TODO Parse content

        for (ConversationListener listener : conversation.listeners) {
            listener.interactionFinished(this);
        }
    }

    public void addUsage(int inputTokens, int outputTokens) {
        long amount = inputTokens * model.getInputTokenCost() + outputTokens * model.getOutputTokenCost();
        amountSpentNanos += amount;
        conversation.amountSpentNanos += amount;

        for (ConversationListener listener : conversation.listeners) {
            listener.usageUpdated(conversation, amount);
        }
    }
}
