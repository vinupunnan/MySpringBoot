package com.kailas.mm.model.entity.nonsql;

import org.springframework.data.mongodb.core.mapping.Document;


@Document("collection= item")
public class Itemmongo {
    private Integer itemId;
    private String itemCode;
    private String description;
    private Short ficRequiredYesNo;
    private Short classifiedYesNo;
    private Integer pricingPolicy;

}
