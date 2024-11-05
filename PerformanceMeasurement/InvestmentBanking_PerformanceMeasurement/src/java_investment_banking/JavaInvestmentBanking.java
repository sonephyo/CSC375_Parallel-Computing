package java_investment_banking;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class JavaInvestmentBanking {

    private final ConcurrentHashMap<String, Double> nameToMoneyHashMap;


    public JavaInvestmentBanking() {
        this.nameToMoneyHashMap = new ConcurrentHashMap<>(16, 0.75f, 16);
    }

    public boolean containsCustomer(String key) {
        return nameToMoneyHashMap.containsKey(key);
    }

    public String putMoney(String name, double amount) {
        if (nameToMoneyHashMap.containsKey(name)) {
            double oldMoney = nameToMoneyHashMap.get(name);
            nameToMoneyHashMap.put(name, nameToMoneyHashMap.get(name) + amount);
            return "The user " + name + " has changed from " + oldMoney + " to " + amount;
        }
        return "The user " + name + " doesn't exist";
    }

    public String registerUser(String name, Number inputAmount) {
        double amount;
        // Check if the input is an instance of Integer or Double
        if (inputAmount instanceof Integer) {
            amount = inputAmount.doubleValue();
        } else if (inputAmount instanceof Double) {
            amount = (Double) inputAmount;
        } else {
            return "Invalid amount type"; // Handle other types if necessary
        }

        if (amount < 0 || amount > Double.MAX_VALUE) {
            return "Invalid amount";
        }
        if (nameToMoneyHashMap.containsKey(name)) {
            return "The user " + name + " has already been registered";
        } else {
            if (amount < 10) {
                return "Need at least 10 dollars for the user to be registered";
            }
            nameToMoneyHashMap.put(name, amount);
            return "The user " + name + "is now registered";
        }
    }

    public Double getMoney(String name) {
        return nameToMoneyHashMap.get(name);
    }

    public Set<String> getAllCustomers() {
        return nameToMoneyHashMap.keySet();
    }



    public static void main(String[] args) throws InterruptedException {


        JavaInvestmentBanking banking = new JavaInvestmentBanking();


        Runnable task1 = () -> {
            Random r = ThreadLocalRandom.current();
            for (int i = 0; i < 10; i++) {
                banking.registerUser("User" + i, r.nextDouble(100,300));
                System.out.println("task 1: " + i + "- " + Thread.currentThread().getName());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };


        for (int i = 0; i < 1; i++) {
            Thread t1 = new Thread(task1);
            t1.setName("Thread" + (i+1));
            t1.start();
            t1.join();
        }

        System.out.println(banking.getAllCustomers());



        System.out.println(banking.nameToMoneyHashMap);
    }
}
