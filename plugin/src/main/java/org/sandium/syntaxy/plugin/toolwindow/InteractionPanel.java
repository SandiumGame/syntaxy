package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;

import java.awt.*;

public class InteractionPanel {

    public static final Insets TEXT_AREA_INSET = JBUI.insets(5);

    private Interaction interaction;
    private final JBPanel<?> panel;

    public InteractionPanel(Interaction interaction) {
        this.interaction = interaction;

        panel = new JBPanel<>(new VerticalFlowLayout(true, false));

        JBTextArea queryInput = new JBTextArea();
        queryInput.setLineWrap(true);
        queryInput.setWrapStyleWord(true);
        queryInput.setText("Foo");
        queryInput.setMargin(TEXT_AREA_INSET);

        panel.add(queryInput);
    }

    public JBPanel<?> getPanel() {
        return panel;
    }

}
