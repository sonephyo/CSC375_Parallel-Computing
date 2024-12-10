package metalAlloyServer;

import metalAlloy.MetalAlloy;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class MetalAlloyClient {
    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

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

    public double[][] startHeating(MetalAlloy metalTest, int numOfIterations) throws Exception {
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

            if (i % 100 == 0 ) {
                System.out.println("_----");
                for (double[] row: combinedFuture.get()) {
                    System.out.println(Arrays.toString(row));
                }
            }

            i++;
        }
        Thread.sleep(1000);

        if (in != null) {
            in.close();  // Close ObjectInputStream
        }
        if (out != null) {
            out.close();  // Close ObjectOutputStream
        }
        if (clientSocket != null) {
            clientSocket.close();  // Close the Socket
        }
        return null;

    }

    public double[][] combine2DArrays(double[][] array1, double[][] array2) {
        // Calculate the total number of rows

        double[][] resultedArray = new double[array1.length][array1[0].length];
        for (int i = 0; i < array1.length; i++) {
            double[] mergedRow = new double[array1[i].length + array2[i].length];
            System.arraycopy(array1[i], 0, mergedRow, 0, array1[i].length);

            // Copy elements from the second array to the merged array
            System.arraycopy(array2[i], 0, mergedRow, array1[i].length, array2[i].length);
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