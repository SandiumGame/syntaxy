package org.sandium.syntaxy.backend.llm.providers;

import org.sandium.syntaxy.backend.llm.conversation.Interaction;

public abstract class BaseProvider {

    public abstract void execute(Interaction interaction);

}
