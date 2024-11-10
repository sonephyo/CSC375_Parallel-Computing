package com.csc375.performance_measurement_backend.controllers.performance_measurement;


import com.csc375.performance_measurement_backend.service.PerformanceMeasurementService;
import lombok.AllArgsConstructor;
import org.openjdk.jmh.runner.RunnerException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/pm")
public class PerformanceMeasurementController {

    private final PerformanceMeasurementService performanceMeasurementService;

    public PerformanceMeasurementController(PerformanceMeasurementService performanceMeasurementService) {
        this.performanceMeasurementService = performanceMeasurementService;
    }

    @GetMapping("/start")
    public String start() throws RunnerException {
        performanceMeasurementService.startBenchmarkForPerformanceMeasurement();
        return "Done";
    }
}