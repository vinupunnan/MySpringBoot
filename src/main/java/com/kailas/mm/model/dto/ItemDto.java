package com.kailas.mm.model.dto;

import com.kailas.mm.entity.Item;

import java.io.Serializable;

public class ItemDto implements Serializable {
    private int itemId;
    private String itemCode;
    private String itemDescription;

    public ItemDto() {
        super();
    }

    public ItemDto(Item item){
        this.itemId = item.getItemId();
        this.itemCode =item.getItemCode();
        this.itemDescription =item.getDescription();

    }


    public ItemDto(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
}
