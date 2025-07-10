package org.sandium.syntaxy.backend2.providers.implementations;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;
import org.sandium.syntaxy.backend2.providers.Provider;
import org.sandium.syntaxy.backend2.providers.annotations.ProviderName;
import org.sandium.syntaxy.backend2.providers.annotations.ProviderSetting;

@ProviderName("aws.bedrockName")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Bedrock extends Provider {

    private String accessKeyId;
    private String secretAccessKey;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    @Override
    public void init() {
        // TODO Check for connection settings
        // TODO Download/validate models
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @ProviderSetting("aws.accessKeyId")
    @XmlTransient
    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }

    @ProviderSetting(value="aws.accessKeyId", passwordField = true)
    @XmlTransient
    public void setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
    }

}
