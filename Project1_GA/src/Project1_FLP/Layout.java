package Project1_FLP;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Layout{

    ArrayList<Factory> factoryArrayList;

    public void evaluate(int num_of_stations, int num_of_threads) {
        factoryArrayList = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(num_of_threads);
        List<Future<Factory>> futures = new ArrayList<>();

        for (int i = 0; i < num_of_threads; i++) {
            System.out.println("FactoryTask: " + i);
            Callable<Factory> factoryTask = new FactoryTask(num_of_stations);
            Future<Factory> factoryFuture = executorService.submit(factoryTask);
            futures.add(factoryFuture);
        }

        try {
            for (Future<Factory> factoryFuture : futures) {
                Factory factory = factoryFuture.get();
                System.out.println(factory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }

}
