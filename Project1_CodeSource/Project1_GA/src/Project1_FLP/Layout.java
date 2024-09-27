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

    private final List<Future<Factory>> futures = new ArrayList<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    public Layout(int num_of_threads) {
        this.num_of_threads = num_of_threads;
    }

    // Locks for randomizing the GA operations
    private static final Lock lock = new ReentrantLock();

    public void evaluate(int num_of_stations) {

        ExecutorService executorService = Executors.newFixedThreadPool(num_of_threads);

        try {
            for (int i = 0; i < num_of_threads; i++) {
                System.out.println("FactoryTask: " + i);
                Callable<Factory> factoryTask = new FactoryTask(num_of_stations);
                Future<Factory> factoryFuture = executorService.submit(factoryTask);
                futures.add(factoryFuture);
            }

            for (Future<Factory> future : futures) {
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

        doGAOperations(futures);

    }

    public synchronized Factory pickRandom() throws ExecutionException, InterruptedException {

        if (futures.isEmpty()) {
            throw new IllegalStateException("No factories available to pick.");
        }

        int randomIndex = new Random().nextInt(futures.size());
        Factory selectedFactory = futures.get(randomIndex).get();
        futures.remove(randomIndex);
        return selectedFactory;
    }

    public void doGAOperations(List<Future<Factory>> futures) {
        ExecutorService executorServiceForGA = Executors.newFixedThreadPool(num_of_threads);
        try {

            Random random = new Random();
            int index = 0;

            while (index < futures.size()) {
                int gaOperationRandom = ThreadLocalRandom.current().nextInt(2);
                System.out.println(atomicInteger.getAndIncrement());

                executorServiceForGA.submit(() -> {
                    Factory factory = null;
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
        System.out.println("crossover: " + factory1.getAffinity_value()  + " " + factory2.getAffinity_value());
        return factory2;
    }

    private Factory doMutation(Factory factory) {
        System.out.println("mutation: " + factory.getAffinity_value());
        return null;
    }

}
