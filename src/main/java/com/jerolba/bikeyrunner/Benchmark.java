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
        warmup(runnable);
        for (int i = 0; i < times; i++) {
            Iteration it = this.newIteration();
            long start = System.nanoTime();
            Counter counter = new Counter();
            Consumed c = () -> {
                int current = counter.inc();
                current++;
                if (isMark(current)) {
                    long elapsed = System.nanoTime() - start;
                    it.add(current / sampleSize - 1, elapsed);
                }
            };
            runnable.accept(c);
        }
    }
    
    public void warmup(Consumer<Consumed> runnable) {
        long start = System.currentTimeMillis();
        Counter counter = new Counter();
        try {
            for (int i = 0; i < 100000; i++) {
                Consumed c = () -> {
                    if (counter.inc() % 10_000 == 0) {
                        if ((System.currentTimeMillis() - start) > 10_000) {
                            throw new RuntimeException();
                        }
                    }
                };
                runnable.accept(c);
            }
        } catch (RuntimeException stop) {

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
        int discarded = iterations.size() * 10 / 100;
        List<Iteration> copy = new ArrayList<>(iterations);
        Collections.sort(copy);
        Iteration ac = new Iteration(samples);
        for (int i = discarded; i < copy.size() - discarded; i++) {
            ac.add(copy.get(i));
        }
        ac.forIterations(samples - 2 * discarded);
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
    
    public static class Counter {
        
        public int count = 0;
        
        public int inc() {
            return ++count;
        }
        
    }
}