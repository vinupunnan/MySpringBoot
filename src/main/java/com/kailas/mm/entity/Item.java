package com.kailas.mm.entity;

import javax.persistence.*;

@Entity
@Table(name = "item_master")
public class Item {
    @Id
    @Column(name= "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer itemId;

    @Column(name ="item_code")
    private String itemCode;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name ="description")
     private String description;

}
