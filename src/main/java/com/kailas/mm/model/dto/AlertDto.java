package com.kailas.mm.model.dto;

import java.util.List;
import java.util.Map;

public class AlertDto {
    private String dcId;
    List<Map<String,String>> data;

    public AlertDto() {
    }

    public String getDcId() {
        return dcId;
    }

    public void setDcId(String dcId) {
        this.dcId = dcId;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
