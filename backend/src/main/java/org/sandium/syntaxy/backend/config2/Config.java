package org.sandium.syntaxy.backend.config2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement(name = "config")
public class Config {

//    private final Map<String, Agent> agents;
//    private final Map<String, Provider> providers;

    private List<Provider> providers = new ArrayList<Provider>();

    public Config() {
//        agents = new HashMap<>();
//        providers = new HashMap<>();
//        providers = new ArrayList<>();
    }

    public List<Provider> getProviders() {
        return providers;
    }

    @XmlElement(name = "provider", type = Provider.class)
    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

//
//    public Config(List<Provider> providers) {
//        this.providers = providers;
//    }
//
//    public List<Provider> getProviders() {
//        return new ArrayList<>();
//    }
//
//    public void setProviders(List<Provider> providers) {
//        this.providers = providers;
//    }

}
