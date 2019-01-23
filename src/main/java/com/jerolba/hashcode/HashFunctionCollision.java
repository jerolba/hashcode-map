package com.jerolba.hashcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Simulates the same code present in HashMap
 * to calculate the number of hash collisions
 *
 */
public class HashFunctionCollision {

    private static int rows = 50000;
    private static int cols = 1000;

    public static void main(String[] args) {
        System.out.println("Histogram collision for Objects::hash function");
        hashHistogram(Objects::hash);

        System.out.println("Histogram collision for '(i << 16) + j' function");
        hashHistogram(HashFunctionCollision::simpleHash);
    }

    /**
     * Optimized Hash function to test
     * @param i first key of the composited object
     * @param j second key of the composited object
     * @return the calculated hash code
     */
    public static int simpleHash(int i, int j) {
        return (i << 16) + j;
    }

    /**
     * HashMap applies an other transformation to te returned hashCode value.
     * Because is private to the class we can not access.
     * Extracted from HashMap#hash.
     * @param key to apply the hashCode
     * @return the final hash value to use in the HashMap
     */
    static final int hash(int hashCode) {
        int h;
        return (h = hashCode) ^ (h >>> 16);
    }

    /**
     * Calculates the Histogram of collisions using a hash function over a composite key
     * @param hashFunction to apply
     */
    public static void hashHistogram(BiFunction<Integer, Integer, Integer> hashFunction) {
        Map<Integer, Integer> histogram = new HashMap<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int hash = hashFunction.apply(i, j);
                histogram.compute(hash, (k, v) -> (v == null) ? 1 : v + 1);
            }
        }
        Map<Integer, Integer> metaHistogram = new HashMap<>();
        histogram.forEach((k, v) -> metaHistogram.compute(v, (count, times) -> (times == null) ? 1 : times + 1));
        metaHistogram.forEach((k, v) -> System.out.println("Hash values with " + k + " collisions: " + v));
    }

}
