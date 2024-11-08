package com.csc375.performance_measurement_backend.jmh_testing;


import org.openjdk.jmh.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

@State(Scope.Benchmark)
public class SharedData {

    HashMap<String, Double> currentData;
//    ArrayList<String> incomingReadData;
//    HashMap<String, Double> incomingWriteData;

    @Param({"10", "20"})
    public int numOfStartData;

//    @Param({"10", "20"})
//    public int numOfReadData;
//
//    @Param({"10", "20"})
//    public int numOfWriteData;

    @Setup(Level.Trial)
    public void setUp() {
        System.out.println("Setting up shared data for concurrentHashMap");
        setUpStartData(numOfStartData);
//        setUpReadData(numOfReadData);
//        setUpWriteData(numOfWriteData);
    }

    public void setUpStartData(int numOfStartData) {
        currentData = new HashMap<>();
        Random r = new Random();
        for (int i = 0; i < numOfStartData; i++) {
            currentData.put("User: " + i, r.nextDouble(50, 1000));
        }
    }

//    public void setUpReadData(int numOfReadData) {
//        incomingReadData = new ArrayList<>();
//        Random r = new Random();
//        for (int i = 0; i < numOfReadData; i++) {
//            incomingReadData.add("User: " + r.nextInt(0, currentData.size() - 1));
//        }
//    }
//
//    public void setUpWriteData(int numOfWriteData) {
//        incomingWriteData = new HashMap<>();
//        Random r = new Random();
//        for (int i = 0; i < numOfWriteData; i++) {
//            // Putting data that is half new and half old
//            incomingWriteData.put("User: " + r.nextInt(0, currentData.size() * 2), r.nextDouble(50, 1000));
//        }

//    }








}
