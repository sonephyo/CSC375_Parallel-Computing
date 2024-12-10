package metalAlloyServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import metalAlloy.MetalAlloy;

public class MetalAlloyTest {

    public static void main(String[] args) throws Exception {

        MetalAlloy metalTest = new MetalAlloy(100, 100, 0.75, 1.0,1.25);


//        String ip = "129.3.20.1";
        String ip = "localhost";
        MetalAlloyClient metalAlloyClient = new MetalAlloyClient();

        metalAlloyClient.startConnection(ip, 4444);

        metalAlloyClient.startHeating(metalTest, 1000);




    }
}