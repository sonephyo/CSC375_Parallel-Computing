package com.csc375.performance_measurement_backend.jmh_testing;


import com.csc375.performance_measurement_backend.performance_measurement_workers.custom_investment_banking.CustomInvestmentBanking;
import org.openjdk.jmh.annotations.*;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Thread)
public class CustomInvestmentBankingBenchmark {

    private CustomInvestmentBanking customInvestmentBanking;

    @Setup(Level.Trial)
    public void setup(SharedData sharedData) {
        customInvestmentBanking = new CustomInvestmentBanking();
        for (String userName: sharedData.currentData.keySet()) {
            customInvestmentBanking.registerUser(userName, sharedData.currentData.get(userName));
        }
    }

    @Benchmark
    @Group("customConcurrentHashMap")
    @GroupThreads(2)
    public boolean containsCustomerBenchmark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return customInvestmentBanking.containsCustomer("User: " + random.nextInt(100));
    }

    @Benchmark
    @Group("customConcurrentHashMap")
    @GroupThreads(2)
    public String putMoneyBenchMark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return customInvestmentBanking.putMoney("User: " + random.nextInt(100), random.nextDouble(0, 10000));
    }

    @Benchmark
    @Group("customConcurrentHashMap")
    @GroupThreads(2)
    public String registerUserBenchmark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return customInvestmentBanking.registerUser("User: " + random.nextInt(), random.nextDouble(0, 100));
    }

    @Benchmark
    @Group("customConcurrentHashMap")
    @GroupThreads(50)
    public Double getMoneyBenchMark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return customInvestmentBanking.getMoney("User: " + random.nextInt());
    }

    @Benchmark
    @Group("customConcurrentHashMap")
    @GroupThreads(30)
    public Set<String> getAllCustomersBenchmark() {
        return customInvestmentBanking.getAllCustomers();
    }


}
