package com.jerolba.bikeyjmh;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import com.jerolba.bikey.TupleMapUnoptimized;
import com.jerolba.bikey.TupleUnoptimized;
import com.jerolba.bikeyrunner.RandomDomain;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

@State(Scope.Benchmark)
public class BenchmarkTupleUnoptimizedRead {

    private static String VALUE = "VALUE";

    private RandomDomain domain;
    private List<TupleUnoptimized<Integer, Integer>> tupleDomain;
    private TupleMapUnoptimized<Integer, Integer, String> map;

    @Param({"1000"})
    public int rows;
    @Param({"100"})
    public int cols;
    
    @Setup
    public void setup() throws IOException {
        map = new TupleMapUnoptimized<Integer, Integer, String>();
        domain = new RandomDomain(rows, cols);
        tupleDomain = domain.getDomain().stream().map(k -> new TupleUnoptimized<>(k.i, k.j)).collect(Collectors.toList());
        RandomDomain create = new RandomDomain(rows, cols);
        for (Bikey key : create.getDomain()) {
            map.put(new TupleUnoptimized<>(key.i, key.j), VALUE);
        }
    }

    @Benchmark
    public void readTupleUnoptimized(Blackhole hole) {
        for (TupleUnoptimized<Integer, Integer> bikey : tupleDomain) {
            hole.consume(map.get(bikey));
        }
    }

/*    @Benchmark
    public void readNewTupleUnoptimized(Blackhole hole) {
        for (Bikey bikey : domain.getDomain()) {
            hole.consume(map.get(new TupleUnoptimized<>(bikey.i, bikey.j)));
        }
    }*/

}
