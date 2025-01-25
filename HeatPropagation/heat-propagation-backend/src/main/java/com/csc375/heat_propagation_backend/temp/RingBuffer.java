package com.csc375.heat_propagation_backend.temp;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RingBuffer {
    private Object[] buffer;
    private int head = 0;
    private int tail = 0;
    private int capacity;
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private Condition notFull = lock.newCondition();

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new Object[capacity];
    }

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while ((tail + 1) % capacity == head) {
                notFull.await();
            }
            buffer[tail] = x;
            tail = (tail + 1) % capacity;
            if (tail == head) {
                head = (head + 1) % capacity;
            }
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (head == tail) {
                notEmpty.await();
            }
            Object item = buffer[head];
            head = (head + 1) % capacity;
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}

