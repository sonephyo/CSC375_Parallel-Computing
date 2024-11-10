package com.csc375.performance_measurement_backend.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BenchMarkResults {

    HashMap<String, BenchMarkTypes> benchmarkResults;
}
