package com.kailas.mm.entity;


import com.kailas.mm.model.dto.ItemDto;
import jakarta.persistence.*;

@Entity
@Table(name = "item_master")

public class Item {
    @Id
    @Column(name= "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(name ="item_code")//Item code
    private String itemCode;

    @Column(name ="description")
    private String description;

    @Column(name="fic_required_yesno")
    private Short ficRequiredYesNo;


    @Column(name ="classified_yesno")
    private Short classifiedYesNo;

     @Column(name = "pricing_policy")
     private Integer pricingPolicy;

    public Item() {
    }

    public Item(ItemDto itemDto) {
        this.itemCode = itemDto.getItemCode();
        this.description =itemDto.getItemDescription();
        this.pricingPolicy=itemDto.getPricingPolicy();
        this.classifiedYesNo =itemDto.getClassifiedYesNo();

    }




    public Short getFicRequiredYesNo() {
        return ficRequiredYesNo;
    }

    public void setFicRequiredYesNo(Short ficRequiredYesNo) {
        this.ficRequiredYesNo = ficRequiredYesNo;
    }

    public Integer getPricingPolicy() {
        return pricingPolicy;
    }

    public void setPricingPolicy(Integer pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemCode() {
        return itemCode;
    }

    public Short getClassifiedYesNo() {
        return classifiedYesNo;
    }

    public void setClassifiedYesNo(Short classifiedYesNo) {
        this.classifiedYesNo = classifiedYesNo;
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
