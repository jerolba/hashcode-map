package com.jerolba.bikeyrunner;

import java.util.Iterator;
import java.util.function.Supplier;

import com.jerolba.bikey.BikeyMap;
import com.jerolba.bikey.DoubleMap;
import com.jerolba.bikey.TupleMap;
import com.jerolba.bikeyrunner.Benchmark.Iteration;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

public class CPUBenchmarkWrite {

    private static final int SAMPLES = 100;
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
        new CPUBenchmarkWrite(rows, cols).go();
    }

    public CPUBenchmarkWrite(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.sampleSize = rows * cols / SAMPLES;
        this.domain = new RandomDomain(rows, cols);
    }

    public void go() {
        System.out.println("Running for " + rows + "x" + cols);
        runWith(TupleMap::new);
        runWith(DoubleMap::new);
    }

    private void runWith(Supplier<BikeyMap<Integer, Integer, String>> factory) {
        Benchmark benchmark = new Benchmark(SAMPLES);
        for (int loops = 0; loops < 30; loops++) {
            Iterator<Bikey> iterator = domain.getDomain().iterator();
            Iteration it = benchmark.newIteration();
            BikeyMap<Integer, Integer, String> bikeyMap = factory.get();
            long start = System.nanoTime();
            int cont = 0;
            while (iterator.hasNext()) {
                Bikey next = iterator.next();
                bikeyMap.put(next.i, next.j, VALUE);
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
