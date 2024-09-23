package Project1_FLP;

import java.util.ArrayList;

public class Layout{

    ArrayList<Factory> factoryArrayList;


    public void evaluate(int num_of_stations, int num_of_threads) {
        factoryArrayList = new ArrayList<>();

        Factory f1 = new Factory();
        f1.populate_factory(num_of_stations);
    }

}
