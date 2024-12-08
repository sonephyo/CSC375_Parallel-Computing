package metalAlloyServer;

import metalAlloy.MetalAlloy;

import java.io.*;
import java.net.Socket;
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

    public MetalAlloy sendMessage(MetalAlloy metalAlloy) throws IOException, ClassNotFoundException {
        out.writeObject(metalAlloy);
        return (MetalAlloy) in.readObject();
    }

    public CompletableFuture<MetalAlloy> sendMessageAsync(MetalAlloy metalAlloy) throws IOException, ClassNotFoundException {
        // Simulate sending a message asynchronously and getting a response from the server
        out.writeObject(metalAlloy);
        return CompletableFuture.supplyAsync(() -> {
            // Simulate network delay or long computation
            try {
                Thread.sleep(5000); // Simulate delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                return (MetalAlloy) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}