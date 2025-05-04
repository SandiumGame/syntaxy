package org.sandium.syntaxy.backend2.config;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.InputStream;

public class ConfigXmlParser {

    public static void main(String[] args) throws JAXBException {
        ConfigXmlParser parser = new ConfigXmlParser();
        Config config = parser.parse(ConfigXmlParser.class.getResourceAsStream("/config2.xml"));
        System.out.println("Size: "+config.getProviders().size());
    }

    public Config parse(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Config.class);
        return (Config) context.createUnmarshaller().unmarshal(inputStream);
    }
}
