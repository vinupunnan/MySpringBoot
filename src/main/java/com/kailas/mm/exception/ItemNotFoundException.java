package com.kailas.mm.exception;

import java.util.List;

public class ItemNotFoundException extends RuntimeException {


    private final String message;
    private List<String> itemIds;

    public List<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<String> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ItemNotFoundException(String message){
        this.message = message;
    }

    public ItemNotFoundException(String message, List<String> itemIds){
        super();
        this.message = message;
        this.itemIds = itemIds;
    }
}
