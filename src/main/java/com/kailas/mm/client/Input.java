package com.kailas.mm.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Input {


        @JsonProperty("payerName")
        private String payerName;

        public String getPayerName() {
                return payerName;
        }

        public void setPayerName(String payerName) {
                this.payerName = payerName;
        }

        public String getFhirResource() {
                return fhirResource;
        }

        public void setFhirResource(String fhirResource) {
                this.fhirResource = fhirResource;
        }

        public String getProfileName() {
                return profileName;
        }

        public void setProfileName(String profileName) {
                this.profileName = profileName;
        }

        public Boolean getIncludeHistory() {
                return includeHistory;
        }

        public void setIncludeHistory(Boolean includeHistory) {
                this.includeHistory = includeHistory;
        }

        @JsonProperty("fhirResource")
        private String fhirResource;

        @JsonProperty("profileName")
        private String profileName;

        @JsonProperty("includeHistory")
        private Boolean includeHistory;

     }
