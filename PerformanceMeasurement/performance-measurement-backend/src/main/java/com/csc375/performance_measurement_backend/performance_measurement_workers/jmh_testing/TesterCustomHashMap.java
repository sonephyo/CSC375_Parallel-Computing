package com.csc375.performance_measurement_backend.performance_measurement_workers.jmh_testing;


import com.csc375.performance_measurement_backend.performance_measurement_workers.custom_investment_banking.CustomHashMap;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TesterCustomHashMap {
    public static void main(String[] args) throws InterruptedException {
        CustomHashMap<String, Double> banking = new CustomHashMap<>();

        Runnable writingNewUsers = () -> {
            Random r = ThreadLocalRandom.current();
            for (int i = 0; i < 1000; i++) {
                banking.put("User" + i, r.nextDouble(100,300));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable readingUsers = () -> {
            for (int i = 0; i < 5; i++) {
                if (banking.keyArrayList().isEmpty()) {
                    System.out.println("No user in the banking yet");
                } else {
                    System.out.println("Current userPool Size: " + banking.keyArrayList().size());
                    String[] temp = banking.keyArrayList().toArray(new String[0]);
                    String user = temp[ThreadLocalRandom.current().nextInt(temp.length)];
                    Double value = banking.get(user);
                    System.out.println(user + " has " + value);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Continuing to next one");
                System.out.println("----");
            }
        };

        Thread t1 = new Thread(writingNewUsers);
        Thread t2 = new Thread(readingUsers);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Size: " + banking.getSize());
        System.out.println("Capacity: " + banking.getCapacity());
    }
}


