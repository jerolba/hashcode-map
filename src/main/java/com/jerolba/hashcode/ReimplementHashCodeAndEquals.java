package com.jerolba.hashcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * What happens if we reimplement equals and hashCode?
 *
 */
public class ReimplementHashCodeAndEquals {

    public static class Person {

        private Integer id;
        private String name;

        public Person(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Person other = (Person) obj;
            return Objects.equals(id, other.id);
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
