package Project1_FLP;

import java.util.Random;

public class Factory extends Thread{
    private int[][] spots;


    public Factory() {
        this.spots = new int[100][100];

    }

    public void populate_factory(int number_of_station) {

        for (int i = 0; i < number_of_station; i++) {
            Station station = new Station();
            assign_station(station);
        }
    }

    private void assign_station(Station station) {
        int row = new Random().nextInt(spots.length);
        int col = new Random().nextInt(spots[0].length);

        System.out.println("row: " + row + ", col: " + col);
    }

    private void evaluate_affinity() {

    }

    @Override
    public void run() {

    }

    public static void main(String[] args) {
        Factory f = new Factory();
        f.populate_factory(10);
    }
}
