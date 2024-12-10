package com.csc375.heat_propagation_backend.metalAlloyServerClient;

import com.csc375.heat_propagation_backend.metalAlloy.MetalAlloy;
import com.csc375.heat_propagation_backend.service.WebSocketService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
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


    public void startHeating(MetalAlloy metalTest, int numOfIterations) throws Exception {
        int i = 0;
        while (i <= numOfIterations) {

            MetalAlloy updatedMetalAlloy = metalTest.deepCopy();
            CompletableFuture<double[][]> serverResultFuture = this.sendMessageAsync(updatedMetalAlloy);

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

            metalTest.setMetalAlloyTemps(combinedFuture.join());

            if (i % 10 == 0) {
                webSocketService.sendData(combinedFuture.get());
            }

            if (i % 1000 == 0) {
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

    public double[][] combine2DArrays(double[][] array1, double[][] array2) {
        // Calculate the total number of rows

        double[][] resultedArray = new double[array1.length][array1[0].length + array2[0].length]; // Adjust size to hold both arrays
        for (int i = 0; i < array1.length; i++) {
            double[] mergedRow = new double[array1[i].length + array2[i].length];
            for (int j = 0; j < array1[i].length; j++) {
//                if (array1[i][j] > 0) {array1[i][j] *= lower_factor;}
//                if (array1[i][j] < 0) {array1[i][j] *= higher_factor;}
                mergedRow[j] = array1[i][j];
            }
            for (int j = 0; j < array2[i].length; j++) {
//                if (array2[i][j] > 0) {array2[i][j] *= lower_factor;}
//                if (array1[i][j] < 0) {array1[i][j] *= higher_factor;}
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