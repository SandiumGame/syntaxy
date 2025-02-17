package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.llm.conversation.MessageListener;

import javax.swing.*;
import java.awt.*;

public class MessagePanel {

    public static final Insets TEXT_AREA_INSET = JBUI.insets(5);

    private Message message;
    private final JBPanel<?> panel;
    JBTextArea content;

    public MessagePanel(Message message) {
        this.message = message;

        panel = new JBPanel<>(new VerticalFlowLayout(true, false));
        content = new JBTextArea();
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        content.setMargin(TEXT_AREA_INSET);

        panel.add(content);

        message.addListener(new MessageListener() {
            @Override
            public void contentAdded(Message message) {
                SwingUtilities.invokeLater(() -> {
                    updateContentArea();
                });
            }
        });
        updateContentArea();
    }

    public JBPanel<?> getPanel() {
        return panel;
    }

    private void updateContentArea() {
        content.setText(message.getContent());
        content.revalidate();
    }

}
