package org.sandium.syntaxy.backend.llm.providers;

import org.sandium.syntaxy.backend.llm.Model;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;

import java.util.HashMap;
import java.util.Map;

public abstract class Provider {

    private final String id;
    private final Map<String, Model> models;
    private final Map<String, String> modelAliases;

    public Provider(String id) {
        this.id = id;
        models = new HashMap<>();
        modelAliases = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void addModel(Model model) {
        verifyModel(model);
        models.put(model.getName(), model);
    }

    public void addModelAlias(String name, String alias) {
        modelAliases.put(alias, name);
    }

    public Model getModel(String modelName) {
        // TODO Look up aliases
        return null;
    }

    protected abstract void verifyModel(Model model);

    public abstract void execute(Interaction interaction);

}
