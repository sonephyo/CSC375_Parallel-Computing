package com.csc375.heat_propagation_backend.metalAlloyServerClient;

import com.csc375.heat_propagation_backend.metalAlloy.MetalAlloy;
import com.csc375.heat_propagation_backend.metalAlloy.MetalAlloyPartition;
import com.csc375.heat_propagation_backend.metalAlloy.MetalAlloySegment;
import com.csc375.heat_propagation_backend.service.WebSocketService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class MetalAlloyClient {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private WebSocketService webSocketService;
    private final double lower_factor = 0.99;
    private final double higher_factor = 1.01;

    public void startConnection(String ip, int port, WebSocketService webSocketService) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
        this.webSocketService = webSocketService;
    }
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new ObjectOutputStream(clientSocket.getOutputStream());
        in = new ObjectInputStream(clientSocket.getInputStream());
    }

    public CompletableFuture<double[][]> sendMessageAsync(MetalAlloy metalAlloy) throws IOException, ClassNotFoundException {

        return CompletableFuture.supplyAsync(() -> {
            synchronized (this) {
                try {
                    out.writeObject(metalAlloy);
                    out.flush();  // Ensure the object is sent
                } catch (IOException e) {
                    throw new RuntimeException("Error sending message", e);
                }

                try {
                    return (double[][]) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("Error receiving response", e);
                }
            }
        });
    }

    public CompletableFuture<double[][]> sendMessageAsync2D(MetalAlloyPartition metalAlloy2D) throws IOException, ClassNotFoundException {


        return CompletableFuture.supplyAsync(() -> {
            synchronized (this) {

                try {
                    out.writeObject(metalAlloy2D);
                    out.flush();  // Ensure the object is sent
                } catch (IOException e) {
                    throw new RuntimeException("Error sending message", e);
                }

                try {
                    return (double[][]) in.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException("Error receiving response", e);
                }
            }
        });
    }


    public void startHeating(MetalAlloy metalTest, int numOfIterations) throws Exception {
        int i = 0;
        while (i <= numOfIterations) {

            MetalAlloy updatedMetalAlloy = metalTest.deepCopy();
//            CompletableFuture<double[][]> serverResultFuture = this.sendMessageAsync(updatedMetalAlloy);

            MetalAlloyPartition metalAlloy2DForServer = prepare2DForServer(metalTest);
            CompletableFuture<double[][]> serverResultFuture = this.sendMessageAsync2D(metalAlloy2DForServer);


            CompletableFuture<double[][]> localResultFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return updatedMetalAlloy.doOperationByRange(
                            updatedMetalAlloy.getMetalAlloyTemps()[0].length / 2,
                            updatedMetalAlloy.getMetalAlloyTemps()[0].length
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            serverResultFuture.join();
            localResultFuture.join();

            CompletableFuture<double[][]> combinedFuture = serverResultFuture.thenCombine(localResultFuture, this::combine2DArrays);
            double[][] combinedResult = combinedFuture.join();
            combinedResult[0][0] = updatedMetalAlloy.getMetalAlloyTemps()[0][0];
            metalTest.setMetalAlloyTemps(combinedResult);

            if (i % 2 == 0) {
                if (webSocketService != null) {
                    webSocketService.sendData(combinedFuture.get());
                }
            }

            if (i % 100 == 0) {
                for (double[] row : combinedFuture.get()) {
                    System.out.println(Arrays.toString(row));
                }
            }

            i++;
        }

        Thread.sleep(1000); // Letting the server and client to process before ending

        if (in != null) {
            in.close();  // Close ObjectInputStream
        }
        if (out != null) {
            out.close();  // Close ObjectOutputStream
        }
        if (clientSocket != null) {
            clientSocket.close();  // Close the Socket
        }

    }

    private MetalAlloyPartition prepare2DForServer(MetalAlloy metalTest) {
        double[][] currentTemps = metalTest.getMetalAlloyTemps();
        MetalAlloySegment[][] currentMetalAlloySegments = metalTest.getMetalAlloySegments();
        HashMap<String, Double> currentThermalConstants = metalTest.getThermalConstants();


        double[][] partitionLeftMetalAlloy = new double[currentTemps.length][(currentTemps[0].length/2) + 1];
        MetalAlloySegment[][] partitionLeftMetalAlloySegments = new MetalAlloySegment[currentTemps.length][(currentTemps[0].length/2) + 1];

        for (int i = 0; i < partitionLeftMetalAlloy.length; i++) {
            for (int j = 0; j < partitionLeftMetalAlloy[i].length; j++) {
                partitionLeftMetalAlloy[i][j] = currentTemps[i][j];
                partitionLeftMetalAlloySegments[i][j] = currentMetalAlloySegments[i][j];
            }
        }

        return new MetalAlloyPartition(partitionLeftMetalAlloy, partitionLeftMetalAlloySegments, currentThermalConstants);
    }

    public double[][] combine2DArrays(double[][] array1, double[][] array2) {
        // Calculate the total number of rows

        double[][] array1Temp = new double[array1.length][array1[0].length-1];
        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array1[0].length-1; j++) {
                array1Temp[i][j] = array1[i][j];
            }
        }
        array1 = array1Temp;

        double[][] resultedArray = new double[array1.length][array1[0].length + array2[0].length]; // Adjust size to hold both arrays
        for (int i = 0; i < array1.length; i++) {
            double[] mergedRow = new double[array1[i].length + array2[i].length];
            for (int j = 0; j < array1[i].length; j++) {
                mergedRow[j] = array1[i][j];
            }
            for (int j = 0; j < array2[i].length; j++) {
                mergedRow[array1[i].length + j] = array2[i][j];
            }

            // Store the merged row in the resulted array
            resultedArray[i] = mergedRow;
        }


        return resultedArray;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}