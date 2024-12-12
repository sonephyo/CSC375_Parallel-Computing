package com.csc375.heat_propagation_backend.metalAlloy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MetalAlloyPartition implements Serializable {

    private double[][] metalAlloyTemps;
    private MetalAlloySegment[][] metalAlloySegments;
    private HashMap<String, Double> thermalConstants;

    public MetalAlloyPartition(double[][] metalAlloyTemps, MetalAlloySegment[][] metalAlloySegments, HashMap<String, Double> thermalConstants) {
        this.metalAlloyTemps = metalAlloyTemps;
        this.metalAlloySegments = metalAlloySegments;
        this.thermalConstants = thermalConstants;
    }

    public double[][] getMetalAlloyTemps() {
        return metalAlloyTemps;
    }

    public void setMetalAlloyTemps(double[][] metalAlloyTemps) {
        this.metalAlloyTemps = metalAlloyTemps;
    }

    public MetalAlloySegment[][] getMetalAlloySegments() {
        return metalAlloySegments;
    }

    public void setMetalAlloySegments(MetalAlloySegment[][] metalAlloySegments) {
        this.metalAlloySegments = metalAlloySegments;
    }

    public HashMap<String, Double> getThermalConstants() {
        return thermalConstants;
    }

    public void setThermalConstants(HashMap<String, Double> thermalConstants) {
        this.thermalConstants = thermalConstants;
    }

    public double[][] doPartitionOperation() throws ExecutionException, InterruptedException {
        double[][] resultingArray = new double[metalAlloySegments.length][metalAlloySegments[0].length];

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < resultingArray.length; i++) {
            final int row = i;
            int finalI = i;
            futures.add(executor.submit(() -> {
                for (int j = 0; j < resultingArray[0].length; j++) {
                    resultingArray[row][j] = calculateTemperatureAtRegion(finalI, j);
                }
                return null;
            }));
        }

        // Wait for all tasks to finish
        for (Future<Void> future : futures) {
            future.get();  // Wait for the task to complete
        }

        executor.shutdown();

        return resultingArray;
    }

    public double calculateTemperatureAtRegion(int x, int y) {
        double result = 0;
        int[][] coordinates = {
                {x, y+1},
                {x, y-1},
                {x-1, y},
                {x+1, y},
        };
        // Condition for checking the edges
        coordinates = Arrays.stream(coordinates).filter(coord -> !(coord[0] < 0 || coord[1] < 0 || coord[0] >= metalAlloySegments.length || coord[1] >= metalAlloySegments[0].length)).toArray(int[][]::new);

        for (String metal: thermalConstants.keySet()) {
            double individualMetalTempTotal = 0;
            for (int[] coord: coordinates) {
                double metalPercent = metalAlloySegments[coord[0]][coord[1]].getMetalComposition().get(metal + "percent");
                individualMetalTempTotal += metalAlloyTemps[coord[0]][coord[1]] * metalPercent;
            }

            result += thermalConstants.get(metal) * individualMetalTempTotal;
        }


        return result/coordinates.length;
    }
}
