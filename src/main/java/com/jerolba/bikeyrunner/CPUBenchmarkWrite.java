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
        String version = "baseline";
        int rows = 2_000;
        int cols = 200;
        if (args.length > 0) {
            version = args[0];
            rows = Integer.parseInt(args[1]);
            cols = Integer.parseInt(args[2]);
        }
        new CPUBenchmarkWrite(rows, cols).go(version);
    }

    public CPUBenchmarkWrite(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.domain = new RandomDomain(rows, cols);
    }

    public void go(String version) {
        System.out.println("Running "+ version + " for " + rows + "x" + cols);
        if (version.equals("baseline")) {
            baseline();
        } else if (version.equals("TupleMap")) {
            runWith(TupleMap::new);
        } else if (version.equals("DoubleMap")) {
            runWith(DoubleMap::new);
        }
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
    
    private void baseline() {
        Benchmark benchmark = new Benchmark(times, rows * cols);
        long[] hole = new long[2];
        benchmark.run((c) -> {
            Iterator<Bikey> iterator = domain.getDomain().iterator();
            while (iterator.hasNext()) {
                Bikey next = iterator.next();
                hole[0]+=next.i;
                hole[1]+=next.j;
                c.consumed();
            }
        });
        System.out.println(hole);
        benchmark.average().print();
    }

}
