package Project1_FLP;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Factory{
    private final int[][] spots;
    private final int num_of_stations;


    public Factory(int numOfStations) {
        int randomRowSize = (int) Math.ceil(numOfStations / 2.0);
        int randomColSize = (int) Math.ceil(numOfStations / 2.0);
        this.spots = new int[randomRowSize][randomColSize];
        this.num_of_stations = numOfStations;
    }

    public void populate_factory() {

        for (int i = 0 ; i < num_of_stations ; i++) {
            Station station = new Station();
            assign_station(station);
        }
        System.out.println("Done populating factory");
    }

    private void assign_station(Station station) {
        int row = ThreadLocalRandom.current().nextInt(spots.length);
        int col = ThreadLocalRandom.current().nextInt(spots[0].length);

        if (spots[row][col] == 0) {
            spots[row][col] = station.getStation_type_val();
        } else {
            this.assign_station(station);
        }
    }

    private void evaluate_affinity() {

    }

    @Override
    public String toString() {

        System.out.println("-----");
        for (int[] spot : spots) {
            System.out.println(Arrays.toString(spot));
        }
        System.out.println("-----");


        return "Factory{" +
                "spots=" +
                '}';
    }
}
