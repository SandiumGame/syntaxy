package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.sandium.syntaxy.backend.AiExecutor;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.ConversationListener;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.plugin.core.AiService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class SyntaxyToolWindow {

    public static final Insets INSET = JBUI.insets(2);
    public static final Insets TEXT_AREA_INSET = JBUI.insets(5);

    private final Project project;
    private final ArrayList<ConversationPanel> conversationPanels;
    private final AiService aiService;
    private JBSplitter content;
    private JBPanel<?> output;
    private ComboBox<String> modelDropdown;
    private JBTextArea queryInput;

    public SyntaxyToolWindow(Project project) {
        this.project = project;
        conversationPanels = new ArrayList<>();
        aiService = ApplicationManager.getApplication().getService(AiService.class);
        createPanel();
    }

    public JComponent getContent() {
        return content;
    }

    private void createPanel() {
        // Create a split pane
        content = new JBSplitter();
        content.setProportion(0.75f);
        content.setSplitterProportionKey("org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.SplitterProportion");
        content.setOrientation(true);
        content.setFirstComponent(createOutputPanel());
        content.setSecondComponent(createInputPanel());
    }

    private JBPanel<?> createOutputPanel() {
        JBPanel<?> outputRoot = new JBPanel<>();
        outputRoot.setLayout(new BorderLayout());
        outputRoot.setMinimumSize(new Dimension(10, 100));

        output = new JBPanel<>(new GridBagLayout());

        JBScrollPane scrollPane = new JBScrollPane(output);
        outputRoot.add(scrollPane, BorderLayout.CENTER);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        output.add(new JPanel(), constraints);

        return outputRoot;
    }

    private JBPanel<?> createInputPanel() {
        // Root panel
        JBPanel<?> inputRoot = new JBPanel<>();
        inputRoot.setLayout(new GridBagLayout());
        inputRoot.setMinimumSize(new Dimension(10, 100));

        // Dropdown for model selection
        String[] models = {"GPT-4", "GPT-3.5", "Custom Model"};
        modelDropdown = new ComboBox<>(models);
        modelDropdown.setSelectedIndex(0);
        inputRoot.add(new JLabel("Select Model: "), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.LINE_START, GridBagConstraints.NONE, INSET, 0, 0));
        inputRoot.add(modelDropdown, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.LINE_START, GridBagConstraints.HORIZONTAL, INSET, 0, 0));

        // Text input area for user query
        queryInput = new JBTextArea();
        queryInput.setLineWrap(true);
        queryInput.setWrapStyleWord(true);
        queryInput.setMargin(TEXT_AREA_INSET);
        JBScrollPane scrollPane = new JBScrollPane(queryInput);
        inputRoot.add(scrollPane, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, INSET, 0, 0));

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(this::submit);
        JBPanel<?> bottomPanel = new JBPanel<>(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(submitButton);
        inputRoot.add(bottomPanel, new GridBagConstraints(0, 2, 2, 1, 1, 0, GridBagConstraints.LINE_END, GridBagConstraints.HORIZONTAL, INSET, 0, 0));

        return inputRoot;
    }

    public void submit(ActionEvent e) {
        String selectedModel = (String) modelDropdown.getSelectedItem();
        String userQuery = queryInput.getText();

        ConversationPanel conversationPanel = new ConversationPanel();
        conversationPanels.add(conversationPanel);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = output.getComponentCount();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = INSET;
        output.add(conversationPanel.getPanel(), constraints);
        output.revalidate();

        conversationPanel.submit(userQuery);
    }
}
