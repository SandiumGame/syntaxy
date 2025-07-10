package org.sandium.syntaxy.backend2.providers;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;

public class ProviderManagerTest {

    @Test
    public void loadProviders() throws JAXBException {
        ProviderManager providerManager = ProviderManager.load(this.getClass().getResourceAsStream("providers-test.xml"));
        providerManager.init();

        // TODO
        // TODO Step through all providers and all models and see if they work?
    }
}
