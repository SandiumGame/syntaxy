package org.sandium.syntaxy.backend2.providers;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElements;
import jakarta.xml.bind.annotation.XmlRootElement;
import org.sandium.syntaxy.backend2.providers.implementations.Bedrock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "providers")
public class ProviderManager {

    private List<Provider> providers;

    public static ProviderManager load(InputStream inputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(ProviderManager.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        unmarshaller.setEventHandler(event -> {
            // TODO Better error message. Include source file path.
            // TODO Make this a utility class
//            System.out.println(event.getMessage());
//            System.out.println(event.getLocator().getColumnNumber() + " " + event.getLocator().getLineNumber());
//            throw new RuntimeException("Test XXXX");
            return true;
        });
        return (ProviderManager) unmarshaller.unmarshal(inputStream);
    }

    public ProviderManager() {
        providers = new ArrayList<>();
    }

    public void init() {
        for (Provider provider : providers) {
            provider.init();
        }
    }

    public List<Provider> getProviders() {
        return providers;
    }

    @XmlElements({
            @XmlElement(name="bedrock", type= Bedrock.class)
    })
    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

}
