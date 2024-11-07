# JMH Benchmarking Guide

## Key Annotations

- **@Benchmark**: Marks the method to be benchmarked.
- **@BenchmarkMode**: Defines the mode of benchmarking (e.g., `Mode.AverageTime`, `Mode.Throughput`).
- **@OutputTimeUnit**: Specifies the time unit for results (e.g., `TimeUnit.MILLISECONDS`).
- **@Warmup**: Defines warmup iterations before measurements.
- **@Measurement**: Defines measurement iterations.
- **@Fork**: Specifies the number of JVM forks (default is 1).
- **@State**: Specifies how the benchmark state is shared across threads (e.g., `Scope.Thread` or `Scope.Benchmark`).


# Using in options instead of the annotations
```java
Options options = new OptionsBuilder()
        .include(".*CalculatorBenchmark.*") // Specify which benchmarks to include
        .warmupIterations(1)  // 1 warmup iteration
        .warmupTime(TimeValue.seconds(1))  // 3 seconds warmup time
        .measurementIterations(10)  // 3 measurement iterations
        .measurementTime(TimeValue.seconds(1))  // 2 seconds measurement time
        .forks(1)  // Forking 1 separate JVM instance
        .mode(Mode.Throughput)  // Measure throughput (operations per unit of time)
        .build();
```

include - the code files/sources that you want to benchmark (format in regex)
warmupIterations - the number of warm up iterations
warmupTime - time for each warm up iteration
measurementIterations - the number of each real iterations
measurementTime - time for each real iteration
fork - testing each on different JVM to ensure reliability

## Example: Single Benchmark

```java
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class CalculatorBenchmark {

    private Calculator calculator = new Calculator();

    @Benchmark
    public int benchmarkAdd() {
        return calculator.add(1, 2);
    }

    static class Calculator {
        public int add(int a, int b) {
            return a + b;
        }
    }
}
