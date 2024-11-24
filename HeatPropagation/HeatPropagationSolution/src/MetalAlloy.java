import java.util.Arrays;
import java.util.HashMap;

public class MetalAlloy {

    private double[][] metalAlloyTemps;
    private MetalAlloySegment[][] metalAlloySegments;
    private HashMap<String, Double> thermalConstants;
    private double topLeftHeat;
    private double bottomRightHeat;

    public MetalAlloy(double metal1ThermalConstant, double metal2ThermalConstant, double metal3ThermalConstant, double topLeftHeat, double bottomRightHeat) {
        this.thermalConstants = new HashMap<>();
        thermalConstants.put("metal1", metal1ThermalConstant);
        thermalConstants.put("metal2", metal2ThermalConstant);
        thermalConstants.put("metal3", metal3ThermalConstant);
        this.topLeftHeat = topLeftHeat;
        this.bottomRightHeat = bottomRightHeat;
    }

    public double calculateTemperatureAtRegion(int x, int y) {
        double result = 0;
        for (String metal: thermalConstants.keySet()) {

            int[][] coordinates = {
                    {x, y+1},
                    {x, y-1},
                    {x-1, y},
                    {x+1, y},
            };

            double individualMetalTempTotal = 0;
            for (int[] coord: coordinates) {
                double metalPercent = metalAlloySegments[coord[0]][coord[1]].getMetalComposition().get(metal + "percent");
                individualMetalTempTotal += metalAlloyTemps[coord[0]][coord[1]] * metalPercent;
            }

            result += thermalConstants.get(metal) * individualMetalTempTotal;
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] coordinates = {
                {0, 1},
                {0, 1},
                {0, 1},
                {0, 1},
        };

        int[][] arrayTest = {
                {1, 2, 3, 4, 5, 6, 7, 8},
                {9, 10, 11, 12, 13, 14, 15, 16},
                {17, 18, 19, 20, 21, 22, 23, 24},
                {25, 26, 27, 28, 29, 30, 31, 32},
                {33, 34, 35, 36, 37, 38, 39, 40},
                {41, 42, 43, 44, 45, 46, 47, 48},
                {49, 50, 51, 52, 53, 54, 55, 56},
                {57, 58, 59, 60, 61, 62, 63, 64}
        };


        double ans = Arrays.stream(coordinates)
                .mapToDouble(coord -> arrayTest[coord[0]][coord[1]]) // map each coordinate to a value from arrayTest
                .sum();

        System.out.println(ans);
    }

}
