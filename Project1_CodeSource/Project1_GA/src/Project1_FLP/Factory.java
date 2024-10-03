package Project1_FLP;

import Project1_FLP.Enum.Station_Type;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Factory implements  Comparable<Factory>{
    private final String id;
    private int[][] spots;
    private final int num_of_stations;
    private double affinity_value;
    private HashMap<Station_Type, List<ClusterStation>> clusterStation_HashMap = new HashMap<>();


    public Factory(int numOfStations) {
        this.id = UUID.randomUUID().toString();
        int randomRowSize = (int) Math.ceil(numOfStations / 2.0);
        int randomColSize = (int) Math.ceil(numOfStations / 2.0);
        this.spots = new int[randomRowSize][randomColSize];
        this.num_of_stations = numOfStations;
    }

    public Factory (Factory factory_to_clone) {
        this.id = UUID.randomUUID().toString();
        this.spots = factory_to_clone.spots;
        this.num_of_stations = factory_to_clone.num_of_stations;
        this.affinity_value = factory_to_clone.affinity_value;
        this.clusterStation_HashMap = factory_to_clone.clusterStation_HashMap;
    }


    public void populate_factory() {

        for (int i = 0 ; i < num_of_stations ; i++) {
            Station station = new Station();
            assign_station(station, 0);
        }
        outputSpots();
    }

    private void assign_station(Station station, int count_of_recursion) {
        int row = ThreadLocalRandom.current().nextInt(spots.length);
        int col = ThreadLocalRandom.current().nextInt(spots[0].length);

        // To prevent recursion overflow
        if (count_of_recursion == 5) {
            return;
        }

            try {

                int value = station.getStation_type_val();
                if (value == 1) {
                    placeStation1(station, row, col, count_of_recursion);
                } else if (value == 2) {
                    placeStation2(station, row, col, count_of_recursion);
                } else if (value == 3) {
                    placeStation3(station, row, col, count_of_recursion);
                } else if (value == 4) {
                    placeStation4(station, row, col, count_of_recursion);
                }
            } catch(IndexOutOfBoundsException e) {
                assign_station(station, + 1);
            } catch (Exception e) {
                System.err.println("An unexpected error occurred: " + e.getMessage());
            }

    }

    public void placeStation1(Station station, int row, int col, int count_of_recursion) {
        if (spots[row][col] == 0 && spots[row - 1][col] == 0 && spots[row][col + 1] == 0 && spots[row - 1][col + 1] == 0) {
            spots[row][col] = station.getStation_type_val();
            spots[row - 1][col] = station.getStation_type_val();
            spots[row][col + 1] = station.getStation_type_val();
            spots[row - 1][col + 1] = station.getStation_type_val();
        } else {
            this.assign_station(station, count_of_recursion + 1);
        }
    }
    public void placeStation2(Station station, int row, int col, int count_of_recursion) {
        if (spots[row][col] == 0 && spots[row-1][col] == 0 && spots[row-1][col+1] == 0) {
            spots[row][col] = station.getStation_type_val();
            spots[row-1][col] = station.getStation_type_val();
            spots[row-1][col+1] = station.getStation_type_val();
        } else {
            this.assign_station(station, count_of_recursion + 1);
        }
    }

    public void placeStation3(Station station, int row, int col, int count_of_recursion) {
        if (spots[row][col] == 0 && spots[row+1][col] == 0 && spots[row-1][col] == 0) {
            spots[row][col] = station.getStation_type_val();
            spots[row+1][col] = station.getStation_type_val();
            spots[row-1][col] = station.getStation_type_val();
        } else {
            this.assign_station(station, count_of_recursion + 1);
        }
    }

    public void placeStation4(Station station, int row, int col, int count_of_recursion) {
        if (spots[row][col] == 0 && spots[row-1][col] == 0 && spots[row][col-1] == 0 && spots[row][col+1] == 0) {
            spots[row][col] = station.getStation_type_val();
            spots[row-1][col] = station.getStation_type_val();
            spots[row][col-1] = station.getStation_type_val();
            spots[row][col+1] = station.getStation_type_val();
        } else {
            this.assign_station(station, count_of_recursion + 1);
        }
    }

    public double evaluate_affinity() {

        clusterStation_HashMap = this.create_cluster(this.spots);
        double result = 0;

        for (Station_Type station_type : clusterStation_HashMap.keySet()) {
            List<ClusterStation> cluster_stations = clusterStation_HashMap.get(station_type);

            // get the station of next char (e.g. StationA -> StationB)
            Station_Type connected_station= Station_Type.getStation_Type(station_type.getValue()+1);
            if (connected_station == null) {
                connected_station = Station_Type.TypeA;
                } // If the type doesn't exist, it should connect back to the first station
            List<ClusterStation> connected_cluster_stations = clusterStation_HashMap.get(connected_station);
            if (connected_cluster_stations != null) {

                for (ClusterStation cluster_station : cluster_stations) {
                    for (ClusterStation connected_cluster_station : connected_cluster_stations) {
                        result += this.getConnectionValue(cluster_station,connected_cluster_station);
                    }
                }
            }

        }

        for (Station_Type station_type : clusterStation_HashMap.keySet()) {
            List<ClusterStation> cluster_stations_copy = new ArrayList<>(clusterStation_HashMap.get(station_type));
            for (int i = 0; i<cluster_stations_copy.size(); i++) {
                ClusterStation cluster_station = cluster_stations_copy.get(i);
                for (int j = i+1; j < cluster_stations_copy.size(); j++) {
                    ClusterStation connected_cluster_station = cluster_stations_copy.get(j);
                    result -= this.getConnectionValue(cluster_station,connected_cluster_station);
                }
            }
        }
        if (result == Double.POSITIVE_INFINITY) {

            double testagain = this.evaluate_affinity();
            throw new IllegalArgumentException("The value is infinity");
        }
        this.affinity_value = result;
        return result;
    }

    /**
     * Generate the connection value between the two station
     * Two Factor influences the connection value
     * 1) The distances between the mid-points of the two stations
     * 2) The spots that each cluster occupies
     * @param first_cluster_station - first cluster station
     * @param second_cluster_station - second cluster station
     * @return
     */
    private double getConnectionValue(ClusterStation first_cluster_station, ClusterStation second_cluster_station) {
        double[] first_station_cluster_mid_point = get_mid_point_cluster(first_cluster_station);
        double[] connected_cluster_station_mid_point = get_mid_point_cluster(second_cluster_station);

        double deltaX = first_station_cluster_mid_point[0] - connected_cluster_station_mid_point[0];
        double deltaY = first_station_cluster_mid_point[1] - connected_cluster_station_mid_point[1];

        int first_cluster_station_len = first_cluster_station.getCoordinates().size();
        int second_cluster_station_len = second_cluster_station.getCoordinates().size();

        double deltaXSquared = deltaX * deltaX * first_cluster_station_len * first_cluster_station_len;
        double deltaYSquared = deltaY * deltaY * second_cluster_station_len * first_cluster_station_len;

        return 1/Math.sqrt(deltaXSquared + deltaYSquared);
    }

    private double[] get_mid_point_cluster(ClusterStation cluster_station) {
        double sumX = 0;
        double sumY = 0;
        int sizeOfCluster = cluster_station.getCoordinates().size();
        for (int[] point: cluster_station.getCoordinates()){
            sumX += point[0];
            sumY += point[1];
        }

        return new double[]{sumX/sizeOfCluster, sumY/sizeOfCluster};

    }

    private HashMap<Station_Type, List<ClusterStation>> create_cluster(int[][] matrix) {
        int[][] copiedMatrix = Arrays.stream(matrix)
                .map(int[]::clone) // Clone each row
                .toArray(int[][]::new);

        clusterStation_HashMap = new HashMap<>();

        for (int i = 0; i < copiedMatrix.length; i++) {
            for (int j = 0; j < copiedMatrix[i].length; j++) {
                if (copiedMatrix[i][j] != 0) {
                    ClusterStation clusterStation = new ClusterStation(copiedMatrix[i][j]);
                    visit_connected_stations(copiedMatrix, i, j, clusterStation);
                    if (clusterStation_HashMap.containsKey(clusterStation.getStation_type())) {
                        clusterStation_HashMap.get(clusterStation.getStation_type()).add(clusterStation);
                    } else {
                        clusterStation_HashMap.put(clusterStation.getStation_type(), new ArrayList<>());
                        clusterStation_HashMap.get(clusterStation.getStation_type()).add(clusterStation);
                    }
                }
            }
        }

        return clusterStation_HashMap;

    }

    private void visit_connected_stations(int[][] matrix, int row, int col,ClusterStation clusterStation) {

        if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length) {
            return ;
        }
        if (matrix[row][col] != clusterStation.get_value_of_Station_type()) {
            return ;
        }

        clusterStation.add_coordinate(new int[]{row, col});
        matrix[row][col]= 0;

        visit_connected_stations(matrix, row+1, col, clusterStation);
        visit_connected_stations(matrix, row, col+1, clusterStation);
        visit_connected_stations(matrix, row, col-1, clusterStation);
        visit_connected_stations(matrix, row-1, col, clusterStation);
    }
    public Factory doMutation() {
        Set<Station_Type> allStations = this.clusterStation_HashMap.keySet();
        if (allStations.isEmpty()) {
            throw new NullPointerException();
        }
        destroyOldCreateNewClusters();

        this.evaluate_affinity();

        return this;
    }

    public void destroyOldCreateNewClusters(){
        Set<Station_Type> allStations = this.clusterStation_HashMap.keySet();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < random.nextInt(1,10); i++) {
            int randomIndex = random.nextInt(clusterStation_HashMap.size());

            List<Station_Type> stationList = new ArrayList<>(allStations);

            Station_Type randomStation = stationList.get(randomIndex);

//            System.out.println(randomStation);
            List<ClusterStation> selectedClusterStations = clusterStation_HashMap.get(randomStation);

            if (selectedClusterStations == null || selectedClusterStations.isEmpty()) {
                selectedClusterStations = clusterStation_HashMap.get(stationList.getFirst());
            }

            ClusterStation clusterStation = selectedClusterStations.get(random.nextInt(selectedClusterStations.size()));

            destroyCoordinatesOfClusterStation(clusterStation);
            randomPlacementOfDestroyedCoordinates(clusterStation);
            this.create_cluster(this.spots);

//            System.out.println("--- ");
//            System.out.println("mutation ended");
//            outputSpots();
//            for (List<ClusterStation> clusterStationList: clusterStation_HashMap.values()) {
//                for (ClusterStation station: clusterStationList) {
//                    System.out.println(station.getStation_type());
//                    for (int[] point: station.getCoordinates()) {
//                        System.out.print(" [" + point[0] + "," + point[1] + "]");
//                    }
//                    System.out.println();
//                }
//            }
//            System.out.println("---");
        }



    }

    public void destroyCoordinatesOfClusterStation(ClusterStation clusterStation) {
        for (int[] coordinate: clusterStation.getCoordinates()){
            spots[coordinate[0]][coordinate[1]] = 0;
        }
    }

    private void randomPlacementOfDestroyedCoordinates(ClusterStation clusterStation) {

        Station_Type station_type = Station_Type.getStation_Type(clusterStation.get_value_of_Station_type());
//        System.out.println("random placement done for " + station_type);
        if (station_type == Station_Type.TypeA) {
            int countOfLoop = clusterStation.getCoordinates().size() / 4;
            for (int i = 0; i < countOfLoop; i++) {
                assign_station(new Station(station_type), -1000);
            }
        } else if (station_type == Station_Type.TypeB) {
            int countOfLoop = clusterStation.getCoordinates().size() / 3;
            for (int i = 0; i < countOfLoop; i++) {
                assign_station(new Station(station_type), -1000);
            }
        } else if (station_type == Station_Type.TypeC) {
            int countOfLoop = clusterStation.getCoordinates().size() / 3;
            for (int i = 0; i < countOfLoop; i++) {
                assign_station(new Station(station_type), -1000);
            }
        } else if (station_type == Station_Type.TypeD) {
            int countOfLoop = clusterStation.getCoordinates().size() / 4;
            for (int i = 0; i < countOfLoop; i++) {
                assign_station(new Station(station_type), -1000);
            }
        }

    }



    public void doCrossover(Factory other_factory) {

    }

    public void setSpots(int[][] spots) {
        this.spots = spots;
    }

    public int[][] getSpots() {
        return spots;
    }

    public double getAffinity_value() {
        return affinity_value;
    }

    @Override
    public int compareTo(Factory other) {
        return Double.compare(this.getAffinity_value(), other.getAffinity_value());
    }

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            Factory factory = new Factory(5);
            factory.populate_factory();
            factory.evaluate_affinity();
        }

        Factory factory1 = new Factory(5);
        factory1.spots = new int[][]{
                {3, 1, 1},
                {3, 1, 1},
                {3, 0, 0}
        };
        factory1.evaluate_affinity();

    }

    public void outputSpots() {
        for (int[] spot : this.spots) {
            System.out.println(Arrays.toString(spot));
        }
    }

}
