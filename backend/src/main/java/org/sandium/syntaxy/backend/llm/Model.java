package org.sandium.syntaxy.backend.llm;

public class Model {

    private String name;
    private long inputTokenCost;
    private long outputTokenCost;

    public Model(String name, long inputTokenCost, long outputTokenCost) {
        this.name = name;
        this.inputTokenCost = inputTokenCost;
        this.outputTokenCost = outputTokenCost;
    }

    public String getName() {
        return name;
    }

    public long getInputTokenCost() {
        return inputTokenCost;
    }

    public long getOutputTokenCost() {
        return outputTokenCost;
    }
}
