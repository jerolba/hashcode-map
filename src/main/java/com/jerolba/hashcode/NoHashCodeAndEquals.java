package com.jerolba.hashcode;

import java.util.HashMap;
import java.util.Map;

/**
 * What happens if we don't reimplement equals and hashCode?
 *
 */
public class NoHashCodeAndEquals {

    public static class Person {

        private Integer id;
        private String name;

        public Person(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

    }

    public static void main(String[] args) {
        Map<Person, Integer> map = new HashMap<>();
        map.put(new Person(1, "Alberto"), 35);
        map.put(new Person(2, "Ana"), 28);
        // more operations...
        map.put(new Person(1, "Alberto"), 36);
        System.out.println(map.size());
    }

}
