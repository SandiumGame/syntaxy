package org.sandium.syntaxy.backend.config;

import org.sandium.syntaxy.backend.config.agents.Agent;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public Map<String, Agent> agents;

    public Config() {
        this.agents = new HashMap<>();
    }

    public void addAgent(Agent agent) {
        agents.put(agent.getId(), agent);
    }
}
