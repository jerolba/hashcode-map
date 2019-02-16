package com.jerolba.hashcode;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class HashMapInspector {
    
    private final Field tableField;
    private Field nextField = null;
    private Field leftField = null;
    private Field rightField = null;
    
    public HashMapInspector() throws Exception {
        tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
    }

    public void inspect(HashMap<?,?> map) {
        try {
            Object[] table = (Object[]) tableField.get(map);
            int length = table.length;
            System.out.println("Table size: " + length);

            int empty = 0;
            int cNode = 0;
            int cTree = 0;
            for (Object nodes : table) {
                if (nodes != null) {
                    Class<? extends Object> class1 = nodes.getClass();
                    String name = class1.getName();
                    if (name.equals("java.util.HashMap$Node")) {
                        cNode++;
                    } else {
                        cTree++;
                    }
                } else {
                    empty++;
                }
            }
            System.out.println("Empty items: " + empty);
            System.out.println("Nodes: "+ cNode);
            System.out.println("TreeNodes: "+ cTree);

            //NODES
            Map<Integer, Integer> histogramNodes = new HashMap<>();
            
            for (Object node : table) {
                if (node != null) {
                    Class<? extends Object> class1 = node.getClass();
                    String name = class1.getName();
                    if (name.equals("java.util.HashMap$Node")) {
                        if (nextField == null) {
                            nextField = class1.getDeclaredField("next");
                            nextField.setAccessible(true);
                        }
                        Object it = node;
                        int contIt = 0;
                        while (it!=null) {
                            it = nextField.get(it);
                            contIt++;
                        }
                        histogramNodes.compute(contIt, (k, v) -> (v == null) ? 1 : v + 1);
                    }
                }
            }
            //Tree
            Map<Integer, Integer> histogramTree = new HashMap<>();
            for (Object node : table) {
                if (node != null) {
                    Class<? extends Object> class1 = node.getClass();
                    String name = class1.getName();
                    if (!name.equals("java.util.HashMap$Node")) {
                        if (leftField == null) {
                            leftField = class1.getDeclaredField("left");
                            leftField.setAccessible(true);
                            rightField = class1.getDeclaredField("right");
                            rightField.setAccessible(true);
                        }
                        int contIt = contTree(node);
                        histogramTree.compute(contIt, (k, v) -> (v == null) ? 1 : v + 1);
                    }
                }
            }
            System.out.println("Nodes histogram:" + histogramNodes);
            System.out.println("TreeNodes histogram: "+ histogramTree);
        } catch (Exception e) {
            e.printStackTrace();
        }        
    }
    
    private int contTree(Object treeNode) throws Exception {
        if (treeNode==null) {
            return 0;
        }
        Object left = leftField.get(treeNode);
        Object right = rightField.get(treeNode);
        return contTree(left) + contTree(right) + 1;
    }
}
