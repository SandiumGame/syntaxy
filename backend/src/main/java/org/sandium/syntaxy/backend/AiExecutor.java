package org.sandium.syntaxy.backend;

import org.sandium.syntaxy.backend.config.Config;
import org.sandium.syntaxy.backend.config.ConfigXmlParser;
import org.sandium.syntaxy.backend.config.agents.Agent;
import org.sandium.syntaxy.backend.llm.Model;
import org.sandium.syntaxy.backend.llm.conversation.Conversation;
import org.sandium.syntaxy.backend.llm.conversation.Interaction;
import org.sandium.syntaxy.backend.llm.conversation.Message;
import org.sandium.syntaxy.backend.llm.providers.Provider;
import org.sandium.syntaxy.backend.llm.providers.Bedrock;

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
    private Provider provider;

    public AiExecutor() {
        config = new Config();
        config.addProvider(new Bedrock());

        try {
            ConfigXmlParser parser = new ConfigXmlParser(AiExecutor.class.getResourceAsStream("/config.xml"), config);
            parser.parse();

//            for (BaseProvider provider : config.getProviders()) {
//                provider.initModels();
//            }
        } catch (Exception e) {
            // TODO Need to handle errors
            throw new RuntimeException(e);
        }

    }

    private void setAgentModels() {
        for (Agent agent : config.getAgents()) {
            Model model = provider.getModel(agent.getModelName());

            if (model == null) {
                throw new RuntimeException("Could not find matching model " + agent.getModelName());
            }
            agent.setModel(model);
        }
    }

    public Config getConfig() {
        return config;
    }

    public void execute(Conversation conversation, ExecutionContext executionContext) {
        new Thread(() -> {
            Interaction interaction = conversation.getCurrentInteraction();
            for (Message message : interaction.getMessages()) {
                if (message.getMessageType() == null) {
                    throw new RuntimeException("Message type can not be null.");
                }
            }

            // TODO Find script. Format message text. Pass to LLM.
            Agent agent = interaction.getAgent();
            if (agent == null) {
                // TODO Handle bad agent. Should really happen
                throw new RuntimeException("Agent not set");
            }

            agent.execute(conversation, executionContext, provider);

            // TODO Verify model and other fields

            // bedrock.execute(conversation);

            // TODO Process results. i.e. What script if any to run next


        }).start();
    }

}
