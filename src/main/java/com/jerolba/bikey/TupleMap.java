package com.jerolba.bikey;

import java.util.HashMap;
import java.util.Map;

public class TupleMap<R, T, V> implements BikeyMap<R, T, V> {

    private final Map<Tuple<R, T>, V> map = new HashMap<>();

    @Override
    public void put(R firstKey, T secondKey, V value) {
        put(new Tuple<>(firstKey, secondKey), value);
    }

    public void put(Tuple<R, T> key, V value) {
        map.put(key, value);
    }

    public V get(Tuple<R, T> key) {
        return map.get(key);
    }

}
