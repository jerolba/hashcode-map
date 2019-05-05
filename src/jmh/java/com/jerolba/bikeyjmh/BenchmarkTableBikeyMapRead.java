package com.jerolba.bikeyjmh;

import java.io.IOException;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import com.jerolba.bikey.TableBikeyMap;
import com.jerolba.bikeyrunner.RandomDomain;
import com.jerolba.bikeyrunner.RandomDomain.Bikey;

@State(Scope.Benchmark)
public class BenchmarkTableBikeyMapRead {

    private static String VALUE = "VALUE";

    private RandomDomain domain;
    private TableBikeyMap<Integer, Integer, String> map;
    
    @Param({"1000"})
    public int rows;
    @Param({"100"})
    public int cols;

    @Setup
    public void setup() throws IOException {
        map = new TableBikeyMap<Integer, Integer, String>();
        domain = new RandomDomain(rows, cols);
        RandomDomain create = new RandomDomain(rows, cols);
        for (Bikey key : create.getDomain()) {
            map.put(key.i, key.j, VALUE);
        }
    }

    @Benchmark
    public void readDouble(Blackhole hole) {
        for (Bikey bikey : domain.getDomain()) {
            hole.consume(map.get(bikey.i, bikey.j));
        }
    }

}
