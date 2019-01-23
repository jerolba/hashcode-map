package com.jerolba.hashcode;

/**
 * Describes an object which has a composite key, used to provide multiple
 * implementations in HashCollision
 *
 */
public interface HasKey {

    Object getKey();
}
