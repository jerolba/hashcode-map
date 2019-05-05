package com.jerolba.bikeyjmh;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import com.jerolba.bikey.Tuple;
import com.jerolba.bikey.TupleMap;
import com.jerolba.bikeyrunner.RandomDomain;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

@State(Scope.Benchmark)
public class BenchmarkTupleRead {

    private static String VALUE = "VALUE";

    private RandomDomain domain;
    private List<Tuple<Integer, Integer>> tupleDomain;
    private TupleMap<Integer, Integer, String> map;

    @Param({"1000"})
    public int rows;
    @Param({"100"})
    public int cols;
    
    @Setup
    public void setup() throws IOException {
        map = new TupleMap<Integer, Integer, String>();
        domain = new RandomDomain(rows, cols);
        tupleDomain = domain.getDomain().stream().map(k -> new Tuple<>(k.i, k.j)).collect(Collectors.toList());
        RandomDomain create = new RandomDomain(rows, cols);
        for (Bikey key : create.getDomain()) {
            map.put(new Tuple<>(key.i, key.j), VALUE);
        }
    }

    @Benchmark
    public void readTuple(Blackhole hole) {
        for (Tuple<Integer, Integer> bikey : tupleDomain) {
            hole.consume(map.get(bikey));
        }
    }

    @Benchmark
    public void readNewTuple(Blackhole hole) {
        for (Bikey bikey : domain.getDomain()) {
            hole.consume(map.get(new Tuple<>(bikey.i, bikey.j)));
        }
    }

}
