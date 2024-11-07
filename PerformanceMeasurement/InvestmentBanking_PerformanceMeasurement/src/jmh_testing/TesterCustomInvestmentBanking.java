package jmh_testing;

import custom_investment_banking.CustomInvestmentBanking;
import java_investment_banking.JavaInvestmentBanking;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TesterCustomInvestmentBanking {

    public static void main(String[] args) throws InterruptedException {
        CustomInvestmentBanking banking = new CustomInvestmentBanking();

        Runnable writingNewUsers = () -> {
            Random r = ThreadLocalRandom.current();
            for (int i = 0; i < 1000; i++) {
                banking.registerUser("User" + i, r.nextDouble(100,300));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Runnable readingUsers = () -> {
            Random r = ThreadLocalRandom.current();
            for (int i = 0; i < 5; i++) {
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


    }
}
