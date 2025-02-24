package org.sandium.syntaxy.backend.config;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class ConfigXmlParser {

    private final InputStream xml;
    private final Config config;

    public ConfigXmlParser(InputStream xml, Config config) {
        this.xml = xml;
        this.config = config;
    }

    public void parse() {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(xml);

            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        System.out.println("Start Element: " + reader.getLocalName());
                        reader.getAttributeValue(null, "");
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        System.out.println("End Element: " + reader.getLocalName());
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (reader.hasText()) {
                            System.out.println("Characters: " + reader.getText().trim());
                        }
                        break;
                }
            }
        } catch (Exception e) {
            // TODO Need to handle errors
            e.printStackTrace();
        }
    }

    private void parseModels() {

    }
}
