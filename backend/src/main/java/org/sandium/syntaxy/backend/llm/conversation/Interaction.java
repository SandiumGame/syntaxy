package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.config.agents.Agent;
import org.sandium.syntaxy.backend.llm.Model;

import java.util.ArrayList;
import java.util.Collection;

public class Interaction {

    private final Conversation conversation;
    final Collection<InteractionListener> listeners;
    private String userQuery;
    private final ArrayList<Message> messages;
    private Agent agent;
    private long amountSpentNanos;
    private boolean finished;

    public Interaction(Conversation conversation) {
        this.conversation = conversation;
        listeners = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public Conversation getConversation() {
        return conversation;
    }

    public void addListener(InteractionListener listener) {
        listeners.add(listener);
    }

    public String getUserQuery() {
        return userQuery;
    }

    public void setUserQuery(String userQuery) {
        this.userQuery = userQuery;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Message addMessage() {
        Message message = new Message(this);
        messages.add(message);

        for (InteractionListener listener : listeners) {
            listener.messageAdded(message);
        }

        return message;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public long getAmountSpentNanos() {
        return amountSpentNanos;
    }

    public void addUsage(int inputTokens, int outputTokens) {
        Model model = agent.getModel();

        long amount = inputTokens * model.getInputTokenCost() + outputTokens * model.getOutputTokenCost();
        amountSpentNanos += amount;

        for (InteractionListener listener : listeners) {
            listener.usageUpdated(this, amount);
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;

        // TODO Parse content

        for (InteractionListener listener : listeners) {
            listener.finished(this);
        }
    }


}
