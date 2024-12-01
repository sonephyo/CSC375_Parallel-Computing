import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorSpecies;

import java.util.Arrays;
import java.util.HashMap;

public class MetalAlloy {

    private double[][] metalAlloyTemps;
    private MetalAlloySegment[][] metalAlloySegments;
    private HashMap<String, Double> thermalConstants;
    private double topLeftHeat;
    private double bottomRightHeat;
    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;


    public MetalAlloy(double topLeftHeat, double bottomRightHeat, double metal1ThermalConstant, double metal2ThermalConstant, double metal3ThermalConstant) {
        this.thermalConstants = new HashMap<>();
        thermalConstants.put("metal1", metal1ThermalConstant);
        thermalConstants.put("metal2", metal2ThermalConstant);
        thermalConstants.put("metal3", metal3ThermalConstant);
        this.topLeftHeat = topLeftHeat;
        this.bottomRightHeat = bottomRightHeat;

        int row = 3;
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
        for (String metal: thermalConstants.keySet()) {

            double individualMetalTempTotal = 0;
            for (int[] coord: coordinates) {
                // Condition for checking the edges
                if (coord[0] < 0 || coord[1] < 0 || coord[0] >= metalAlloySegments.length || coord[1] >= metalAlloySegments[0].length) continue;
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

    public void doOperation() {

        double[][] resultingArray = new double[metalAlloySegments.length][metalAlloySegments[0].length];
        resultingArray[0][0] = this.topLeftHeat;
        resultingArray[metalAlloySegments.length-1][metalAlloySegments[0].length-1] = this.bottomRightHeat;
//        for (int i = 0; i < metalAlloySegments.length; i++) {
//            for (int j = 0; j < metalAlloySegments[0].length; j++) {
//                if ((i == 0 && j == 0) || (i == metalAlloySegments.length-1 && j == metalAlloySegments[0].length-1)) continue;
//                resultingArray[i][j] = calculateTemperatureAtRegion(i, j);
//            }
//        }

        for (int i = 0; i < metalAlloySegments.length; i++) {
            // Implementing SIMD to each row
        }

//        for (double[] doubles : resultingArray) {
//            System.out.println(Arrays.toString(doubles));
//        }

        metalAlloyTemps = resultingArray;

    }

    public static void main(String[] args) {
        MetalAlloy metalTest = new MetalAlloy(100, 100, 0.75, 1.0,1.25);



        for (int i = 0; i < 2; i++) {
            System.out.println("--------");
            metalTest.doOperation();
            for (double[] doubles : metalTest.metalAlloyTemps) {
                System.out.println(Arrays.toString(doubles));
            }
        }



    }


}
