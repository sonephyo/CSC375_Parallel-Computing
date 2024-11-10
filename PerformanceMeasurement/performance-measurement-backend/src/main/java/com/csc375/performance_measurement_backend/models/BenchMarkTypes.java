package com.csc375.performance_measurement_backend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BenchMarkTypes {
    HashMap<String, IndividualBenchMark> benchMarkTypesToIndividualBenchMarkHM;
}
