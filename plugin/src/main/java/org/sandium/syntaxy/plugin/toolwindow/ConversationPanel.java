package org.sandium.syntaxy.plugin.toolwindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.VerticalFlowLayout;
import com.intellij.ui.components.JBPanel;
import org.sandium.syntaxy.backend.AiExecutor;
import org.sandium.syntaxy.backend.ExecutionContext;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.ConversationListener;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.config.prompt.PromptType;
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
            public void messageAdded(Message message) {
                SwingUtilities.invokeLater(() -> {
                    MessagePanel messagePanel = new MessagePanel(message);
                    interactions.add(messagePanel);
                    panel.add(messagePanel.getPanel());
                    panel.revalidate();
                });
            }

            @Override
            public void finished(Conversation conversation) {
                System.out.println("Usage: $" + aiService.getTotalAmountSpentNanos() / 1000000000.0);
                // TODO enable submit button
            }

            @Override
            public void usageUpdated(Conversation result, long amountSpentNanos) {
                aiService.addUsage(amountSpentNanos);
            }
        });
    }

    public JBPanel<?> getPanel() {
        return panel;
    }

    public void submit(String userQuery, ExecutionContext executionContext) {
        AiExecutor executor = aiService.getAiExecutor();

        executionContext.setUserQuery(userQuery);

        conversation.setScript("GeneralQueryAgent");
        conversation.setModel(executor.getDefaultModel());
        executor.execute(conversation, executionContext);
    }

}
