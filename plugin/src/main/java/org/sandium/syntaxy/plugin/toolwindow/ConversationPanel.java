package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBPanel;
import org.sandium.syntaxy.backend.AiExecutor;
import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.ConversationListener;
import org.sandium.syntaxy.backend.llm.conversation.InteractionListener;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.plugin.core.AiService;

import javax.swing.*;
import java.util.ArrayList;

public class ConversationPanel {

    // Text
    // Code diff
    // JIRA ticket
    // Expand/collapse sections

    // Cost
    // Buttons - Confirm plan, apply diffs, create JIRA ticket, etc
    // Continue conversation

    private final Conversation conversation;
    private final ArrayList<MessagePanel> interactions;
    private final JBPanel<?> panel;
    private final AiService aiService;

    public ConversationPanel() {
        conversation = new Conversation();
        interactions = new ArrayList<>();
        aiService = ApplicationManager.getApplication().getService(AiService.class);
        panel = new JBPanel<>(new VerticalFlowLayout(true, false));

        conversation.addListener(new ConversationListener() {
            @Override
            public void interactionAdded(Interaction interaction) {
                ConversationPanel.this.interactionAdded(interaction);
            }
        });
    }

    public JBPanel<?> getPanel() {
        return panel;
    }

    private void interactionAdded(Interaction interaction) {
        interaction.addListener(new InteractionListener() {
            @Override
            public void messageAdded(Message message) {
                ConversationPanel.this.messageAdded(message);
            }

            @Override
            public void finished(Interaction interaction) {
                ConversationPanel.this.finished(interaction);
            }

            @Override
            public void usageUpdated(Interaction interaction, long amountSpentNanos) {
                ConversationPanel.this.usageUpdated(interaction, amountSpentNanos);
            }
        });
    }

    private void messageAdded(Message message) {
        SwingUtilities.invokeLater(() -> {
            MessagePanel messagePanel = new MessagePanel(message);
            interactions.add(messagePanel);
            panel.add(messagePanel.getPanel());
            panel.revalidate();
        });

    }

    private void finished(Interaction interaction) {
        System.out.println("Usage: $" + aiService.getTotalAmountSpentNanos() / 1000000000.0);
        // TODO enable submit button
    }

    private void usageUpdated(Interaction interaction, long amountSpentNanos) {
        aiService.addUsage(amountSpentNanos);
    }

    public void submit(String userQuery, ExecutionContext executionContext) {
        AiExecutor executor = aiService.getAiExecutor();

        Interaction interaction = conversation.addInteraction();
        interaction.setUserQuery(userQuery);
        interaction.setScript("MainRouter");
        interaction.setModel(executor.getDefaultModel());

        executor.execute(conversation, executionContext);
    }

}
