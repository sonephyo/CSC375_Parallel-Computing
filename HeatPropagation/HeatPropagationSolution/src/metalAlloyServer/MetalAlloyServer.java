package metalAlloyServer;

import metalAlloy.MetalAlloy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class MetalAlloyServer {

    private Socket clientSocket;
    private ObjectOutputStream out;
    private ObjectInputStream in;


    public void start(int port) throws IOException, ClassNotFoundException {

        System.out.println("Connecting server at port number : " + port);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                clientSocket = serverSocket.accept();
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());

                metalAlloyOperation(out, in);

                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void metalAlloyOperation(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {

        MetalAlloy metalAlloy = (MetalAlloy) in.readObject();
        System.out.println("Got the metalAlloy, returning it now");
        metalAlloy.doOperation();

        // Send sorted array back to client
        out.writeObject(metalAlloy);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MetalAlloyServer server = new MetalAlloyServer();
        server.start(4444);
    }
}
    