package com.csc375.genetic_algorithm_project1_backend.facilityLayoutProblemSolution;

public class Main {
    static final int num_of_stations = 48;

    static final int num_of_threads = 1;
    static final int countOfGAOperations = 2000;


    public static void main(String[] args) throws InterruptedException {

        Layout layout = new Layout(num_of_threads);
        layout.evaluate(num_of_stations, countOfGAOperations);

    }
}