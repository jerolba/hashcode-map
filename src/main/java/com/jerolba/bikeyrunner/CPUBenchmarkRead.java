package com.jerolba.bikeyrunner;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.jerolba.bikey.BikeyMap;
import com.jerolba.bikey.DoubleMap;
import com.jerolba.bikey.Tuple;
import com.jerolba.bikey.TupleMap;
import com.jerolba.bikeyrunner.Benchmark.Iteration;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

public class CPUBenchmarkRead {

    private static final int SAMPLES = 50;
    private static final String VALUE = "VALUE";

    private int rows;
    private int cols;
    private RandomDomain domain;
    private int sampleSize;

    public static void main(String[] args) {
        int rows = 10_000;
        int cols = 300;
        if (args.length > 0) {
            rows = Integer.parseInt(args[0]);
            cols = Integer.parseInt(args[1]);
        }
        new CPUBenchmarkRead(rows, cols).go();
    }

    public CPUBenchmarkRead(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.sampleSize = rows * cols / SAMPLES;
        this.domain = new RandomDomain(rows, cols);
    }

    public void go() {
        System.out.println("Running for " + rows + "x" + cols);
        TupleMap<Integer, Integer, String> tupleMap = createMap(TupleMap::new);
        runWithTupleMap(tupleMap);
        DoubleMap<Integer, Integer, String> doubleMap = createMap(DoubleMap::new);
        runWithDoubleMap(doubleMap);
    }

    private <T extends BikeyMap<Integer, Integer, String>> T createMap(Supplier<T> factory) {
        RandomDomain domain = new RandomDomain(rows, cols);
        Iterator<Bikey> iterator = domain.getDomain().iterator();
        T bikeyMap = factory.get();
        while (iterator.hasNext()) {
            Bikey next = iterator.next();
            bikeyMap.put(next.i, next.j, VALUE);
        }
        return bikeyMap;
    }

    private void runWithTupleMap(TupleMap<Integer, Integer, String> bikeyMap) {
        // Instantiate all tuples before accessing it
        List<Tuple<Integer, Integer>> tupleDomain = domain.getDomain().stream().map(k -> new Tuple<>(k.i, k.j))
                .collect(Collectors.toList());

        Benchmark benchmark = new Benchmark(SAMPLES);
        for (int loops = 0; loops < 10; loops++) {
            Iterator<Tuple<Integer, Integer>> iterator = tupleDomain.iterator();
            Iteration it = benchmark.newIteration();
            long start = System.nanoTime();
            int cont = 0;
            while (iterator.hasNext()) {
                Tuple<Integer, Integer> next = iterator.next();
                bikeyMap.get(next);
                cont++;
                if (cont % sampleSize == 0) {
                    long elapsed = System.nanoTime() - start;
                    it.add(cont / sampleSize - 1, elapsed);
                }
            }
        }
        Iteration average = benchmark.average();
        for (long l : average.samples) {
            System.out.println(l);
        }
    }

    private void runWithDoubleMap(DoubleMap<Integer, Integer, String> bikeyMap) {
        Benchmark benchmark = new Benchmark(SAMPLES);
        for (int loops = 0; loops < 10; loops++) {
            Iterator<Bikey> iterator = domain.getDomain().iterator();
            Iteration it = benchmark.newIteration();
            long start = System.nanoTime();
            int cont = 0;
            while (iterator.hasNext()) {
                Bikey next = iterator.next();
                bikeyMap.get(next.i, next.j);
                cont++;
                if (cont % sampleSize == 0) {
                    long elapsed = System.nanoTime() - start;
                    it.add(cont / sampleSize - 1, elapsed);
                }
            }
        }
        Iteration average = benchmark.average();
        for (long l : average.samples) {
            System.out.println(l);
        }
    }
}
