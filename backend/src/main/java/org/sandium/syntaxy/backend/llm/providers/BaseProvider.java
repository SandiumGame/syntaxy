package org.sandium.syntaxy.backend.llm.providers;

import org.sandium.syntaxy.backend.llm.conversation.Conversation;

public abstract class BaseProvider {

    public abstract void execute(Conversation conversation);

}
