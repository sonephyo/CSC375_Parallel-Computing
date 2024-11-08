package com.csc375.performance_measurement_backend.jmh_testing;


import com.csc375.performance_measurement_backend.performance_measurement_workers.java_investment_banking.JavaInvestmentBanking;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TesterJavaInvestmentBanking {

    public static void main(String[] args) throws InterruptedException {
        Random r = ThreadLocalRandom.current();

        JavaInvestmentBanking banking = new JavaInvestmentBanking();
        for (int i = 0; i < 10; i++) {
            banking.registerUser("User" + i, r.nextDouble(100, 300));
        }

        Runnable writingNewUsers = () -> {
            for (int i = 10; i < 100; i++) {
                banking.registerUser("User" + i, r.nextDouble(100,300));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable readingUsers = () -> {
            for (int i = 0; i < 5000; i++) {
                if (banking.getAllCustomers().isEmpty()) {
                    System.out.println("No user in the banking yet");
                } else {
                    System.out.println("Current userPool Size: " + banking.getAllCustomers().size());
                    String[] temp = banking.getAllCustomers().toArray(new String[0]);
                    String user = temp[ThreadLocalRandom.current().nextInt(temp.length)];
                    Double value = banking.getMoney(user);
                    System.out.println(user + " has " + value);
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("----");
            }
        };

        Thread t1 = new Thread(writingNewUsers);
        Thread t2 = new Thread(readingUsers);
        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("Current userPool Size: " + banking.getAllCustomers().size());


    }
}
