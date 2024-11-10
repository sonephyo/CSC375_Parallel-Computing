package com.csc375.performance_measurement_backend.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
public class ResultFormatBenchMark {
    private double score;
    private double scoreError;
    private long sampleCount;
    private String scoreUnit;
    private double min;
    private double max;
    private double standardDeviation;
    private double variance;

    public ResultFormatBenchMark(double score, double scoreError, long sampleCount, String scoreUnit, double min, double max, double standardDeviation, double variance) {
        this.score = score;
        this.scoreError = scoreError;
        this.sampleCount = sampleCount;
        this.scoreUnit = scoreUnit;
        this.min = min;
        this.max = max;
        this.standardDeviation = standardDeviation;
        this.variance = variance;
    }
}
