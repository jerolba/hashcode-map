package com.jerolba.hashcode;

import java.util.HashMap;

import com.jerolba.jmnemohistosyne.Histogramer;
import com.jerolba.jmnemohistosyne.MemoryHistogram;

/**
 * Entry point to execute the experiments
 *
 */
public class HashCollision {

    static int rows = 50_000;
    static int cols = 1_000;

    private ObjectConstructor constructor;

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            rows = Integer.parseInt(args[0]);
            cols = Integer.parseInt(args[1]);
        }

        System.out.println("Running for " + rows + "x" + cols);
        HashCollision improvedHash = new HashCollision(FixedObject::new);
        improvedHash.nodeMemory();
        System.out.println("Hash count: " + FixedObjectKey.hashCount);
        System.out.println("Equals count: " + FixedObjectKey.equalsCount);

        HashCollision defaultHash = new HashCollision(MyObject::new);
        defaultHash.nodeMemory();
        System.out.println("Hash count: " + MyObjectKey.hashCount);
        System.out.println("Equals count: " + MyObjectKey.equalsCount);

    }

    public HashCollision(ObjectConstructor constructor) {
        this.constructor = constructor;
    }

    /**
     * Execute the load of all objects in map and trace the memory consumption of
     * the Top 20 classes
     */
    private void nodeMemory() throws Exception {
        HashMapInspector inspector = new HashMapInspector();
        MemoryHistogram diff = Histogramer.getDiff(() -> {
            long start = System.nanoTime();
            HashMap<Object, HasKey> index = new HashMap<>();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    HasKey myInstance = constructor.create(i, j, "foo");
                    index.put(myInstance.getKey(), myInstance);
                }
            }
            long end = System.nanoTime();
            System.out.println("Milliseconds to create map: " + (end - start) / (1000 * 1000));

            inspector.inspect(index);
            return index;
        });
        System.out.println(diff.getTop(20));
    }

}
