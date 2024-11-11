package com.csc375.performance_measurement_backend.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScoreError() {
        return scoreError;
    }

    public void setScoreError(double scoreError) {
        this.scoreError = scoreError;
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public String getScoreUnit() {
        return scoreUnit;
    }

    public void setScoreUnit(String scoreUnit) {
        this.scoreUnit = scoreUnit;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }
}
