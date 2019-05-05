package com.jerolba.bikey;

import java.util.HashMap;
import java.util.Map;

public class TupleMapUnoptimized<R, T, V> extends AbstractTestBikeyMap<R, T, V> {

    private final Map<TupleUnoptimized<R, T>, V> map = new HashMap<>();

    @Override
    public V put(R firstKey, T secondKey, V value) {
        return put(new TupleUnoptimized<>(firstKey, secondKey), value);
    }

    public V put(TupleUnoptimized<R, T> key, V value) {
        return map.put(key, value);
    }

    @Override
    public V get(R row, T column) {
        return get(new TupleUnoptimized<R, T>(row, column));
    }
    
    public V get(TupleUnoptimized<R, T> key) {
        return map.get(key);
    }

}
