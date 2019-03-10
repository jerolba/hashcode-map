package com.jerolba.bikey;

import java.util.Objects;

public class Tuple<R, T> {

    private R first;
    private T second;

    public Tuple(R first, T second) {
        this.first = first;
        this.second = second;
    }

    public R getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return (first.hashCode() << 16) + second.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Tuple<?,?> other = (Tuple<?,?>) obj;
        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }
}
