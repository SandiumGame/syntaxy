package org.sandium.syntaxy.backend.llm.conversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Conversation {

    private final ArrayList<Interaction> interactions;
    private final Collection<ConversationListener> listeners;
    private final Set<String> agents;

    public Conversation() {
        interactions = new ArrayList<>();
        listeners = new ArrayList<>();
        agents = new HashSet<>();
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
        newInteraction.getMessages().addAll(oldInteraction.getMessages().stream().filter(Message::isKeepInConversation).toList());

        addIneraction(newInteraction);
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

    public void addAgent(String id) {
        agents.add(id);
    }

    public boolean containsAgent(String id) {
        return agents.contains(id);
    }

}
