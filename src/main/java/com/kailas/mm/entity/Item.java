package com.kailas.mm.entity;



import javax.persistence.*;

@Entity
@Table(name = "item_master")

public class Item {
    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer itemId;

    @Column(name ="item_code")//Item code
    private String itemCode;

    @Column(name ="description")
    private String description;


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


}
