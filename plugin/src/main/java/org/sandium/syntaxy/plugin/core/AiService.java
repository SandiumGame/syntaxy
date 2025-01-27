package org.sandium.syntaxy.plugin.core;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.Service;
import org.sandium.syntaxy.backend.AiAssistant;

@Service(Service.Level.APP)
public final class AiService {

    private static final String AMOUNT_SPENT = "org.sandium.syntaxy.plugin.core.AiService.totalAmountSpent";

    private AiAssistant aiAssistant;
    private long totalAmountSpentNanos;

    // TODO Loads local/project scripts - Can at least get the dir

    AiService() {
        aiAssistant = new AiAssistant(); // TODO Need to set provider
        try {
            totalAmountSpentNanos = Long.parseLong(PropertiesComponent.getInstance().getValue(AMOUNT_SPENT, "0"));
        } catch (NumberFormatException e) {
            totalAmountSpentNanos = 0;
        }
    }

    public AiAssistant getAiAssistant() {
        return aiAssistant;
    }

    public long getTotalAmountSpentNanos() {
        return totalAmountSpentNanos;
    }

    public void addUsage(long amountSpentNanos) {
        totalAmountSpentNanos += amountSpentNanos;
        PropertiesComponent.getInstance().setValue(AMOUNT_SPENT, Long.toString(totalAmountSpentNanos));
    }

}
