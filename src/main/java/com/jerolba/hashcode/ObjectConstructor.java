package com.jerolba.hashcode;

/**
 * Functional interface to simplify defining a constructor with 3 parameters
 * pass it as parameter to the test executor
 *
 */

@FunctionalInterface
public interface ObjectConstructor {

    HasKey create(int i, int j, String foo);

}
