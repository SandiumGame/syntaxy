package org.sandium.syntaxy.plugin.core;

import com.intellij.openapi.components.Service;
import org.sandium.syntaxy.backend.AiAssistant;
import org.sandium.syntaxy.backend.llm.LlmResultsHandler;

@Service(Service.Level.PROJECT)
public final class AiService {

    private AiAssistant aiAssistant;
    private Arr

    AiService() {
        aiAssistant = new AiAssistant();
    }

    // TODO Tracks usage
    // TODO Loads local/project scripts

    public void execute(String text) {
        aiAssistant.execute(text, new LlmResultsHandler() {
            @Override
            public void onContent(String text) {

            }

            @Override
            public void onContentFinished() {

            }

            @Override
            public void onMetadata(int inputTokens, int outputTokens) {

            }
        });
    }

}
