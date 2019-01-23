package com.jerolba.hashcode;

/**
 * Object with hashCode function that uses the classic hash calculation on the
 * composite key
 *
 */
public class MyObject implements HasKey {

    private MyObjectKey key;
    private String someAttribute;

    public MyObject(int firstKey, int secondKey, String someAttribute) {
        this.key = new MyObjectKey(firstKey, secondKey);
        this.someAttribute = someAttribute;
    }

    @Override
    public MyObjectKey getKey() {
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
        MyObject other = (MyObject) obj;
        return this.key.equals(other.key);
    }

}
