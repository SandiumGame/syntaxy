package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.llm.Model;

import java.util.ArrayList;
import java.util.Collection;

public class Interaction {

    private final Conversation conversation;
    private Collection<InteractionListener> listeners;
    private Model model;
    private String query;
    private final StringBuilder content;
    private boolean finished;
    private long amountSpentNanos;

    public Interaction(Conversation conversation) {
        this.conversation = conversation;
        listeners = new ArrayList<>();
        content = new StringBuilder();
    }

    public void addListener(InteractionListener listener) {
        listeners.add(listener);
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
        for (InteractionListener listener : listeners) {
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
