package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.backend.llm.conversation.InteractionListener;

import javax.swing.*;
import java.awt.*;

public class InteractionPanel {

    public static final Insets TEXT_AREA_INSET = JBUI.insets(5);

    private Interaction interaction;
    private final JBPanel<?> panel;
    JBTextArea content;

    public InteractionPanel(Interaction interaction) {
        this.interaction = interaction;

        panel = new JBPanel<>(new VerticalFlowLayout(true, false));
        content = new JBTextArea();
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setMargin(TEXT_AREA_INSET);

        panel.add(content);

        interaction.addListener(new InteractionListener() {
            @Override
            public void contentAdded(Interaction interaction) {
                SwingUtilities.invokeLater(() -> {
                    content.setText(interaction.getContent());
                    content.revalidate();
                });
            }
        });
    }

    public JBPanel<?> getPanel() {
        return panel;
    }

}
