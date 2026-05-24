package com.kailas.mm.model.properties;

public class ThresholdValue {
    private double criticalLow;
    private double urgentLow;
    private double urgentHigh;
    private double criticalHigh;
    private String unit;
    private String description;

    public double getCriticalLow() {
        return criticalLow;
    }

    public void setCriticalLow(double criticalLow) {
        this.criticalLow = criticalLow;
    }

    public double getUrgentLow() {
        return urgentLow;
    }

    public void setUrgentLow(double urgentLow) {
        this.urgentLow = urgentLow;
    }

    public double getUrgentHigh() {
        return urgentHigh;
    }

    public void setUrgentHigh(double urgentHigh) {
        this.urgentHigh = urgentHigh;
    }

    public double getCriticalHigh() {
        return criticalHigh;
    }

    public void setCriticalHigh(double criticalHigh) {
        this.criticalHigh = criticalHigh;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
