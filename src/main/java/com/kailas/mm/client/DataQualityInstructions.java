package com.kailas.mm.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class DataQualityInstructions {
    private String dataQualityRule;

    private String strategy;

    private String elemPath;

    public String getDataQualityRule() {
        return dataQualityRule;
    }

    public void setDataQualityRule(String dataQualityRule) {
        this.dataQualityRule = dataQualityRule;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getElemPath() {
        return elemPath;
    }

    public void setElemPath(String elemPath) {
        this.elemPath = elemPath;
    }

    public String getElemType() {
        return elemType;
    }

    public void setElemType(String elemType) {
        this.elemType = elemType;
    }

    private String elemType;
}