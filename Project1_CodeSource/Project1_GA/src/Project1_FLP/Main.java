package Project1_FLP;

public class Main {
    static final int num_of_stations = 10;

    static final int num_of_threads = 1;
    static final int countOfGAOperations = 2000;


    public static void main(String[] args) {

        Layout layout = new Layout(num_of_threads);
        layout.evaluate(num_of_stations, countOfGAOperations);

    }
}