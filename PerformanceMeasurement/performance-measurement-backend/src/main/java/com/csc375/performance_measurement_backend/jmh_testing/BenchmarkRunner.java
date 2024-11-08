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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

public class BenchmarkRunner {
    public static void main(String[] args) throws IOException, RunnerException {

        Options options = new OptionsBuilder()
                .include("com.csc375.performance_measurement_backend.jmh_testing.JavaInvestmentBankingBenchmark.*")
                .warmupIterations(1)  // 1 warmup iteration
                .warmupTime(TimeValue.seconds(1))  // 3 seconds warmup time
                .measurementIterations(10)  // 3 measurement iterations
                .measurementTime(TimeValue.seconds(1))  // 2 seconds measurement time
                .forks(1)  // Forking 1 separate JVM instance
                .mode(Mode.Throughput)  // Measure throughput (operations per unit of time)
                .build();


        // Create the runner to run the benchmark
        Runner runner = new Runner(options);

        // Run the benchmark and capture results
        Collection<RunResult> runResults = runner.run(); // Capture the results

        runResults.forEach(runResult -> {
            BenchmarkResult aggregatedResult = runResult.getAggregatedResult();
            Result primaryResult = runResult.getPrimaryResult();
            System.out.println("test results");
            System.out.println(primaryResult.getSampleCount());
            System.out.println(primaryResult.getRole());
            System.out.println(primaryResult.getLabel());
            System.out.println(primaryResult.getScoreError());
            System.out.println(Arrays.toString(primaryResult.getScoreConfidence()));
            System.out.println(primaryResult.toString());
        });
    }
}
