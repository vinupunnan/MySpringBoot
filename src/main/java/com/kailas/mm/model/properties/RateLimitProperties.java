package com.kailas.mm.model.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "myalerts.ratelimit")
public class RateLimitProperties {
    private Limit defaultLimit = new Limit();
    private Map<String, Limit> dcs = new HashMap<>();

    public Limit getDefaultLimit() {
        return defaultLimit;
    }

    public void setDefaultLimit(Limit defaultLimit) {
        this.defaultLimit = defaultLimit;
    }

    public Map<String, Limit> getDcs() {
        return dcs;
    }

    public void setDcs(Map<String, Limit> dcs) {
        this.dcs = dcs;
    }
}
