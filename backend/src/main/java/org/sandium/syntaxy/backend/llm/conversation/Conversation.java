package org.sandium.syntaxy.backend.llm.conversation;

import java.util.ArrayList;
import java.util.Collection;

public class Conversation {

    Collection<ConversationListener> listeners;
    private ArrayList<Interaction> interactions;
    long amountSpentNanos;
    private boolean finished;

    public Conversation() {
        this.listeners = new ArrayList<>();
        interactions = new ArrayList<>();
    }

    public ArrayList<Interaction> getInteractions() {
        return interactions;
    }

    public Interaction addInteraction() {
        Interaction interaction = new Interaction(this);
        interactions.add(interaction);
        return interaction;
    }

    public void addListener(ConversationListener listener) {
        listeners.add(listener);
    }

    public boolean isFinished() {
        return finished;
    }

    public long getAmountSpentNanos() {
        return amountSpentNanos;
    }

}
