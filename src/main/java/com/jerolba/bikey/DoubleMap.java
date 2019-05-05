package com.jerolba.bikey;

import java.util.HashMap;
import java.util.Map;

public class DoubleMap<R, T, V> extends AbstractTestBikeyMap<R, T, V> {

    private Map<R, Map<T, V>> map = new HashMap<>();

    @Override
    public V put(R firstKey, T secondKey, V value) {
        Map<T, V> innerMap = map.get(firstKey);
        if (innerMap == null) {
            innerMap = new HashMap<>();
            map.put(firstKey, innerMap);
        }
        return innerMap.put(secondKey, value);
    }

    public V get(R firstKey, T secondKey) {
        Map<T, V> innerMap = map.get(firstKey);
        if (innerMap != null) {
            return innerMap.get(secondKey);
        }
        return null;
    }

}
