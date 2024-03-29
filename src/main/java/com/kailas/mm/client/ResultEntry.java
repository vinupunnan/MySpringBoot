package com.kailas.mm.client;

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (5.2.1-SNAPSHOT).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * ResultEntry
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ResultEntry   {
    @JsonProperty("fhirResource")
    private String fhirResource;

    @JsonProperty("payerName")
    private String payerName;

    @JsonProperty("profileName")
    private String profileName;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("InstructionSet")

    private List<InstructionSet> instructionSet = null;


}

