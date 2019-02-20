package com.jerolba.bikeyrunner;

import java.util.Iterator;
import java.util.function.Supplier;

import com.jerolba.bikey.BikeyMap;
import com.jerolba.bikey.DoubleMap;
import com.jerolba.bikey.TupleMap;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

public class CPUBenchmarkWrite {

    private static final int times = 30;
    private static final String VALUE = "VALUE";

    private int rows;
    private int cols;
    private RandomDomain domain;

    public static void main(String[] args) {
        int rows = 5_000;
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
        this.domain = new RandomDomain(rows, cols);
    }

    public void go() {
        System.out.println("Running for " + rows + "x" + cols);
        runWith(TupleMap::new);
        runWith(DoubleMap::new);
    }

    private void runWith(Supplier<BikeyMap<Integer, Integer, String>> factory) {
        Benchmark benchmark = new Benchmark(times, rows * cols);
        benchmark.run((c) -> {
            Iterator<Bikey> iterator = domain.getDomain().iterator();
            BikeyMap<Integer, Integer, String> bikeyMap = factory.get();
            while (iterator.hasNext()) {
                Bikey next = iterator.next();
                bikeyMap.put(next.i, next.j, VALUE);
                c.consumed();
            }
        });
        benchmark.average().print();
    }

}
