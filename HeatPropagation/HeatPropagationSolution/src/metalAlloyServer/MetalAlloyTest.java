package metalAlloyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import metalAlloy.MetalAlloy;

public class MetalAlloyTest {

    private static final String ip = "localhost";

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        MetalAlloyClient metalAlloyClient = new MetalAlloyClient();
        metalAlloyClient.startConnection(ip, 4444);

        MetalAlloy metalTest = new MetalAlloy(100, 100, 0.75, 1.0,1.25);

        CompletableFuture<MetalAlloy> metalResult = metalAlloyClient.sendMessageAsync(metalTest);

        metalResult.thenAccept(result -> {
            for (double[] metalrow : result.getMetalAlloyTemps())  {
                System.out.println(Arrays.toString(metalrow));
            }
        });

//        for (double[] metalrow : metalTest.getMetalAlloyTemps())  {
//            System.out.println(Arrays.toString(metalrow));
//        }

        metalResult.join();
        System.out.println("Going to end the conection");
        metalAlloyClient.stopConnection();
    }
}
