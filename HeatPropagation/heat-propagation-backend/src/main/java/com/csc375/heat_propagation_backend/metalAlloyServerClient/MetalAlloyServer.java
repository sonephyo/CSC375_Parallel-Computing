package com.csc375.heat_propagation_backend.metalAlloyServerClient;

import com.csc375.heat_propagation_backend.metalAlloy.MetalAlloy;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MetalAlloyServer {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public void start(int port) throws IOException, ClassNotFoundException {

        System.out.println("Connecting server at port number : " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                new Thread(() -> {
                    try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                         ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());) {
                        while (!clientSocket.isClosed()) {
                            metalAlloyOperation(out, in);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            if (in != null) {
                                in.close();
                            }
                            if (out != null) {
                                out.close();
                            }

                            if (clientSocket != null && !clientSocket.isClosed()) {
                                clientSocket.close(); // Close socket after client disconnects
                            }
                        } catch (IOException e) {
                            System.err.println("Error closing client socket: " + e.getMessage());
                            try {
                                clientSocket.close();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }).start();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void metalAlloyOperation(ObjectOutputStream out, ObjectInputStream in) throws Exception {
        try {

            MetalAlloy metalAlloy = (MetalAlloy) in.readObject();

            assert metalAlloy != null;
            double[][] operationResultByRange = metalAlloy.doOperationByRange(0, metalAlloy.getMetalAlloyTemps()[0].length / 2);

            // Send sorted array back to client
            out.writeObject(operationResultByRange);
        } catch (EOFException e) {
            System.out.println("Client disconnected. Closing Connection");
            clientSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
            clientSocket.close();
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MetalAlloyServer server = new MetalAlloyServer();
        server.start(4444);
    }
}
    