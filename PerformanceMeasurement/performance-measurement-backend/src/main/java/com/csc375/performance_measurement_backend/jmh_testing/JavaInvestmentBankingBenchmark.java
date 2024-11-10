package com.csc375.performance_measurement_backend.jmh_testing;


import com.csc375.performance_measurement_backend.performance_measurement_workers.java_investment_banking.JavaInvestmentBanking;
import org.openjdk.jmh.annotations.*;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Thread)
public class JavaInvestmentBankingBenchmark {

    private JavaInvestmentBanking javaInvestmentBanking;

    @Setup(Level.Trial)
    public void setup(SharedData sharedData) {
        javaInvestmentBanking = new JavaInvestmentBanking();
        for (String userName: sharedData.currentData.keySet()) {
            javaInvestmentBanking.registerUser(userName, sharedData.currentData.get(userName));
        }
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(2)
    public boolean containsCustomerBenchmark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return javaInvestmentBanking.containsCustomer("User: " + random.nextInt(100));
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(2)
    public String putMoneyBenchMark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return javaInvestmentBanking.putMoney("User: " + random.nextInt(100), random.nextDouble(0, 10000));
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(2)
    public String registerUserBenchmark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return javaInvestmentBanking.registerUser("User: " + random.nextInt(), random.nextDouble(0, 100));
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(20)
    public Double getMoneyBenchMark() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return javaInvestmentBanking.getMoney("User: " + random.nextInt());
    }

    @Benchmark
    @Group("concurrentHashMap")
    @GroupThreads(30)
    public Set<String> getAllCustomersBenchmark() {
        return javaInvestmentBanking.getAllCustomers();
    }


}
