package com.jerolba.bikeyjmh;

import java.io.IOException;
import java.util.List;

import org.openjdk.jmh.annotations.*;

import com.jerolba.bikey.*;
import com.jerolba.bikeyrunner.RandomDomain;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

@State(Scope.Benchmark)
public class BenchmarkWrite {

    private static String VALUE = "VALUE";

    private RandomDomain domain;
    
    @Param({"1000"})
    public int rows;
    @Param({"100"})
    public int cols;

    @Setup
    public void setup() throws IOException {
        domain = new RandomDomain(rows, cols);
    }

    @Benchmark
    public void writeTuple() {
        TupleMap<Integer, Integer, String> map = new TupleMap<>();
        List<Bikey> rndDomain = domain.getDomain();
        for (Bikey bikey : rndDomain) {
            map.put(new Tuple<>(bikey.i, bikey.j), VALUE);
        }
    }

    @Benchmark
    public void writeDouble() {
        DoubleMap<Integer, Integer, String> map = new DoubleMap<>();
        List<Bikey> rndDomain = domain.getDomain();
        for (Bikey bikey : rndDomain) {
            map.put(bikey.i, bikey.j, VALUE);
        }
    }

    @Benchmark
    public void writeTableBikeyMap() {
        TableBikeyMap<Integer, Integer, String> map = new TableBikeyMap<>();
        List<Bikey> rndDomain = domain.getDomain();
        for (Bikey bikey : rndDomain) {
            map.put(bikey.i, bikey.j, VALUE);
        }
    }
    
    @Benchmark
    public void writeMatrixBikeyMap() {
        MatrixBikeyMap<Integer, Integer, String> map = new MatrixBikeyMap<>();
        List<Bikey> rndDomain = domain.getDomain();
        for (Bikey bikey : rndDomain) {
            map.put(bikey.i, bikey.j, VALUE);
        }
    }
}
