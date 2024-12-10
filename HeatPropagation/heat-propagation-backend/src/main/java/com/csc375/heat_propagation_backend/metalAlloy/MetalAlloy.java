package com.csc375.heat_propagation_backend.metalAlloy;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MetalAlloy implements Serializable {

    private double[][] metalAlloyTemps;
    private MetalAlloySegment[][] metalAlloySegments;
    private HashMap<String, Double> thermalConstants;
    private double topLeftHeat;
    private double bottomRightHeat;


    public MetalAlloy(int numOfRows, double topLeftHeat, double bottomRightHeat, double metal1ThermalConstant, double metal2ThermalConstant, double metal3ThermalConstant) throws Exception {
        this.thermalConstants = new HashMap<>();
        thermalConstants.put("metal1", metal1ThermalConstant);
        thermalConstants.put("metal2", metal2ThermalConstant);
        thermalConstants.put("metal3", metal3ThermalConstant);
        this.topLeftHeat = topLeftHeat;
        this.bottomRightHeat = bottomRightHeat;

        int row = numOfRows;
        int col = row * 4;


        metalAlloyTemps = new double[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                metalAlloyTemps[i][j] = 0;
            }
        }

        metalAlloySegments = new MetalAlloySegment[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                HashMap<String, Double> randomPercentsHM = getRandomPercents();
                metalAlloySegments[i][j] = new MetalAlloySegment(randomPercentsHM.get("metal1"), randomPercentsHM.get("metal2"), randomPercentsHM.get("metal3"));
//                metalAlloySegments[i][j] = new MetalAlloySegment(0.34,0.33,0.33);
            }
        }

        for (MetalAlloySegment[] metalAlloySegment : metalAlloySegments) {
            for (MetalAlloySegment metalAlloySeg : metalAlloySegment) {
                for (Double value: metalAlloySeg.getMetalComposition().values()) {
                    if (value > 0.43 || value < 0.23) {
                        throw new Exception("Value large");
                    }
                }
            }
        }

        metalAlloyTemps[0][0] = this.topLeftHeat;
        metalAlloyTemps[metalAlloyTemps.length-1][metalAlloySegments[metalAlloyTemps.length-1].length-1] = this.bottomRightHeat;

    }

    public HashMap<String, Double> getRandomPercents() {
        Random random = new Random();

        double min = 0.23;
        double max = 0.43;
        double metal1Percent = min + (max - min) * random.nextDouble();
        double metal2Percent = min + (max - min) * random.nextDouble();
        double metal3Percent = 1 - metal1Percent - metal2Percent;

        if (metal3Percent < 0.23 || metal3Percent > 0.43) { return getRandomPercents();}

        HashMap<String, Double> percents = new HashMap<>();
        percents.put("metal1", metal1Percent);
        percents.put("metal2", metal2Percent);
        percents.put("metal3", metal3Percent);
        return percents;
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



    public double[][] doOperationByRange(int startCol, int endCol) throws Exception {
        if (startCol < 0 || endCol > this.metalAlloySegments[0].length) throw new IndexOutOfBoundsException("The startCol or the endCol should be between " + 0 + " and " + this.metalAlloySegments[0].length);
        double[][] resultingArray = new double[metalAlloySegments.length][endCol-startCol];

        if (startCol == 0) resultingArray[0][0] = this.topLeftHeat;
        if (endCol == this.metalAlloySegments[0].length) {
            resultingArray[resultingArray.length-1][resultingArray[0].length-1] = this.bottomRightHeat;
        }

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < resultingArray.length; i++) {
            final int row = i;
            futures.add(executor.submit(() -> {
                for (int j = 0; j < resultingArray[0].length; j++) {
                    if ((row == 0 && j == 0 && startCol == 0) || (row == resultingArray.length - 1 && j == resultingArray[0].length - 1 && endCol == metalAlloySegments[0].length))
                        continue;
                    resultingArray[row][j] = calculateTemperatureAtRegion(row, startCol + j);
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


    public double[][] doOperation() {

        double[][] resultingArray = new double[metalAlloySegments.length][metalAlloySegments[0].length];
        resultingArray[0][0] = this.topLeftHeat;
        resultingArray[metalAlloySegments.length-1][metalAlloySegments[0].length-1] = this.bottomRightHeat;
        for (int i = 0; i < metalAlloySegments.length; i++) {
            for (int j = 0; j < metalAlloySegments[0].length ; j++) {
                if ((i == 0 && j == 0) || (i == metalAlloySegments.length - 1 && j == metalAlloySegments[0].length - 1))
                    continue;
                resultingArray[i][j] = calculateTemperatureAtRegion(i, j);
            }
        }
        return resultingArray;
    }

    public double[][] getMetalAlloyTemps() {
        return metalAlloyTemps;
    }

    public void setMetalAlloyTemps(double[][] metalAlloyTemps) {
        this.metalAlloyTemps = metalAlloyTemps;
    }

    public MetalAlloy deepCopy() throws IOException, ClassNotFoundException {
        // Serialize the current object to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(this);
        objectOutputStream.flush();

        // Deserialize the byte array back into a new object (deep copy)
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return (MetalAlloy) objectInputStream.readObject(); // Return the deep-copied object
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        MetalAlloy metalTest = new MetalAlloy(100, 100, 0.75, 1.0,1.25);
//        MetalAlloy copy = metalTest.deepCopy();
//
//        System.out.println(metalTest);
//        System.out.println(copy);


//        double[][] prev = null;
//
//        for (int i = 0; i < 100000; i++) {
////            System.out.println("--------");
//            long startTime = System.nanoTime();
//            metalTest.doOperation();
//            long endTime = System.nanoTime();
//
//            // Calculate the time taken for execution
//            long duration = endTime - startTime;
//
//            // Convert to milliseconds for readability
//            if (i%1== 0) System.out.println("iteration: " + i);
//
//            if (i % 50 == 0) {
//                System.out.println("Execution time: " + duration/1_000_000_000.0 + " seconds");
//
//                System.out.println("=========================");
//
//                if (prev != null) {
//                    System.out.println("Prev and current arrays are equal: " + Arrays.deepEquals(prev, metalTest.metalAlloyTemps));
//                }
//
//                for (double[] doubles : metalTest.metalAlloyTemps) {
//                    for (double value : doubles) {
//                        System.out.printf("%8f ", value); // Aligns values with two decimal places
//                    }
//                    System.out.println(); // Moves to the next line after printing each row
//                }
//
//                prev = metalTest.metalAlloyTemps;
//
//                System.out.println("=========================");
//            }
//        }
//        for (double[] doubles : metalTest.metalAlloyTemps) {
//            for (double value : doubles) {
//                System.out.printf("%8.2f ", value); // Aligns values with two decimal places
//            }
//            System.out.println(); // Moves to the next line after printing each row
//        }
    }

    @Override
    public String toString() {
        return "MetalAlloy{" +
                "thermalConstants=" + thermalConstants +
                ", topLeftHeat=" + topLeftHeat +
                ", bottomRightHeat=" + bottomRightHeat +
                '}';
    }
}
