package org.sandium.syntaxy.backend.llm;

public class Model {

    private String name;
    private String pattern;
    private String id;
    private long inputTokenCost;
    private long outputTokenCost;

    public Model(String name, String pattern, long inputTokenCost, long outputTokenCost) {
        this.name = name;
        this.pattern = pattern;
        this.inputTokenCost = inputTokenCost;
        this.outputTokenCost = outputTokenCost;
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

    public String getPattern() {
        return pattern;
    }

    public long getInputTokenCost() {
        return inputTokenCost;
    }

    public long getOutputTokenCost() {
        return outputTokenCost;
    }
}
