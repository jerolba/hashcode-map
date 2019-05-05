package com.jerolba.bikeyrunner;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import com.jerolba.bikey.BikeyMap;
import com.jerolba.bikey.DoubleMap;
import com.jerolba.bikey.TupleMap;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;
import com.jerolba.jmnemohistosyne.Histogramer;
import com.jerolba.jmnemohistosyne.MemoryHistogram;

public class MemoryBenchmark {

    private static final int SAMPLES = 50;
    private static final String VALUE = "VALUE";
    private static final List<Object> classes = Arrays.asList(
            "com.jerolba.bikey.*",
            "java.util.HashMap*",
            "Object[]",
            "int[]",
            "long[]",
            "java.util.HashSet",
            "java.util.ArrayList",
            "java.util.LinkedHashMap*",
            "java.util.TreeMap*",
            "java.util.BitSet"
            );

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
        new MemoryBenchmark(rows, cols).go();
    }

    public MemoryBenchmark(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.sampleSize = rows * cols / SAMPLES;
        this.domain = new RandomDomain(rows, cols);
    }

    public void go() {
        System.out.println("Running for " + rows + "x" + cols);
        Iterator<Bikey> iteratorTuple = domain.getDomain().iterator();
        runWith(iteratorTuple, TupleMap::new);

        Iterator<Bikey> iteratorDouble = domain.getDomain().iterator();
        runWith(iteratorDouble, DoubleMap::new);
    }

    private void runWith(Iterator<Bikey> iterator, Supplier<BikeyMap<Integer, Integer, String>> factory) {
        Histogramer histogramer = new Histogramer();
        MemoryHistogram reference = histogramer.createHistogram();
        MemoryHistogram diff = reference;
        BikeyMap<Integer, Integer, String> bikeyMap = factory.get();
        int cont = 0;
        while (iterator.hasNext()) {
            Bikey next = iterator.next();
            bikeyMap.put(next.i, next.j, VALUE);
            cont++;
            if (cont % sampleSize == 0) {
                MemoryHistogram current = histogramer.createHistogram();
                diff = current.diff(reference);
                System.out.println(cont + "," + diff.filter(classes.toArray()).getTotalMemory());
            }
        }
        System.out.println(diff.getTop(40));
    }

}
