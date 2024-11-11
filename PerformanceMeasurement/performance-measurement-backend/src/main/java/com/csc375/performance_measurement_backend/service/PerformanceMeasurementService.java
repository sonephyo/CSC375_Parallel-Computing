package com.csc375.performance_measurement_backend.service;


import com.csc375.performance_measurement_backend.models.IndividualBenchMark;
import com.csc375.performance_measurement_backend.models.ResultFormatBenchMark;
import lombok.AllArgsConstructor;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class PerformanceMeasurementService {

    public HashMap<String, IndividualBenchMark> startBenchmarkForPerformanceMeasurement() throws RunnerException {
        Options options = new OptionsBuilder()
                .include("com.csc375.performance_measurement_backend.jmh_testing.JavaInvestmentBankingBenchmark.*")
                .include("com.csc375.performance_measurement_backend.jmh_testing.CustomInvestmentBankingBenchmark.*")
                .warmupIterations(1)  // 1 warmup iteration
                .warmupTime(TimeValue.milliseconds(100))  // 3 seconds warmup time
                .measurementIterations(5)
                .measurementTime(TimeValue.milliseconds(100))  // 2 seconds measurement time
                .forks(1)  // Forking 1 separate JVM instance
                .mode(Mode.Throughput)  // Measure throughput (operations per unit of time)
                .build();


        // Create the runner to run the benchmark
        Runner runner = new Runner(options);

        // Run the benchmark and capture results
        Collection<RunResult> runResults = runner.run(); // Capture the results


        HashMap<String, IndividualBenchMark> individualBenchMarkHashMap = new HashMap<>();
        runResults.forEach(runResult -> {

            HashMap<String, ResultFormatBenchMark> resultFormatBenchMarkHashMap = new HashMap<>();

            Result primaryResult = runResult.getPrimaryResult();

            ResultFormatBenchMark r = new ResultFormatBenchMark(
                    primaryResult.getScore(),
                    primaryResult.getScoreError(),
                    primaryResult.getSampleCount(),
                    primaryResult.getScoreUnit(),
                    primaryResult.getStatistics().getMin(),
                    primaryResult.getStatistics().getMax(),
                    primaryResult.getStatistics().getStandardDeviation(),
                    primaryResult.getStatistics().getVariance()
            );

            resultFormatBenchMarkHashMap.put("primaryResult", r);

            // Print each secondary result in the map
            Map<String, Result> secondaryResults = runResult.getSecondaryResults();
            System.out.println("Secondary Results:");
            for (Map.Entry<String, Result> entry : secondaryResults.entrySet()) {
                System.out.println("Secondary Result: " + entry.getKey());
                System.out.println("Result: " + entry.getValue().getScore());

                ResultFormatBenchMark rs = new ResultFormatBenchMark(
                        secondaryResults.get(entry.getKey()).getScore(),
                        secondaryResults.get(entry.getKey()).getScoreError(),
                        secondaryResults.get(entry.getKey()).getSampleCount(),
                        secondaryResults.get(entry.getKey()).getScoreUnit(),
                        secondaryResults.get(entry.getKey()).getStatistics().getMin(),
                        secondaryResults.get(entry.getKey()).getStatistics().getMax(),
                        secondaryResults.get(entry.getKey()).getStatistics().getStandardDeviation(),
                        secondaryResults.get(entry.getKey()).getStatistics().getVariance()
                );
                resultFormatBenchMarkHashMap.put(entry.getKey(), rs);
            }
            System.out.println("resultFormatBenchMarkHashMap:" + resultFormatBenchMarkHashMap);

            IndividualBenchMark individualBenchMark = new IndividualBenchMark(resultFormatBenchMarkHashMap);
            System.out.println("Still executed");
            individualBenchMarkHashMap.put("test", individualBenchMark);
//


//            System.out.println("------->>>>>");
//            // Print primary result
//            System.out.println(primaryResult.getLabel());
//            System.out.println(primaryResult.getRole());
//            System.out.println("Primary Result: " + primaryResult);
//            // Printing score
//            System.out.println("Score: " + primaryResult.getScore());
//
//            // Printing confidence intervals for the score
//            System.out.println("Score Confidence Intervals: " + Arrays.toString(primaryResult.getScoreConfidence()));
//
//            // Printing error in the score
//            System.out.println("Score Error: " + primaryResult.getScoreError());
//            System.out.println("Sample count:  " + primaryResult.getSampleCount() );
//
//            // Printing score unit
//            System.out.println("Score Unit: " + primaryResult.getScoreUnit());
//
//            // Printing confidence interval at 50% level (e.g., 0.5)
//            System.out.println("Confidence Interval at 50%: " + Arrays.toString(primaryResult.getStatistics().getConfidenceIntervalAt(0.5)));
//
//            // Printing mean value of the score
//            System.out.println("Mean: " + primaryResult.getStatistics().getMean());
//
//            // Printing minimum value of the score
//            System.out.println("Minimum: " + primaryResult.getStatistics().getMin());
//
//            // Printing maximum value of the score
//            System.out.println("Maximum: " + primaryResult.getStatistics().getMax());
//
//            // Printing standard deviation of the score
//            System.out.println("Standard Deviation: " + primaryResult.getStatistics().getStandardDeviation());
//
//            // Printing variance of the score
//            System.out.println("Variance: " + primaryResult.getStatistics().getVariance());


            // Print aggregated result
//            BenchmarkResult aggregatedResult = runResult.getAggregatedResult();
//            System.out.println("Aggregated Result: " + aggregatedResult);

            // Print each benchmark result in the collection
//            Collection<BenchmarkResult> individualBenchMarkHashMap = runResult.getBenchmarkResults();
//            System.out.println("Benchmark Results:");
//            for (BenchmarkResult result : individualBenchMarkHashMap) {
//                printBenchmarkResult(result);
//            }

            // Print each secondary result in the map
//            Map<String, Result> secondaryResults = runResult.getSecondaryResults();
//            System.out.println("Secondary Results:");
//            for (Map.Entry<String, Result> entry : secondaryResults.entrySet()) {
//                System.out.println("Key: " + entry.getKey() + ", Result: " + entry.getValue());
//            }
        });

        return individualBenchMarkHashMap;

    }

    private static void printBenchmarkResult(BenchmarkResult result) {
        if (result == null) {
            System.out.println("BenchmarkResult is null");
            return;
        }
        System.out.println("Role: " + result.getPrimaryResult().getRole());
        // Replace these with actual methods/properties from the BenchmarkResult class
        System.out.println("Benchmark Name: " + result.getBenchmarkResults()); // Example method
        System.out.println("Mean Score: " + result.getPrimaryResult().getScore());
        System.out.println("Score Error (Confidence Interval): " + result.getPrimaryResult().getScoreError());
        System.out.println("Score Unit: " + result.getPrimaryResult().getScoreUnit());

        // If there are other relevant fields or methods in BenchmarkResult, print them here
    }

}
