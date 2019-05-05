package com.jerolba.bikey;

import java.util.HashMap;
import java.util.Map;

public class TupleMap<R, T, V>  extends AbstractTestBikeyMap<R, T, V> {

    private final Map<Tuple<R, T>, V> map = new HashMap<>();

    @Override
    public V put(R firstKey, T secondKey, V value) {
        return put(new Tuple<>(firstKey, secondKey), value);
    }

    public V put(Tuple<R, T> key, V value) {
        return map.put(key, value);
    }

    @Override
    public V get(R row, T column) {
        return map.get(new Tuple<>(row, column));
    }

    public V get(Tuple<R, T> key) {
        return map.get(key);
    }


}
