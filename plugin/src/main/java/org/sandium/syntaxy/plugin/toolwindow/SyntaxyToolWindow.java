package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.sandium.syntaxy.plugin.core.ChatService;

import javax.swing.*;

public class SyntaxyToolWindow implements ToolWindowFactory {

    @Override
    public void init(@NotNull ToolWindow toolWindow) {
        ToolWindowFactory.super.init(toolWindow);
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(toolWindow);
        Content content = ContentFactory.getInstance().createContent(myToolWindow.getContent(), null, false);
        toolWindow.getContentManager().addContent(content);
    }

    public class MyToolWindow {

        private ChatService chatService;

        public MyToolWindow(ToolWindow toolWindow) {
            chatService = toolWindow.getProject().getService(ChatService.class);
        }

        public JComponent getContent() {
            JBPanel panel = new JBPanel();

            var label = new JBLabel(TextBundle.message("randomLabel", "?"));
            panel.add(label);

            JButton button = new JButton(TextBundle.message("shuffle"));
            panel.add(button);
            button.addActionListener(e -> {
                label.setText(TextBundle.message("randomLabel", chatService.getRandomNumber()));
            });

            return panel;
        }
    }
}
