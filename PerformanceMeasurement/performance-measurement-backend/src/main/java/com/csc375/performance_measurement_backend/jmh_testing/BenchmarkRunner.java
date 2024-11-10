package com.csc375.performance_measurement_backend.jmh_testing;

import com.csc375.performance_measurement_backend.performance_measurement_workers.java_investment_banking.JavaInvestmentBanking;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.util.Multimap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BenchmarkRunner {
    public static void main(String[] args) throws IOException, RunnerException {

        Options options = new OptionsBuilder()
                .include("com.csc375.performance_measurement_backend.jmh_testing.JavaInvestmentBankingBenchmark.*")
                .include("com.csc375.performance_measurement_backend.jmh_testing.CustomInvestmentBankingBenchmark.*")
                .warmupIterations(1)  // 1 warmup iteration
                .warmupTime(TimeValue.seconds(1))  // 3 seconds warmup time
                .measurementIterations(3)
                .measurementTime(TimeValue.seconds(1))  // 2 seconds measurement time
                .forks(1)  // Forking 1 separate JVM instance
                .mode(Mode.Throughput)  // Measure throughput (operations per unit of time)
                .build();


        // Create the runner to run the benchmark
        Runner runner = new Runner(options);

        // Run the benchmark and capture results
        Collection<RunResult> runResults = runner.run(); // Capture the results


        runResults.forEach(runResult -> {
            System.out.println("------->>>>>");
            // Print primary result
            Result primaryResult = runResult.getPrimaryResult();
            System.out.println(primaryResult.getLabel());
            System.out.println(primaryResult.getRole());
            System.out.println("Primary Result: " + primaryResult);



            // Print aggregated result
            BenchmarkResult aggregatedResult = runResult.getAggregatedResult();
            System.out.println("Aggregated Result: " + aggregatedResult);

            // Print each benchmark result in the collection
            Collection<BenchmarkResult> benchmarkResults = runResult.getBenchmarkResults();
            System.out.println("Benchmark Results:");
            for (BenchmarkResult result : benchmarkResults) {
                printBenchmarkResult(result);
            }

            // Print each secondary result in the map
            Map<String, Result> secondaryResults = runResult.getSecondaryResults();
            System.out.println("Secondary Results:");
            for (Map.Entry<String, Result> entry : secondaryResults.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Result: " + entry.getValue());
            }
        });

    }
    private static void printBenchmarkResult(BenchmarkResult result) {
        if (result == null) {
            System.out.println("BenchmarkResult is null");
            return;
        }

        // Replace these with actual methods/properties from the BenchmarkResult class
        System.out.println("Benchmark Name: " + result.getBenchmarkResults()); // Example method
        System.out.println("Mean Score: " + result.getPrimaryResult().getScore());
        System.out.println("Score Error (Confidence Interval): " + result.getPrimaryResult().getScoreError());
        System.out.println("Score Unit: " + result.getPrimaryResult().getScoreUnit());

        // If there are other relevant fields or methods in BenchmarkResult, print them here
    }
}
