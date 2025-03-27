package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.llm.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Interaction {

    private final Conversation conversation;
    final Collection<InteractionListener> listeners;
    private String userQuery;
    private final ArrayList<Message> messages;
    private final Set<String> agents;
    private String script;
    private Model model;
    private long amountSpentNanos;
    private boolean finished;

    public Interaction(Conversation conversation) {
        this.conversation = conversation;
        listeners = new ArrayList<>();
        messages = new ArrayList<>();
        agents = new HashSet<>();
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

    public void addAgent(String id) {
        agents.add(id);
    }

    public boolean containsAgent(String id) {
        return agents.contains(id);
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public long getAmountSpentNanos() {
        return amountSpentNanos;
    }

    public void addUsage(int inputTokens, int outputTokens) {
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
