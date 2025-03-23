package org.sandium.syntaxy.backend.config;

import org.sandium.syntaxy.backend.config.agents.Agent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {

    private final Map<String, Agent> agents;

    public Config() {
        this.agents = new HashMap<>();
    }

    public void addAgent(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    public Agent getAgent(String id) {
        return agents.get(id);
    }

    public List<Agent> getAgentsByGroup(String group) {
        return agents.values().stream()
                .filter(agent -> group.equals(agent.getGroup()))
                .toList();
    }
}
