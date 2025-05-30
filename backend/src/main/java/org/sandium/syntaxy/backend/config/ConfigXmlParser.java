package org.sandium.syntaxy.backend.config;

import org.sandium.syntaxy.backend.config.agents.Agent;
import org.sandium.syntaxy.backend.config.prompt.ListRoutesSnippet;
import org.sandium.syntaxy.backend.config.prompt.Prompt;
import org.sandium.syntaxy.backend.config.prompt.PromptType;
import org.sandium.syntaxy.backend.config.prompt.TextSnippet;
import org.sandium.syntaxy.backend.config.prompt.UserLocaleSnippet;
import org.sandium.syntaxy.backend.config.prompt.UserQuerySnippet;
import org.sandium.syntaxy.backend.llm.Model;
import org.sandium.syntaxy.backend.llm.providers.Provider;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ConfigXmlParser {

    private final InputStream xml;
    private final Config config;
    private XMLStreamReader reader;

    public ConfigXmlParser(InputStream xml, Config config) throws IOException {
        String text = new String(xml.readAllBytes(), StandardCharsets.UTF_8)
                .replace("&nbsp;", "&#160;")
                .replace("&lt;", "&#60;")
                .replace("&gt;", "&#62;")
                .replace("&amp;", "&#38;")
                .replace("&quot;", "&#34;")
                .replace("&apos;", "&#39;");
        this.xml = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        this.config = config;
    }

    public void parse() {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLStreamReader(xml);

            parseChildren(null, Map.of("config", this::parseConfigElement));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (XMLStreamException e) {
                // Ignore exceptions
            }
            reader = null;
        }
    }

    private void parseConfigElement() throws XMLStreamException {
        verifyNoAttributes();

        parseChildren(null, Map.of(
                "models", this::parseModelsElement,
                "agent", this::parseAgentElement));
    }

    private void parseModelsElement() throws XMLStreamException {
        verifyAttributes("provider");

        Provider provider = config.getProvider(getAttribute("provider"));
        if (provider == null) {
            error("Unknown provider " + getAttribute("provider"));
            return;
        }

        parseChildren(null, Map.of(
                "model", () -> parseModelElement(provider),
                "modelAlias", () -> parseModelAliasElement(provider)));
    }

    private void parseModelElement(Provider provider) throws XMLStreamException {
        verifyAttributes("name", "id", "inputCost", "outputCost");

        StringBuilder idRegex = new StringBuilder();

        idRegex.append("^");
        String id = getAttribute("id");
        id.chars().forEach(value -> {
            if (value == '*') {
                idRegex.append(".*");
            } else {
                String hex = Integer.toHexString(value);
                idRegex.append("\\u");
                idRegex.append("0".repeat(Math.max(0, 4 - hex.length())));
                idRegex.append(hex);
            }
        });
        idRegex.append("$");

        Model model = new Model(provider,
                getAttribute("name"),
                id,
                idRegex.toString(),
                (long) (getDoubleAttribute("inputCost") / 1000000.0 * 1000000000.0),
                (long) (getDoubleAttribute("outputCost") / 1000000.0 * 1000000000.0));
        provider.addModel(model);

        verifyNoChildElements();
    }

    private void parseModelAliasElement(Provider provider) throws XMLStreamException {
        provider.addModelAlias(getAttribute("name"), getAttribute("alias"));

        verifyNoChildElements();
    }

    private void parseAgentElement() throws XMLStreamException {
        verifyAttributes("id", "group", "model", "title", "description");
        Agent agent = new Agent(config);
        agent.setId(getAttribute("id"));
        agent.setGroup(getAttribute("group", null));
        agent.setModelName(getAttribute("model"));
        agent.setTitle(getAttribute("title", null));
        agent.setDescription(getAttribute("description", null));

        parseChildren(null, Map.of(
                "system", () -> parsePrompt(agent, PromptType.SYSTEM),
                "prompt", () -> parsePrompt(agent, PromptType.USER),
                    "output", () -> parseOutput(agent)));

        config.addAgent(agent);
    }

    private void parsePrompt(Agent agent, PromptType promptType) throws XMLStreamException {
        verifyAttributes("onInitialConversation", "onContinuedConversation", "keepInConversation");

        Prompt prompt = new Prompt();
        prompt.setType(promptType);
        prompt.setOnInitialConversation(getBooleanAttribute("onInitialConversation"));
        prompt.setOnContinuedConversation(getBooleanAttribute("onContinuedConversation"));
        prompt.setKeepInConversation(getBooleanAttribute("keepInConversation"));
        agent.getPrompts().add(prompt);

        parseChildren(text -> prompt.getSnippets().add(new TextSnippet(text)), Map.of(
                "listRoutes", () -> parseListRoutes(prompt),
                "userQuery", () -> parseUserQuery(prompt),
                "userLocale", () -> parseUserLocale(prompt)));
    }

    private void parseOutput(Agent agent) throws XMLStreamException {
        verifyAttributes("showOutput", "endConversation");

        agent.setShowOutput(getBooleanAttribute("showOutput", true));
        agent.setEndConversation(getBooleanAttribute("endConversation", false));

        parseChildren(null, Map.of(
                "routeToAgent", () -> parseRouteToAgent(agent)));
    }

    private void parseRouteToAgent(Agent agent) throws XMLStreamException {
        verifyNoAttributes();
        verifyNoChildElements();

        agent.setRouteToAgent(true);
    }

    private void parseListRoutes(Prompt prompt) throws XMLStreamException {
        verifyAttributes("group");

        String group = getAttribute("group");
        prompt.getSnippets().add(new ListRoutesSnippet(group));

        verifyNoChildElements();
    }

    private void parseUserQuery(Prompt prompt) throws XMLStreamException {
        verifyNoAttributes();
        verifyNoChildElements();
        prompt.getSnippets().add(new UserQuerySnippet());
    }

    private void parseUserLocale(Prompt prompt) throws XMLStreamException {
        verifyNoAttributes();
        verifyNoChildElements();
        prompt.getSnippets().add(new UserLocaleSnippet());
    }

    private void error(String error) {
        Location location = reader.getLocation();
        throw new RuntimeException(error + " at line %s column %s.".formatted(location.getLineNumber(), location.getColumnNumber()));
    }

    private void verifyNoAttributes() {
        if (reader.getAttributeCount() > 0) {
            error("Unexpected attribute %s in <%s>".formatted(reader.getAttributeLocalName(0), reader.getLocalName()));
        }
    }

    private void verifyNoChildElements() throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    error("Unexpected element <%s>".formatted(reader.getLocalName()));
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText().trim();
                    if (!text.isEmpty()) {
                        error("Unexpected text \"%s\"".formatted(text));
                    }
                    break;
            }
        }
    }

    private void verifyAttributes(String ...names) {
        int count = reader.getAttributeCount();

        nextAttribute:
        for (int i=0; i < count; i++) {
            String name = reader.getAttributeLocalName(i);
            for (String n : names) {
                if (n.equals(name)) {
                    continue nextAttribute;
                }
            }

            error("Unexpected attribute \"%s\"".formatted(name));
        }
    }

    private String getAttribute(String name) {
        String value = reader.getAttributeValue(null, name);
        if (value == null) {
            error("Expected attribute \"%s\"".formatted(name));
        }
        return value;
    }

    private String getAttribute(String name, String defaultValue) {
        String value = reader.getAttributeValue(null, name);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private double getDoubleAttribute(String name) {
        String value = reader.getAttributeValue(null, name);
        if (value == null) {
            error("Expected attribute \"%s\"".formatted(name));
            return 0;
        } else {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                error("Invalid number " + value);
                return 0;
            }
        }
    }

    private boolean getBooleanAttribute(String name) {
        String value = reader.getAttributeValue(null, name);
        if (value == null) {
            error("Expected attribute \"%s\"".formatted(name));
        }
        return Boolean.parseBoolean(value);
    }

    private boolean getBooleanAttribute(String name, boolean defaultValue) {
        String value = reader.getAttributeValue(null, name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    private void parseChildren(TextElementProcessor textProcessor, Map<String,ElementProcessor> processors) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();
                    ElementProcessor processor = processors.get(name);
                    if (processor == null) {
                        error("Unexpected element <%s>".formatted(name));
                    } else {
                        processor.processElement();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    String text = reader.getText();
                    if (textProcessor != null) {
                        textProcessor.processText(text);
                    } else if (!reader.isWhiteSpace()) {
                        error("Unexpected text \"%s\"".formatted(text));
                    }
                    break;
            }
        }
    }

    private interface ElementProcessor {
        void processElement() throws XMLStreamException;
    }

    private interface TextElementProcessor {
        void processText(String text) throws XMLStreamException;
    }
}
