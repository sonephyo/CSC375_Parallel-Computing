import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;
import java.util.HashMap;

public class MetalAlloyJMH {

    private double[][] metalAlloyTemps;
    private MetalAlloySegment[][] metalAlloySegments;
    private HashMap<String, Double> thermalConstants;
    private double topLeftHeat;
    private double bottomRightHeat;
    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_MAX;


    public MetalAlloyJMH(double topLeftHeat, double bottomRightHeat, double metal1ThermalConstant, double metal2ThermalConstant, double metal3ThermalConstant) {
        this.thermalConstants = new HashMap<>();
        thermalConstants.put("metal1", metal1ThermalConstant);
        thermalConstants.put("metal2", metal2ThermalConstant);
        thermalConstants.put("metal3", metal3ThermalConstant);
        this.topLeftHeat = topLeftHeat;
        this.bottomRightHeat = bottomRightHeat;

        int row = 1024;
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
                metalAlloySegments[i][j] = new MetalAlloySegment(0.33,0.33, 0.33);
            }
        }

        metalAlloyTemps[0][0] = this.topLeftHeat;
        metalAlloyTemps[metalAlloyTemps.length-1][metalAlloySegments[metalAlloyTemps.length-1].length-1] = this.bottomRightHeat;

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

        double count = 0;
        for (int[] coord: coordinates) {
            if (coord[0] < 0 || coord[1] < 0 || coord[0] >= metalAlloySegments.length || coord[1] >= metalAlloySegments[0].length) continue;
            count++;
        }


        return result/count;
    }

    public double calculateTemperatureAtRegionSIMD(int x, int y) {
        double result = 0;
        int[][] coordinates = {
                {x, y + 1},
                {x, y - 1},
                {x - 1, y},
                {x + 1, y}
        };

        // Filter valid coordinates
        coordinates = Arrays.stream(coordinates)
                .filter(coord -> !(coord[0] < 0 || coord[1] < 0 || coord[0] >= metalAlloySegments.length || coord[1] >= metalAlloySegments[0].length))
                .toArray(int[][]::new);

        int count = coordinates.length;
        if (count == 0) return 0;

        VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_MAX;

        for (String metal : thermalConstants.keySet()) {
            int length = coordinates.length;
            double[] tempValues = new double[length];
            double[] metalPercentages = new double[length];

            // Collect temperature and percentage values
            for (int i = 0; i < length; i++) {
                int[] coord = coordinates[i];
                tempValues[i] = metalAlloyTemps[coord[0]][coord[1]];
                metalPercentages[i] = metalAlloySegments[coord[0]][coord[1]]
                        .getMetalComposition()
                        .getOrDefault(metal + "percent", 0.0);
            }

            DoubleVector tempVector, percentageVector;
            double sum = 0;

            // Loop over the array in chunks of SIMD width (SPECIES.length())
            int i = 0;
            for (; i < SPECIES.loopBound(count); i += SPECIES.length()) {
                // Load the next chunk of temperature and percentage values into vectors
                tempVector = DoubleVector.fromArray(SPECIES, tempValues, i);
                percentageVector = DoubleVector.fromArray(SPECIES, metalPercentages, i);

                // Compute the weighted sum of the current chunk using SIMD
                sum += tempVector.mul(percentageVector).reduceLanes(VectorOperators.ADD);
            }

            // Tail cleanup loop for remaining elements
            for (; i < count; i++) {
                sum += tempValues[i] * metalPercentages[i];
            }

            // Multiply by the thermal constant for the metal and add to the final result
            result += thermalConstants.get(metal) * sum;
        }

        // Return the final temperature result averaged by the number of valid coordinates
        return result / count;
    }




    public void doOperation() {

        double[][] resultingArray = new double[metalAlloySegments.length][metalAlloySegments[0].length];
        resultingArray[0][0] = this.topLeftHeat;
        resultingArray[metalAlloySegments.length-1][metalAlloySegments[0].length-1] = this.bottomRightHeat;
        for (int i = 0; i < metalAlloySegments.length; i++) {
            for (int j = 0; j < metalAlloySegments[0].length; j++) {
                if ((i == 0 && j == 0) || (i == metalAlloySegments.length-1 && j == metalAlloySegments[0].length-1)) continue;
                resultingArray[i][j] = calculateTemperatureAtRegion(i, j);
            }
        }

//        for (double[] doubles : resultingArray) {
//            System.out.println(Arrays.toString(doubles));
//        }

        metalAlloyTemps = resultingArray;

    }

    public void doOperationSIMD() {

        double[][] resultingArraySIMD = new double[metalAlloySegments.length][metalAlloySegments[0].length];
        resultingArraySIMD[0][0] = this.topLeftHeat;
        resultingArraySIMD[metalAlloySegments.length-1][metalAlloySegments[0].length-1] = this.bottomRightHeat;
        for (int i = 0; i < metalAlloySegments.length; i++) {
            for (int j = 0; j < metalAlloySegments[0].length; j++) {
                if ((i == 0 && j == 0) || (i == metalAlloySegments.length-1 && j == metalAlloySegments[0].length-1)) continue;
                resultingArraySIMD[i][j] = calculateTemperatureAtRegionSIMD(i, j);
            }
        }

//        for (double[] doubles : resultingArray) {
//            System.out.println(Arrays.toString(doubles));
//        }

        metalAlloyTemps = resultingArraySIMD;
    }

    public static void main(String[] args) {
        MetalAlloyJMH metalTest = new MetalAlloyJMH(100, 100, 0.75, 1.0,1.25);
        System.out.println("This is normal");



//        for (int i = 0; i < 10000; i++) {
////            System.out.println("--------");
//            metalTest.doOperation();
////            for (double[] doubles : metalTest.metalAlloyTemps) {
////                System.out.println(Arrays.toString(doubles));
////            }
//        }

        int numIterations = 2;
        long totalTime = 0;

        for (int j = 0; j < numIterations; j++) {
            long startTime = System.nanoTime(); // Record start time

            // Your loop for the operation being tested
            for (int i = 0; i < 100; i++) {
                // metalTest.doOperation(); // This is the actual operation you're testing
                metalTest.doOperation(); // Uncomment the real operation when ready
                if (i % 10 == 0) System.out.println("Doing Stuff : Loop " + i);
            }

            long endTime = System.nanoTime(); // Record end time
            long duration = endTime - startTime; // Calculate duration in nanoseconds
            totalTime += duration; // Accumulate total time

            System.out.println("Iteration " + (j + 1) + " took " + duration / 1_000_000 + " ms");
        }

        // Calculate average time taken per run
        System.out.println("\nAverage time for " + numIterations + " iterations: " + (totalTime / numIterations) / 1_000_000 + " ms");

        MetalAlloyJMH metalTest1 = new MetalAlloyJMH(100, 100, 0.75, 1.0,1.25);
        System.out.println("This is SIMD");
        totalTime = 0;

        for (int j = 0; j < numIterations; j++) {
            long startTime = System.nanoTime(); // Record start time

            // Your loop for the operation being tested
            for (int i = 0; i < 100; i++) {
                metalTest1.doOperationSIMD(); // Uncomment the real operation when ready
                if (i % 10 == 0) System.out.println("Doing Stuff : Loop " + i);
            }

            long endTime = System.nanoTime(); // Record end time
            long duration = endTime - startTime; // Calculate duration in nanoseconds
            totalTime += duration; // Accumulate total time

            System.out.println("Iteration " + (j + 1) + " took " + duration / 1_000_000 + " ms");
        }

        // Calculate average time taken per run
        System.out.println("\nAverage time for " + numIterations + " iterations: " + (totalTime / numIterations) / 1_000_000 + " ms");



    }


}