package org.sandium.syntaxy.backend.llm.conversation;

import org.sandium.syntaxy.backend.llm.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

public class Conversation {

    private final ArrayList<Interaction> interactions;
    private final Collection<ConversationListener> listeners;

    public Conversation() {
        interactions = new ArrayList<>();
        listeners = new ArrayList<>();
    }

    public ArrayList<Interaction> getInteractions() {
        return interactions;
    }

    public void addListener(ConversationListener listener) {
        listeners.add(listener);
    }

    public Interaction addInteraction() {
        Interaction interaction = new Interaction(this);
        addIneraction(interaction);
        return interaction;
    }

    public void copyCurrentInteraction() {
        Interaction oldInteraction = getCurrentInteraction();
        Interaction newInteraction = new Interaction(this);

        newInteraction.setUserQuery(oldInteraction.getUserQuery());

        private String userQuery;
        private final ArrayList<Message> messages; // TODO Remove temp messages
        private final Set<String> agents; // TODO move to conversation?
        private String script; // TODO Only need for the initial start
        private Model model;

        addIneraction(interaction);
    }

    private void addIneraction(Interaction interaction) {
        interactions.add(interaction);

        for (ConversationListener listener : listeners) {
            listener.interactionAdded(interaction);
        }
    }


    public Interaction getCurrentInteraction() {
        return interactions.get(interactions.size()-1);
    }

}
