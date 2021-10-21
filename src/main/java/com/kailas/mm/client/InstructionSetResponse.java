package com.kailas.mm.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InstructionSetResponse {
        public List<Input> getInput() {
                return input;
        }

        public void setInput(List<Input> input) {
                this.input = input;
        }

        public List<ResultEntry> getResultEntry() {
                return resultEntry;
        }

        public void setResultEntry(List<ResultEntry> resultEntry) {
                this.resultEntry = resultEntry;
        }

        @JsonProperty("Input")
        private List<Input> input = new ArrayList<>();
        @JsonProperty("ResultEntry")
        private List<ResultEntry> resultEntry = new ArrayList<>();


}
