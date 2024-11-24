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

    public void calculateTemperatureAtRegion() {

    }

}
