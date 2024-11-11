package com.csc375.performance_measurement_backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

public class IndividualBenchMark {

    // The field to store the benchmark results
    private HashMap<String, ResultFormatBenchMark> results;

    public IndividualBenchMark() {
        this.results = new HashMap<>();
    }

    public IndividualBenchMark(HashMap<String, ResultFormatBenchMark> results) {
        this.results = results;
    }

    public HashMap<String, ResultFormatBenchMark> getResults() {
        return results;
    }

    public void setResults(HashMap<String, ResultFormatBenchMark> results) {
        this.results = results;
    }
}
