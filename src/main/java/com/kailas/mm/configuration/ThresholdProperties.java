package com.kailas.mm.configuration;

import com.kailas.mm.model.properties.ThresholdValue;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "lab")
public class ThresholdProperties {
    public Map<String, ThresholdValue> thresholds;

    public Map<String, ThresholdValue> getThresholds() {
        return thresholds;
    }

    public void setThresholds(Map<String, ThresholdValue> thresholds) {
        this.thresholds = thresholds;
    }
}
