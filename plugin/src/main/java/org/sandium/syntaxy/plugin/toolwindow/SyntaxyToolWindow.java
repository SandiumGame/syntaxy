package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class SyntaxyToolWindow {

    public static final Insets INSET = JBUI.insets(2);
    public static final Insets TEXT_AREA_INSET = JBUI.insets(5);

    private final Project project;
    private OutputPanel outputPanel;
    private InputPanel inputPanel;
    private JBSplitter content;

    public SyntaxyToolWindow(Project project) {
        this.project = project;

        outputPanel = new OutputPanel();
        inputPanel = new InputPanel(project, outputPanel);

        // Create a split pane
        content = new JBSplitter();
        content.setProportion(0.75f);
        content.setSplitterProportionKey("org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.SplitterProportion");
        content.setOrientation(true);
        content.setFirstComponent(outputPanel.getContent());
        content.setSecondComponent(inputPanel.getContent());
    }

    public JComponent getContent() {
        return content;
    }

}
