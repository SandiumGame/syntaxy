package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTextArea;
import org.sandium.syntaxy.backend.AiResult;
import org.sandium.syntaxy.backend.AiResultListener;

import javax.swing.*;
import java.awt.*;

import static org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.INSET;
import static org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.TEXT_AREA_INSET;

public class OutputPanel {

    private JBPanel<?> content;
    private JBPanel<?> output;

    public OutputPanel() {
        createPanel();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weighty = 1;
        output.add(new JPanel(), constraints);

        for (int i=0; i < 10; i++) {
            add("Hello " + i);
        }
    }

    public JComponent getContent() {
        return content;
    }

    AiResultListener getListener() {
        return new AiResultListener() {
            public void contentUpdated(AiResult result) {
                System.out.println("Debug output");
            }
        };
    }

    private void createPanel() {
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
        queryInput.setMargin(TEXT_AREA_INSET);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = output.getComponentCount();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.insets = INSET;

        output.add(queryInput, constraints);
    }

}
