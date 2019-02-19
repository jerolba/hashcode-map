package com.jerolba.bikeyrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Benchmark {

    private List<Iteration> iterations = new ArrayList<>();

    private final int samples;

    public Benchmark(int samples) {
        this.samples = samples;
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

    }
}