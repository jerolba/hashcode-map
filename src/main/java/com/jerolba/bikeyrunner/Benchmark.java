package com.jerolba.bikeyrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class Benchmark {

    private List<Iteration> iterations = new ArrayList<>();

    private final int samples;
    private final int times;
    private final int sampleSize;

    public Benchmark(int times, int problemSize) {
        this(times, problemSize, 100);
    }

    public Benchmark(int times, int problemSize, int samples) {
        this.times = times;
        this.samples = samples;
        this.sampleSize = problemSize / samples;
    }

    public void run(Consumer<Consumed> runnable) {
        for (int i = 0; i < times; i++) {
            Iteration it = this.newIteration();
            long start = System.nanoTime();
            int[] cont = new int[1];
            cont[0] = 0;
            Consumed c = () -> {
                int current = cont[0];
                current++;
                if (isMark(current)) {
                    long elapsed = System.nanoTime() - start;
                    it.add(current / sampleSize - 1, elapsed);
                }
                cont[0] = current;
            };
            runnable.accept(c);
        }
    }

    public boolean isMark(int counter) {
        return counter % sampleSize == 0;
    }

    public Iteration newIteration() {
        Iteration it = new Iteration(samples);
        this.iterations.add(it);
        return it;
    }

    public Iteration average() {
        List<Iteration> copy = new ArrayList<>(iterations);
        Collections.sort(copy);
        Iteration ac = new Iteration(samples);
        for (int i = 1; i < copy.size() - 1; i++) {
            ac.add(copy.get(i));
        }
        ac.forIterations(samples - 2);
        return ac;
    }

    @FunctionalInterface
    public static interface Consumed {

        void consumed();

    }

    public static class Iteration implements Comparable<Iteration> {

        final long[] samples;

        public Iteration(int samplesNumber) {
            this.samples = new long[samplesNumber];
        }

        public void add(int n, long value) {
            samples[n] += value;
        }

        public long getTotalTime() {
            return samples[samples.length - 1];
        }

        public void add(Iteration other) {
            for (int i = 0; i < samples.length; i++) {
                samples[i] += other.samples[i];
            }
        }

        public void forIterations(int n) {
            for (int i = 0; i < samples.length; i++) {
                samples[i] = samples[i] / n;
            }
        }

        @Override
        public int compareTo(Iteration o) {
            return (int) (o.getTotalTime() - this.getTotalTime());
        }

        public void print() {
            for (long l : samples) {
                System.out.println(l);
            }
        }

    }
}