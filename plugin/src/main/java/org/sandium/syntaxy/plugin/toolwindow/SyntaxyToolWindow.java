package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;

import javax.swing.*;

public class SyntaxyToolWindow {

    private final Project project;
    private OutputPanel outputPanel;
    private InputPanel inputPanel;
    private JBSplitter content;

    public SyntaxyToolWindow(Project project) {
        this.project = project;

        outputPanel = new OutputPanel();
        inputPanel = new InputPanel();

        //Create a split pane with the two scroll panes in it.
        content = new JBSplitter();
        content.setProportion(0.75f);
        // TODO
        content.setSplitterProportionKey("org.sandium.syntaxy.plugin.toolwindow.SyntaxyToolWindow.SplitterProportion");
        content.setOrientation(true);
        content.setFirstComponent(outputPanel.getContent());
        content.setSecondComponent(inputPanel.getContent());
    }

    public JComponent getContent() {
        return content;
    }

}
