package com.jerolba.bikeyrunner;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.jerolba.bikey.BikeyMap;
import com.jerolba.bikey.DoubleMap;
import com.jerolba.bikey.Tuple;
import com.jerolba.bikey.TupleMap;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

public class CPUBenchmarkRead {

    private static final String VALUE = "VALUE";
    private static final int times = 100;
    private int rows;
    private int cols;

    public static void main(String[] args) {
        String version = "baseline";
        int rows = 2_000;
        int cols = 200;
        if (args.length > 0) {
            version = args[0];
            rows = Integer.parseInt(args[1]);
            cols = Integer.parseInt(args[2]);
        }
        new CPUBenchmarkRead(rows, cols).go(version);
    }

    public CPUBenchmarkRead(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public void go(String version) {
        System.out.println("Running "+ version + " for " + rows + "x" + cols);
        if (version.equals("baseline")) {
            baseline();
        } else if (version.equals("TupleMap")) {
            TupleMap<Integer, Integer, String> tupleMap = createMap(TupleMap::new);
            runWithTupleMap(tupleMap);
        } else if (version.equals("DoubleMap")) {
            DoubleMap<Integer, Integer, String> doubleMap = createMap(DoubleMap::new);
            runWithDoubleMap(doubleMap);
        }
    }

    private void runWithTupleMap(TupleMap<Integer, Integer, String> bikeyMap) {
        // Instantiate all tuples before accessing it
        RandomDomain domain = new RandomDomain(rows, cols);
        List<Tuple<Integer, Integer>> tupleDomain = domain.getDomain().stream().map(k -> new Tuple<>(k.i, k.j))
                .collect(Collectors.toList());

        Benchmark benchmark = new Benchmark(times, rows * cols);
        benchmark.run((c) -> {
            Iterator<Tuple<Integer, Integer>> iterator = tupleDomain.iterator();
            while (iterator.hasNext()) {
                bikeyMap.get(iterator.next());
                c.consumed();
            }
        });
        benchmark.average().print();
    }

    private void runWithDoubleMap(DoubleMap<Integer, Integer, String> bikeyMap) {
        RandomDomain domain = new RandomDomain(rows, cols);
        Benchmark benchmark = new Benchmark(times, rows * cols);
        benchmark.run((c) -> {
            Iterator<Bikey> iterator = domain.getDomain().iterator();
            while (iterator.hasNext()) {
                Bikey next = iterator.next();
                bikeyMap.get(next.i, next.j);
                c.consumed();
            }
        });
        benchmark.average().print();
    }

    private void baseline() {
        RandomDomain domain = new RandomDomain(rows, cols);
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

}
