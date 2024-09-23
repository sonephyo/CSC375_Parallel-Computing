package Project1_FLP;

public class Main {
    static final int num_of_stations = 48;

    static final int num_of_threads = 4;


    public static void main(String[] args) {

        Layout layout = new Layout();
        layout.evaluate(num_of_stations, num_of_threads);

    }
}