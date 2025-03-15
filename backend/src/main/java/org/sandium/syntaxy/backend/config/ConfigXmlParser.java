package org.sandium.syntaxy.backend.config;

import javax.xml.stream.Location;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class ConfigXmlParser {

    private final InputStream xml;
    private final Config config;
    private XMLStreamReader reader;

    public ConfigXmlParser(InputStream xml, Config config) {
        this.xml = xml;
        this.config = config;
    }

    public void parse() {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLStreamReader(xml);

            // Read <config>
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (!"config".equals(reader.getLocalName())) {
                            error("Expected <config>");
                        }

                        parseConfigElement();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (!"config".equals(reader.getLocalName())) {
                            error("Expected </config>");
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        verifyNoText();
                        break;
                }
            }
        } catch (Exception e) {
            // TODO Need to handle errors
            e.printStackTrace();
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

    private void error(String error) {
        Location location = reader.getLocation();
        throw new RuntimeException(error + " at line %s column %s.".formatted(location.getLineNumber(), location.getColumnNumber()));
    }

    private void verifyNoAttributes() {
        if (reader.getAttributeCount() > 0) {
            error("Unexpected attribute %s in <%s>".formatted(reader.getAttributeLocalName(0), reader.getLocalName()));
        }
    }

    private void verifyCloseTagNext() throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    error("Unexpected element <%s>".formatted(reader.getLocalName()));
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    verifyNoText();
                    break;
            }
        }
    }

    private void verifyNoText() {
        String text = reader.getText().trim();
        if (!text.isEmpty()) {
            error("Unexpected text \"%s\"".formatted(text));
        }
    }

    private void parseConfigElement() throws XMLStreamException {
        verifyNoAttributes();
        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();
                    switch (name) {
                        case "models":
                            parseModelsElement();
                            break;
                        case "agent":
                            parseAgentElement();
                            break;
                        default:
                            error("Unexpected element <%s>".formatted(name));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    verifyNoText();
                    break;
            }
        }
    }

    private void parseModelsElement() throws XMLStreamException {
        verifyNoAttributes();

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();
                    switch (name) {
                        case "model":
                            parseModelElement();
                            break;
                        case "modelAlias":
                            parseModelAliasElement();
                            break;
                        default:
                            error("Unexpected element <%s>".formatted(name));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    verifyNoText();
                    break;
            }
        }
    }

    private void parseModelElement() throws XMLStreamException {
        // TODO Read attributes

        verifyCloseTagNext();
    }

    private void parseModelAliasElement() throws XMLStreamException {
        // TODO Read attributes

        verifyCloseTagNext();
    }

    private void parseAgentElement() throws XMLStreamException {
        // TODO Read attributes

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();
                    switch (name) {
                        case "system":
                            parsePrompt(true);
                            break;
                        case "prompt":
                            parsePrompt(false);
                            break;
                        default:
                            error("Unexpected element <%s>".formatted(name));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    verifyNoText();
                    break;
            }
        }
    }

    private void parsePrompt(boolean systemPrompt) throws XMLStreamException {
        // TODO Read attributes

        while (reader.hasNext()) {
            int event = reader.next();
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    String name = reader.getLocalName();
                    switch (name) {
                        case "include":
                            parseInclude();
                            break;
                        case "userPrompt":
                            parseUserPrompt();
                            break;
                        default:
                            error("Unexpected element <%s>".formatted(name));
                            break;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return;
                case XMLStreamConstants.CHARACTERS:
                    // TODO Parse text
                    break;
            }
        }
    }

    private void parseInclude() throws XMLStreamException {
        // TODO Read attributes

        verifyCloseTagNext();
    }

    private void parseUserPrompt() throws XMLStreamException {
        // TODO Read attributes

        verifyCloseTagNext();
    }

}
