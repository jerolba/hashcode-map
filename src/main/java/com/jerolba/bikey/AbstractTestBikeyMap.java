package com.jerolba.bikey;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class AbstractTestBikeyMap<R, T, V> implements BikeyMap<R, T, V> {

    @Override
    public Iterator<BikeyEntry<R, T, V>> iterator() {
        return null;
    }

    @Override
    public V remove(R row, T column) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public Set<Bikey<R, T>> keySet() {
        return null;
    }

    @Override
    public BikeySet<R, T> bikeySet() {
        return null;
    }

    @Override
    public Set<R> rowKeySet() {
        return null;
    }

    @Override
    public Set<T> columnKeySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<BikeyEntry<R, T, V>> entrySet() {
        return null;
    }

    @Override
    public void forEachBikey(BiConsumer<? super R, ? super T> action) {

    }

    @Override
    public void forEach(TriConsumer<? super R, ? super T, ? super V> action) {

    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public boolean containsRow(Object row) {
        return false;
    }

    @Override
    public boolean containsColumn(Object column) {
        return false;
    }

}
