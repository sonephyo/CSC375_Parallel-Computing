// Author - Phone Pyae Sone Phyo

package Project1_FLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Layout{

    private Factory maxValueFactory = null;
    private final int num_of_threads;

    private int futureFactories_size;
    private final List<Future<Factory>> future_Factories = new ArrayList<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public Layout(int num_of_threads) {
        this.num_of_threads = num_of_threads;
    }

    /**
     * Generate factories based on the num_of_threads the class is assigned
     * The maximum affinity will be assigned, to be used in the future.
     * Note: The factories are created in parallel, and need to be put in Future class first
     * @param num_of_stations - station that all factories should have. Number of station influences the factory size.
     */
    public void evaluate(int num_of_stations) {

        ExecutorService executorService = Executors.newFixedThreadPool(num_of_threads);

        try {
            for (int i = 0; i < num_of_threads; i++) {
                System.out.println("FactoryTask: " + i);
                Callable<Factory> factoryTask = new FactoryTask(num_of_stations);
                Future<Factory> factoryFuture = executorService.submit(factoryTask);
                future_Factories.add(factoryFuture);
            }

            for (Future<Factory> future : future_Factories) {
                if (future.isDone() && maxValueFactory != null) {
                    if (maxValueFactory.getAffinity_value() < future.get().getAffinity_value()) {
                        maxValueFactory = future.get();
                    }
                } else {
                    maxValueFactory = future.get();
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }

        doGAOperations();

    }

    /**
     * One thread can only access the method at a time to prevent race conditions
     * @return a random Factory from the available future_Factories
     * @throws ExecutionException -
     * @throws InterruptedException -
     */
    public synchronized Factory pickRandom() throws ExecutionException, InterruptedException {

        if (future_Factories.size() <= 0 ) return null;

        int randomIndex = new Random().nextInt(future_Factories.size());
        Factory selectedFactory = future_Factories.get(randomIndex).get();
        future_Factories.remove(randomIndex);

        return selectedFactory;
    }

    /**
     * Randomly assign mutation or crossover to the future_Factories and then, generate new and higher affinity factories
     */
    public void doGAOperations() {
        ExecutorService executorServiceForGA = Executors.newFixedThreadPool(num_of_threads);
        try {

            futureFactories_size = future_Factories.size();
            int index = 0;


            while (index < futureFactories_size) {

                int gaOperationRandom = ThreadLocalRandom.current().nextInt(2);

                executorServiceForGA.submit(() -> {
                    Factory factory = null;
                    System.out.println("atomicValue: " + atomicInteger.incrementAndGet());
                    if (gaOperationRandom == 0) {
                        try {
                            System.out.println("mutation going");
                            factory = doMutation(pickRandom());
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (gaOperationRandom == 1) {
                        try {
                            System.out.println("crossover going");
                            factory = doCrossOver(pickRandom(), pickRandom());

                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });


                if (gaOperationRandom == 0) {
                    index++;
                }
                if (gaOperationRandom == 1) {
                    index += 2;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            executorServiceForGA.shutdown();
        }
    }

    private Factory doCrossOver(Factory factory1, Factory factory2) {
        if (factory1 == null || factory2 == null) {
            return null;
        }


        System.out.println("crossover: " + factory1.getAffinity_value()  + " " + factory2.getAffinity_value());
        return factory2;
    }

    private Factory doMutation(Factory factory) {
        System.out.println("mutation: " + factory.getAffinity_value());
        return null;
    }

}
