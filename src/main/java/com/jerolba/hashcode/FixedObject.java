package com.jerolba.hashcode;

/**
 * Object with hashCode function fixed. Duplicated structure to allow
 * comparations between incorrect one.
 *
 */
public class FixedObject implements HasKey {

    private FixedObjectKey key;
    private String someAttribute;

    public FixedObject(int firstKey, int secondKey, String someAttribute) {
        this.key = new FixedObjectKey(firstKey, secondKey);
        this.someAttribute = someAttribute;
    }

    @Override
    public FixedObjectKey getKey() {
        return key;
    }

    public String getSomeAttribute() {
        return someAttribute;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FixedObject other = (FixedObject) obj;
        return this.key.equals(other.key);
    }

}
