package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.sandium.syntaxy.backend.AiResult;
import org.sandium.syntaxy.backend.AiResultListener;
import org.sandium.syntaxy.plugin.core.AiService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.INSET;
import static org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.TEXT_AREA_INSET;

public class InputPanel {

    private final AiService aiService;
    private final ArrayList<AiResultListener> listeners;
    private JBPanel<?> content;

    public InputPanel(Project project, OutputPanel outputPanel) {
        aiService = ApplicationManager.getApplication().getService(AiService.class);

        listeners = new ArrayList<>();
        listeners.add(outputPanel.getListener());
        listeners.add(new AiResultListener() {
            public void contentUpdated(AiResult result) {
                System.out.println("Debug input");
            }

            public void usageUpdated(AiResult result, long amountSpentNanos) {
                aiService.addUsage(amountSpentNanos);
            }
        });

        createPanel();
    }

    public JComponent getContent() {
        return content;
    }

    private void createPanel() {
        // Root panel
        content = new JBPanel<>();
        content.setLayout(new GridBagLayout());
        content.setMinimumSize(new Dimension(10, 100));

        // Dropdown for model selection
        String[] models = {"GPT-4", "GPT-3.5", "Custom Model"};
        ComboBox<String> modelDropdown = new ComboBox<>(models);
        modelDropdown.setSelectedIndex(0);
        content.add(new JLabel("Select Model: "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, INSET, 0, 0));
        content.add(modelDropdown, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, INSET, 0, 0));

        // Text input area for user query
        JBTextArea queryInput = new JBTextArea();
        queryInput.setLineWrap(true);
        queryInput.setWrapStyleWord(true);
        queryInput.setMargin(TEXT_AREA_INSET);
        JBScrollPane scrollPane = new JBScrollPane(queryInput);
        content.add(scrollPane, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, INSET, 0, 0));

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            String selectedModel = (String) modelDropdown.getSelectedItem();
            String userQuery = queryInput.getText();

            // TODO Need to pass model to use, open files, etc.
            aiService.getAiAssistant().execute(userQuery, listeners);
        });
        JBPanel<?> bottomPanel = new JBPanel<>(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(submitButton);
        content.add(bottomPanel, new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, INSET, 0, 0));
    }
}
