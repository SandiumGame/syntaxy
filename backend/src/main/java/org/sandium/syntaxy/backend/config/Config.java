package org.sandium.syntaxy.backend.config;

import org.sandium.syntaxy.backend.config.agents.Agent;

import java.util.HashMap;
import java.util.Map;

public class Config {

    private Map<String, Agent> agents;

    public Config() {
        this.agents = new HashMap<>();
    }

    public void addAgent(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    public Agent getAgent(String id) {
        return agents.get(id);
    }
}
