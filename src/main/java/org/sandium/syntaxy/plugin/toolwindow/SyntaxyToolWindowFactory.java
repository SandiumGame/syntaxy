package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class SyntaxyToolWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SyntaxyToolWindow window = new SyntaxyToolWindow(project);
        Content content = ContentFactory.getInstance().createContent(window.getContent(), null, false);
        toolWindow.getContentManager().addContent(content);
    }

}
