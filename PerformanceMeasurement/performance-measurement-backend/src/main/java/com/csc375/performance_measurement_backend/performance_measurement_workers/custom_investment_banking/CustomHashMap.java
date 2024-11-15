package com.csc375.performance_measurement_backend.performance_measurement_workers.custom_investment_banking;


import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CustomHashMap<K,V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int capacity;
    private final float loadFactor;
    private int size;
    private Node<K, V>[] table;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public CustomHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    @SuppressWarnings("unchecked")
    public CustomHashMap(int capacity, float loadFactor) {
        this.capacity = capacity;
        this.loadFactor = loadFactor;
        this.table = (Node<K,V>[]) new Node[capacity];
    }

    private static class Node<K,V> {
        final K key;
        V value;
        Node<K,V> next;

        Node(K key, V value, Node<K,V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private int hash(K key, int capacity) {
        return Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value) {

        lock.writeLock().lock();
        try {
            if (size >= capacity * 0.75) {
                resize();
            }


            int index = hash(key, capacity);
        Node<K,V> node = table[index];
        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node<K,V> newNode = new Node<>(key, value, null);
        newNode.next = table[index];
        table[index] = newNode;

        size++;

        } finally {
            lock.writeLock().unlock();
        }
    }

    public V get(K key) {
        lock.readLock().lock();
        try {
            int index = hash(key, capacity);
            Node<K,V> node = table[index];
            while (node != null) {
                if (node.key.equals(key)) {
                    return node.value;
                }
                node = node.next;
            }
            return null;
        } finally {
            lock.readLock().unlock();
        }

    }

    private void resize() {
        lock.writeLock().lock();
        try {
            int newCapacity = capacity * 2;

            @SuppressWarnings("unchecked")
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCapacity];
            for (int i = 0; i < capacity; i++) {
                Node<K, V> node = table[i];
                while (node != null) {
                    Node<K, V> nextNode = node.next;
                    int index = hash(node.key, newCapacity);
                    node.next = newTable[index];
                    newTable[index] = node;
                    node = nextNode;
                }
            }
            table = newTable;
            capacity = newCapacity;
        } finally {
            lock.writeLock().unlock();
        }
    }


    public ArrayList<String> keyArrayList() {
        lock.readLock().lock();
        try {

            ArrayList<String> valuesArrayList = new ArrayList<>();
            for (int i = 0; i < capacity; i++) {
                Node<K, V> node = table[i];
                while (node != null) {
                    valuesArrayList.add(node.key.toString());
                    node = node.next;
                }
            }
            return valuesArrayList;
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return this.keyArrayList().contains(key.toString());
        } finally {
            lock.readLock().unlock();
        }
    }


    public int getSize() {
        lock.readLock().lock();
        try {
            return size;
        } finally {
            lock.readLock().unlock();
        }
    }

    public float getLoadFactor() {
        lock.readLock().lock();
        try {
            return loadFactor;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getCapacity() {
        lock.readLock().lock();
        try {
            return capacity;
        } finally {
            lock.readLock().unlock();
        }
    }
}

