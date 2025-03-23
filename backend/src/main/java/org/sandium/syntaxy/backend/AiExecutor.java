package org.sandium.syntaxy.backend;

import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.config.ConfigXmlParser;
import org.sandium.syntaxy.backend.config.agents.Agent;
import org.sandium.syntaxy.backend.llm.Model;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.llm.providers.BaseProvider;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

import java.io.IOException;

public class AiExecutor {

    // TODO Needs to come from file
    private final long INPUT_TOKEN_COST = (long)(0.0008 / 1000 * 1000000000L);
    private final long OUTPUT_TOKEN_COST = (long)(0.004 / 1000 * 1000000000L);

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

    private Config config;
    private BaseProvider provider;
    private Model model = new Model("anthropic.claude-3-haiku-20240307-v1:0", INPUT_TOKEN_COST, OUTPUT_TOKEN_COST);

    public AiExecutor() {
        config = new Config();
        try {
            ConfigXmlParser parser = new ConfigXmlParser(AiExecutor.class.getResourceAsStream("/config.xml"), config);
            parser.parse();
        } catch (Exception e) {
            // TODO Need to handle errors
            throw new RuntimeException(e);
        }

        provider = new Bedrock();
    }

    public Model getDefaultModel() {
        return model;
    }

    public void execute(Conversation conversation, ExecutionContext executionContext) {
        new Thread(() -> {
            for (Message message : conversation.getMessages()) {
                if (message.getMessageType() == null) {
                    throw new RuntimeException("Message type can not be null.");
                }
            }

            // TODO Find script. Format message text. Pass to LLM.
            Agent agent = config.getAgent(conversation.getScript());
            if (agent == null) {
                // TODO Handle bad agent. Should really happen
                return;
            }

            agent.execute(conversation, executionContext, provider);

            // TODO Verify model and other fields

            // bedrock.execute(conversation);

            // TODO Process results. i.e. What script if any to run next


        }).start();
    }

}
