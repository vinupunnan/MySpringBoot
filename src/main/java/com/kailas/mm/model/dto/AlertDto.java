package com.kailas.mm.model.dto;

import java.util.List;
import java.util.Map;

public class AlertDto {
    private String dcId;
    List<Map<String, String>> data;
    private List<AlertItemDto> dtolist;

    public AlertDto() {
    }

    /**
     * Retrieves the data center identifier.
     *
     * @return the dcId representing the unique identifier for the data center
     */
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

    public List<AlertItemDto> getDtolist() {
        return dtolist;
    }

    public void setDtolist(List<AlertItemDto> dtolist) {
        this.dtolist = dtolist;
    }
}
