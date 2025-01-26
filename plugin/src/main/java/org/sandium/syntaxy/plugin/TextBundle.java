package org.sandium.syntaxy.plugin;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.NotNull;

public class TextBundle extends DynamicBundle {

    private static final TextBundle instance = new TextBundle();

    private TextBundle() {
        super("messages.TextBundle");
    }

    public static String message(@NotNull String key, Object @NotNull ... params) {
        return instance.getMessage(key, params);
    }
}
