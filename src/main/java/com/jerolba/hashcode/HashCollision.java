package com.jerolba.hashcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Entry point to execute the experiments
 *
 */
public class HashCollision {

    static int rows = 5_000;
    static int cols = 100;

    private ObjectConstructor constructor;

    public static void main(String[] args) {
        if (args.length > 0) {
            rows = Integer.parseInt(args[0]);
            cols = Integer.parseInt(args[1]);
        }

        System.out.println("Running for " + rows + "x" + cols);

        HashCollision defaultHash = new HashCollision(MyObject::new);
        defaultHash.nodeMemory();
        System.out.println("Hash count: " + MyObjectKey.hashCount);
        System.out.println("Equals count: " + MyObjectKey.equalsCount);

        HashCollision improvedHash = new HashCollision(FixedObject::new);
        improvedHash.nodeMemory();
        System.out.println("Hash count: " + FixedObjectKey.hashCount);
        System.out.println("Equals count: " + FixedObjectKey.equalsCount);
    }

    public HashCollision(ObjectConstructor constructor) {
        this.constructor = constructor;
    }

    /**
     * Execute the load of all objects in map and trace the memory consumption of
     * the Top 40 classes
     */
    private void nodeMemory() {
        long start = System.nanoTime();
        Map<Object, HasKey> index = new HashMap<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                HasKey myInstance = constructor.create(i, j, "foo");
                index.put(myInstance.getKey(), myInstance);
            }
        }
        long end = System.nanoTime();
        System.out.println("Milliseconds to create map: " + (end - start) / (1000 * 1000));
        execHistogram().stream().limit(40).forEach(System.out::println);
    }

    /**
     * Call to the jmap command with the current process PID to get a histogram of
     * memory consumption. The command forces to execute a GC before profiling
     * memory to ensure that we only get live objects.
     * The standar output is captured and returned as a String collection.
     *
     * @return
     */
    private List<String> execHistogram() {
        List<String> res = new ArrayList<>();
        try {
            String name = ManagementFactory.getRuntimeMXBean().getName();
            String PID = name.substring(0, name.indexOf("@"));
            Process p = Runtime.getRuntime().exec("jmap -histo:live " + PID);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = input.readLine()) != null) {
                res.add(line);
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
