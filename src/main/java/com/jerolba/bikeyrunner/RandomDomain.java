package com.jerolba.bikeyrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomDomain {

    private final List<Bikey> all = new ArrayList<>();

    public RandomDomain(int rows, int cols) {
        System.out.println("Generating domain of " + rows + " x " + cols);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                all.add(new Bikey(i, j));
            }
        }
        Collections.shuffle(all);
        System.out.println("Generated");
    }

    public List<Bikey> getDomain() {
        return all;
    }

    public static class Bikey {

        public Bikey(int i, int j) {
            this.i = i;
            this.j = j;
        }

        int i;
        int j;
    }

}
