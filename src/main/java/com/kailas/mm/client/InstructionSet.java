package com.kailas.mm.client;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;
import java.util.List;

public class InstructionSet {



       @JsonIgnore
        private OffsetDateTime createdDate;

        @JsonIgnore
        private OffsetDateTime endDate;


        private String createdBy;


        private String modifiedBy;


        private List<DataQualityInstructions> dataQualityInstructions = null;







    }



