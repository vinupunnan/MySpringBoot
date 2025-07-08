package com.kailas.mm.model.entity.nonsql;

import com.kailas.mm.model.dto.ItemDto;
import org.springframework.data.mongodb.core.mapping.Document;


@Document("collection= item")
public class ItemDocument {
    private Integer itemId;
    private String itemCode;
    private String description;
    private Short ficRequiredYesNo;
    private Short classifiedYesNo;
    private Integer pricingPolicy;

    public ItemDocument(Integer itemId, String itemCode, String description, Short ficRequiredYesNo, Short classifiedYesNo, Integer pricingPolicy) {
        this.itemId = itemId;
        this.itemCode = itemCode;
        this.description = description;
        this.ficRequiredYesNo = ficRequiredYesNo;
        this.classifiedYesNo = classifiedYesNo;
        this.pricingPolicy = pricingPolicy;
    }

    public ItemDocument(ItemDto itemDto) {
        this.itemCode = itemDto.getItemCode();
        this.description =itemDto.getItemDescription();
        this.pricingPolicy=itemDto.getPricingPolicy();
        this.classifiedYesNo =itemDto.getClassifiedYesNo();

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

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Short getFicRequiredYesNo() {
        return ficRequiredYesNo;
    }

    public void setFicRequiredYesNo(Short ficRequiredYesNo) {
        this.ficRequiredYesNo = ficRequiredYesNo;
    }

    public Short getClassifiedYesNo() {
        return classifiedYesNo;
    }

    public void setClassifiedYesNo(Short classifiedYesNo) {
        this.classifiedYesNo = classifiedYesNo;
    }

    public Integer getPricingPolicy() {
        return pricingPolicy;
    }

    public void setPricingPolicy(Integer pricingPolicy) {
        this.pricingPolicy = pricingPolicy;
    }
}
