package com.csc375.performance_measurement_backend.testing_tbd;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@State(Scope.Thread)
public class CalculatorBenchmark {

    private Calculator calculator;

    @Setup(Level.Trial)
    public void setUp() {
        System.out.println("setting up calculator");
        calculator = new Calculator();  // Set up the object instance for the benchmark
    }

    @Benchmark
    public int benchmarkAdd() {
        return calculator.add(10, 5);  // Benchmark the add method
    }

    @Benchmark
    public int benchmarkMultiply() {
        return calculator.multiply(10, 5);  // Benchmark the multiply method
    }

    @Benchmark
    public int benchmarkSubtract() {
        return calculator.subtract(10, 5);  // Benchmark the subtract method
    }

    @Benchmark
    public int benchmarkDivide() {
        return calculator.divide(10, 5);  // Benchmark the divide method
    }
}
