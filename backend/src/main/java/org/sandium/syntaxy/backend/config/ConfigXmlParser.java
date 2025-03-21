package org.sandium.syntaxy.backend.config;

import org.sandium.syntaxy.backend.config.agents.Agent;
import org.sandium.syntaxy.backend.config.agents.RouterAgent;

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
    private String currentProvider;

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

        currentProvider = getAttribute("provider");

        parseChildren(null, Map.of(
                "model", this::parseModelElement,
                "modelAlias", this::parseModelAliasElement));

        currentProvider = null;
    }

    private void parseModelElement() throws XMLStreamException {
        // TODO Read attributes

        verifyNoChildElements();
    }

    private void parseModelAliasElement() throws XMLStreamException {
        // TODO Read attributes

        verifyNoChildElements();
    }

    private void parseAgentElement() throws XMLStreamException {
        verifyAttributes("id", "type", "model", "title", "description");
        String type = getAttribute("type");
        Agent agent;
        switch (type) {
            case "router":
                agent = new RouterAgent();
                break;
            default:
                error("Invalid agent type \"%s\"".formatted(type));
                return;
        }

        agent.setId(getAttribute("id"));
        agent.setModel(getAttribute("model"));
        agent.setTitle(getAttribute("title", null));
        agent.setDescription(getAttribute("description", null));

        parseChildren(null, Map.of(
                "system", () -> parsePrompt(true),
                "prompt", () -> parsePrompt(false)));

        config.addAgent(agent);
    }

    private void parsePrompt(boolean systemPrompt) throws XMLStreamException {
        // TODO Read attributes

        parseChildren(text -> {
                // Remove whitespace from start of lines
                System.out.println(reader.getTextStart() + " " + text);
            }, Map.of(
                "routes", this::parseRoutes,
                "userPrompt", this::parseUserPrompt));
    }

    private void parseRoutes() throws XMLStreamException {
        // TODO Read attributes

        verifyNoChildElements();
    }

    private void parseUserPrompt() throws XMLStreamException {
        // TODO Read attributes

        verifyNoChildElements();
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
