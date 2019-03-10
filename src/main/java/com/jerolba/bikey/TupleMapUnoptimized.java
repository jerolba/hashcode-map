package com.jerolba.bikey;

import java.util.HashMap;
import java.util.Map;

public class TupleMapUnoptimized<R, T, V> implements BikeyMap<R, T, V> {

    private final Map<TupleUnoptimized<R, T>, V> map = new HashMap<>();

    @Override
    public void put(R firstKey, T secondKey, V value) {
        put(new TupleUnoptimized<>(firstKey, secondKey), value);
    }

    public void put(TupleUnoptimized<R, T> key, V value) {
        map.put(key, value);
    }

    public V get(TupleUnoptimized<R, T> key) {
        return map.get(key);
    }

}
