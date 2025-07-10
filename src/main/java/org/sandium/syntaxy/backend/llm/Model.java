package org.sandium.syntaxy.backend.llm;

import org.sandium.syntaxy.backend.llm.providers.Provider;

public class Model {

    private final Provider provider;
    private final String name;
    private String id;
    private final String idRegex;
    private final long inputTokenCost;
    private final long outputTokenCost;

    public Model(Provider provider, String name, String id, String idRegex, long inputTokenCost, long outputTokenCost) {
        this.provider = provider;
        this.name = name;
        this.id = id;
        this.idRegex = idRegex;
        this.inputTokenCost = inputTokenCost;
        this.outputTokenCost = outputTokenCost;
    }

    public Provider getProvider() {
        return provider;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRegex() {
        return idRegex;
    }

    public long getInputTokenCost() {
        return inputTokenCost;
    }

    public long getOutputTokenCost() {
        return outputTokenCost;
    }
}
