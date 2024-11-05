# InvestmentBanking_PerformanceMeasurement

## Things about ConcurrentHashMap<K,V>

ConcurrentHashMap is a thread-safe hashmap

**compute(K key, BiFunction<K,V>)** - do something relating to the value by searching the key (Others - computeIfAbsent, computeIfPresent)
Note: the key should exist in the hashmap to be able to call this function or else, it will be NullPointerException.


