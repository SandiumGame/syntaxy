package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;

public class InputPanel {

    private JBPanel<?> content;

    public InputPanel() {
        createPanel();
    }

    public JComponent getContent() {
        return content;
    }

    private void createPanel() {
        // Root panel
        content = new JBPanel<>();
        content.setLayout(new BorderLayout());
        Dimension minimumSize = new Dimension(10, 100);
        content.setMinimumSize(minimumSize);

        // Dropdown for model selection
        String[] models = {"GPT-4", "GPT-3.5", "Custom Model"};
        ComboBox<String> modelDropdown = new ComboBox<>(models);
        modelDropdown.setSelectedIndex(0);
        JBPanel<?> topPanel = new JBPanel<>(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Select Model: "));
        topPanel.add(modelDropdown);
        content.add(topPanel, BorderLayout.NORTH);

        // Text input area for user query
        JTextArea queryInput = new JTextArea(10, 100);
        queryInput.setLineWrap(true);
        queryInput.setWrapStyleWord(true);
        JBScrollPane scrollPane = new JBScrollPane(queryInput);
        content.add(scrollPane, BorderLayout.CENTER);

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String selectedModel = (String) modelDropdown.getSelectedItem();
            String userQuery = queryInput.getText();

            // Handle submission logic
            System.out.println("Model: " + selectedModel);
            System.out.println("Query: " + userQuery);

            // You can integrate with your AI backend here
        });
        JBPanel<?> bottomPanel = new JBPanel<>(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(submitButton);
        content.add(bottomPanel, BorderLayout.SOUTH);
    }
}
