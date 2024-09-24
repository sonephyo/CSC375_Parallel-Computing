package Project1_FLP;

import java.util.concurrent.Callable;

public class FactoryTask implements Callable<Factory>{

    private final int numOfStations;

    public FactoryTask(int numOfStations) {
        this.numOfStations = numOfStations;
    }

    @Override
    public Factory call() throws Exception {
        Factory factory = new Factory(numOfStations);
        factory.populate_factory();
        double result = factory.evaluate_affinity();

        return factory;
    }
}
