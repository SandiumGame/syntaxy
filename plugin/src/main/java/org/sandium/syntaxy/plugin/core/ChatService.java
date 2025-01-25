package org.sandium.syntaxy.plugin.core;

import com.intellij.openapi.components.Service;

@Service(Service.Level.PROJECT)
public final class ChatService {

    ChatService() {
    }

    public int getRandomNumber() {
        return 10;
    }
}
