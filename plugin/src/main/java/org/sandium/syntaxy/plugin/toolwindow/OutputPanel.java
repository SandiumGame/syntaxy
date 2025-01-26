package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;

import javax.swing.*;
import java.awt.*;

public class OutputPanel {

    private JBPanel<?> content;
    private JBPanel<?> output;

    public OutputPanel() {
        createPanel();

        for (int i=0; i < 10; i++) {
            add("Hello " + i);
        }
        output.add(Box.createVerticalGlue());
    }

    public JComponent getContent() {
        return content;
    }

    private void createPanel() {
        // Root panel
        content = new JBPanel<>();
        content.setLayout(new BorderLayout());
        content.setMinimumSize(new Dimension(10, 100));

        output = new JBPanel<>(new GridBagLayout());

        JBScrollPane scrollPane = new JBScrollPane(output);
        content.add(scrollPane, BorderLayout.CENTER);
    }

    private void add(String text) {
        JBTextArea queryInput = new JBTextArea();
        queryInput.setLineWrap(true);
        queryInput.setWrapStyleWord(true);
        queryInput.setText(text);
        queryInput.setBorder(BorderFactory.createLineBorder(JBColor.border()));

        output.add(queryInput);
        output.add(Box.createRigidArea(new Dimension(0,5)));
    }

}
