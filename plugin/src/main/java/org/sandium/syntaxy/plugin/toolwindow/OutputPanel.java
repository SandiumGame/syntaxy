package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.ui.components.JBPanel;

import javax.swing.*;
import java.awt.*;

public class OutputPanel {

    private JBPanel<?> content;

    public OutputPanel() {
        createPanel();
    }

    public JComponent getContent() {
        return content;
    }

    private void createPanel() {
        // Root panel
        content = new JBPanel<>();
        content.setLayout(new BorderLayout());
        Dimension minimumSize = new Dimension(10, 50);
        content.setMinimumSize(minimumSize);

    }

}
