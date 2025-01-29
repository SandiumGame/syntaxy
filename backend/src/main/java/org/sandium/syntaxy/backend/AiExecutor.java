package org.sandium.syntaxy.backend;

import org.sandium.syntaxy.backend.llm.Model;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

public class AiExecutor {

    // TODO Needs to come from file
    private final long INPUT_TOKEN_COST = (long)(0.003 / 1000 * 1000000000L);
    private final long OUTPUT_TOKEN_COST = (long)(0.015 / 1000 * 1000000000L);

    /*
     * Categorize question
     *  - determines what information / execution scripts need to be run
     * 
     * TODO ability to prompt user to continue
     * TODO ability to mark that a conversation should end
     * TODO ability for user to pick the root script that should be run
     *
     * TODO Model file with aliases and pricing
     * 
     */

    private Bedrock bedrock;
    private Model model = new Model("anthropic.claude-3-haiku-20240307-v1:0", INPUT_TOKEN_COST, OUTPUT_TOKEN_COST);

    public AiExecutor() {
        bedrock = new Bedrock();
    }

    public Model getDefaultModel() {
        return model;
    }

    public void execute(Conversation conversation) {
        new Thread(() -> {
            int size = conversation.getInteractions().size();
            if (size == 0) {
                throw new RuntimeException("Conversation does not have any interactions to execute");
            }
            Interaction interaction = conversation.getInteractions().get(size - 1);
            if (interaction.isFinished()) {
                throw new RuntimeException("Interaction has already been executed");
            }

            bedrock.execute(interaction);
        }).start();
    }

}
