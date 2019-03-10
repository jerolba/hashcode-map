package com.jerolba.bikey;

public interface BikeyMap<R, T, V> {

    void put(R firstKey, T secondKey, V value);

}
