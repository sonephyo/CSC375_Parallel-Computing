package com.csc375.heat_propagation_backend.temp;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class test {

    public static int minAbs(int[] array) {
        if (array == null || array.length == 0) return Integer.MAX_VALUE;
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new RecurT(array, 0, array.length));
    }

    private static class RecurT extends RecursiveTask<Integer> {
        private final int[] array;
        private final int start;
        private final int end;

        RecurT(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start <= 100) {
                int minAbsValue = Integer.MAX_VALUE;
                for (int i = start; i < end; i++) {
                    minAbsValue = Math.min(minAbsValue, Math.abs(array[i]));
                }
                return minAbsValue;
            }
            int mid = start + (end - start) / 2;
            RecurT lt = new RecurT(array, start, mid);
            RecurT rt = new RecurT(array, mid, end);
            lt.fork();
            return Math.min(rt.compute(), lt.join());
        }

        public static void main(String[] args) {
            int[] array = {34, 262, 234, 223, 23523};
            System.out.println(minAbs(array));
            System.out.println(minAbs(new int[]{}));
        }
    }
}
