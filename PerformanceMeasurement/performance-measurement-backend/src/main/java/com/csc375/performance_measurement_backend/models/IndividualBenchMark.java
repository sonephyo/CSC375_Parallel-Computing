package com.csc375.performance_measurement_backend.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;


@JsonSerialize // This annotation enables serialization for Jackson
@Getter
@Setter
public class IndividualBenchMark {

    // The field to store the benchmark results
    private HashMap<String, ResultFormatBenchMark> results;

    // Constructor annotated with @JsonCreator and @JsonProperty to tell Jackson how to deserialize
    @JsonCreator
    public IndividualBenchMark(
            @JsonProperty("results") HashMap<String, ResultFormatBenchMark> results) {
        this.results = results;
    }
}
