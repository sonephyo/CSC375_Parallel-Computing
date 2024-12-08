package metalAlloy;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class MetalAlloy implements Serializable {

    private double[][] metalAlloyTemps;
    private MetalAlloySegment[][] metalAlloySegments;
    private HashMap<String, Double> thermalConstants;
    private double topLeftHeat;
    private double bottomRightHeat;


    public MetalAlloy(double topLeftHeat, double bottomRightHeat, double metal1ThermalConstant, double metal2ThermalConstant, double metal3ThermalConstant) {
        this.thermalConstants = new HashMap<>();
        thermalConstants.put("metal1", metal1ThermalConstant);
        thermalConstants.put("metal2", metal2ThermalConstant);
        thermalConstants.put("metal3", metal3ThermalConstant);
        this.topLeftHeat = topLeftHeat;
        this.bottomRightHeat = bottomRightHeat;

        int row = 4;
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
                Random random = new Random();

                // Generate a random number between 0.22 and 0.33
                double min = 0.30;
                double max = 0.40;
//                double metal1Percent = min + (max - min) * random.nextDouble();
//                double metal2Percent = min + (max - min) * random.nextDouble();
//                double metal3Percent = 1 - metal1Percent - metal2Percent;
//                metalAlloySegments[i][j] = new metalAlloy.MetalAlloySegment(metal1Percent, metal2Percent, metal3Percent);
                metalAlloySegments[i][j] = new MetalAlloySegment(0.34,0.33,0.33);
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


        return result/coordinates.length;
    }


    public void doOperation() {

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
        metalAlloyTemps = resultingArray;

    }

    public double[][] getMetalAlloyTemps() {
        return metalAlloyTemps;
    }

    public static void main(String[] args) {
        MetalAlloy metalTest = new MetalAlloy(100, 100, 0.75, 1.0,1.25);



        double[][] prev = null;

        for (int i = 0; i < 100000; i++) {
//            System.out.println("--------");
            long startTime = System.nanoTime();
            metalTest.doOperation();
            long endTime = System.nanoTime();

            // Calculate the time taken for execution
            long duration = endTime - startTime;

            // Convert to milliseconds for readability
            if (i%1== 0) System.out.println("iteration: " + i);

            if (i % 50 == 0) {
                System.out.println("Execution time: " + duration/1_000_000_000.0 + " seconds");

                System.out.println("=========================");

                if (prev != null) {
                    System.out.println("Prev and current arrays are equal: " + Arrays.deepEquals(prev, metalTest.metalAlloyTemps));
                }

                for (double[] doubles : metalTest.metalAlloyTemps) {
                    for (double value : doubles) {
                        System.out.printf("%8f ", value); // Aligns values with two decimal places
                    }
                    System.out.println(); // Moves to the next line after printing each row
                }

                prev = metalTest.metalAlloyTemps;

                System.out.println("=========================");
            }
        }
        for (double[] doubles : metalTest.metalAlloyTemps) {
            for (double value : doubles) {
                System.out.printf("%8.2f ", value); // Aligns values with two decimal places
            }
            System.out.println(); // Moves to the next line after printing each row
        }
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
