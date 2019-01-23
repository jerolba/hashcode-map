package com.jerolba.hashcode;

/**
 * Key of the fixed Object with the correct hashCode implementation.
 *
 */
public class FixedObjectKey {

    // Utility static counters to meter the number of calls to hashCode and equals
    public static int hashCount = 0;
    public static int equalsCount = 0;

    private int firstKey;
    private int secondKey;

    public FixedObjectKey(int firstKey, int secondKey) {
        this.firstKey = firstKey;
        this.secondKey = secondKey;
    }

    public int getFirstKey() {
        return firstKey;
    }

    public int getSecondKey() {
        return secondKey;
    }

    @Override
    public int hashCode() {
        hashCount++;
        return (firstKey << 16) + secondKey;
    }

    @Override
    public boolean equals(Object obj) {
        equalsCount++;
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FixedObjectKey other = (FixedObjectKey) obj;
        if (firstKey != other.firstKey)
            return false;
        if (secondKey != other.secondKey)
            return false;
        return true;
    }

}
