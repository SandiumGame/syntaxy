package org.sandium.syntaxy.plugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

@State(
        name = "org.sandium.syntaxy.plugin.settings.AppSettings",
        storages = @Storage("SdkSettingsPlugin.xml")
)
final class AppSettings implements PersistentStateComponent<AppSettings.State> {

    static class State {
        @NonNls
        public String userId = "John Smith";
        public boolean ideaStatus = false;
        // Provider
        // Provider - Params
        // Usage
    }

    private State myState = new State();

    static AppSettings getInstance() {
        return ApplicationManager.getApplication().getService(AppSettings.class);
    }

    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

}
