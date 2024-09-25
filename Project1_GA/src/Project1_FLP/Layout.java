package Project1_FLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Layout{

    ArrayList<Factory> factoryArrayList;

    public void evaluate(int num_of_stations, int num_of_threads) {
        factoryArrayList = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(num_of_threads);
        List<Future<Factory>> futures = new ArrayList<>();

        try {
            for (int i = 0; i < num_of_threads; i++) {
                System.out.println("FactoryTask: " + i);
                Callable<Factory> factoryTask = new FactoryTask(num_of_stations);
                Future<Factory> factoryFuture = executorService.submit(factoryTask);
                futures.add(factoryFuture);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }

        doGAOperations(futures, num_of_threads);

    }

    public void doGAOperations(List<Future<Factory>> futures, int num_of_threads) {
        ExecutorService executorServiceForGA = Executors.newFixedThreadPool(num_of_threads);
        try {
            int i = 0;
            while (i < futures.size()) {

                Random overallRandom = new Random();
                int selectedOperation = overallRandom.nextInt(2);
                System.out.println(selectedOperation);
                System.out.println(futures.get(i).get());
                System.out.println(futures.size());
                i++;
//                Factory factory1 = futures.get(i).get();
//                Factory factory2 = futures.get(i+1).get();
//                Callable<Factory> combineFactory = () -> factory1.combineFactory(factory2);
//                executorServiceForGA.submit(combineFactory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorServiceForGA.shutdown();
        }
    }

}
