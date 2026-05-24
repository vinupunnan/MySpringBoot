package com.kailas.mm.model.dto;

public class AlertItemDto {

    private String itemCode;
    private String alertType;
    private String quantity;
    private String threshold;
    private String aisle;
    private String triggeredOn;

    public AlertItemDto() {
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getTriggeredOn() {
        return triggeredOn;
    }

    public void setTriggeredOn(String triggeredOn) {
        this.triggeredOn = triggeredOn;
    }
}
