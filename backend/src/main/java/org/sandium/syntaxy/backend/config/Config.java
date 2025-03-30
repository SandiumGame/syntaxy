package org.sandium.syntaxy.backend.config;

import org.sandium.syntaxy.backend.config.agents.Agent;
import org.sandium.syntaxy.backend.llm.providers.Provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private final Map<String, Agent> agents;
    private final Map<String, Provider> providers;

    public Config() {
        agents = new HashMap<>();
        providers = new HashMap<>();
    }

    public void addAgent(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    public Collection<Agent> getAgents() {
        return agents.values();
    }

    public Agent getAgent(String id) {
        return agents.get(id);
    }

    public List<Agent> getAgentsByGroup(String group) {
        return agents.values().stream()
                .filter(agent -> group.equals(agent.getGroup()))
                .toList();
    }

    public void addProvider(Provider provider) {
        providers.put(provider.getId(), provider);
    }

    public Collection<Provider> getProviders() {
        return providers.values();
    }

    public Provider getProvider(String id) {
        return providers.get(id);
    }

}
