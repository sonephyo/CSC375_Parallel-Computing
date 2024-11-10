package com.csc375.performance_measurement_backend.jmh_testing;


import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@State(Scope.Benchmark)
public class SharedData {

    HashMap<String, Double> currentData;

    @Param({"10", "1000"})
    public int numOfStartData;

    @Setup(Level.Trial)
    public void setUp() {
        System.out.println("Setting up shared data for concurrentHashMap");
        setUpStartData(numOfStartData);
    }

    public void setUpStartData(int numOfStartData) {
        currentData = new HashMap<>();
        Random r = new Random();
        for (int i = 0; i < numOfStartData; i++) {
            currentData.put("User: " + i, r.nextDouble(50, 1000));
        }
    }


}
