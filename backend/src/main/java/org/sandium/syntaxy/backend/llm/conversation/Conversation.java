package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.llm.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Conversation {

    private final Collection<ConversationListener> listeners;
    private final ArrayList<Message> messages;
    private final Set<String> agents;
    private String script;
    private Model model;
    private long amountSpentNanos;
    private boolean finished;

    public Conversation() {
        this.listeners = new ArrayList<>();
        messages = new ArrayList<>();
        agents = new HashSet<>();
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public Message addMessage() {
        Message message = new Message(this);
        messages.add(message);

        for (ConversationListener listener : listeners) {
            listener.messageAdded(message);
        }

        return message;
    }

    public void addListener(ConversationListener listener) {
        listeners.add(listener);
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

    public boolean isFinished() {
        return finished;
    }

    public void setFinished() {
        finished = true;

        // TODO Parse content

        for (ConversationListener listener : listeners) {
            listener.finished(this);
        }
    }

    public long getAmountSpentNanos() {
        return amountSpentNanos;
    }

    public void addUsage(int inputTokens, int outputTokens) {
        long amount = inputTokens * model.getInputTokenCost() + outputTokens * model.getOutputTokenCost();
        amountSpentNanos += amount;

        for (ConversationListener listener : listeners) {
            listener.usageUpdated(this, amount);
        }
    }
}
