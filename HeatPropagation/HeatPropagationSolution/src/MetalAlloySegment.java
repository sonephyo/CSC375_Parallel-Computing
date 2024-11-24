import java.util.HashMap;

public class MetalAlloySegment {

    private HashMap<String, Double> metalComposition;

    public MetalAlloySegment(double metal1Percentage, double metal2Percentage, double metal3Percentage) {
        this.metalComposition = new HashMap<>();
        metalComposition.put("metal1percent", metal1Percentage);
        metalComposition.put("metal2percent", metal2Percentage);
        metalComposition.put("metal3percent", metal3Percentage);
    }

    public HashMap<String, Double> getMetalComposition() {
        return metalComposition;
    }
}
