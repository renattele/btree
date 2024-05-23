package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Consumer;

public class Main {
    private static long measure(Runnable r) {
        long start = System.nanoTime();
        r.run();
        long end = System.nanoTime();
        return end - start;
    }

    private static void genFiles() throws IOException {
        File dir = new File("assets");
        dir.mkdir();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            new File("assets/" + i + ".txt").createNewFile();
            PrintStream f = new PrintStream("assets/" + i + ".txt");
            for (int index = 0; index < (i + 1) * 100; index++) {
                f.println(random.nextInt() + " " + random.nextInt());
            }
            f.close();
        }
    }

    private static void readFiles(Consumer<Map<Integer, Integer>> c) throws FileNotFoundException {
        File dir = new File("assets");
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            Map<Integer, Integer> m = new HashMap<>();
            Scanner sc = new Scanner(file);
            while (sc.hasNext()) {
                m.put(sc.nextInt(), sc.nextInt());
            }
            c.accept(m);
        }
    }

    public static void main(String[] args) throws IOException {
        readFiles(map -> {
            Tree<Integer, Integer> tree = new BTree<>();
            long putTime = measure(() -> map.forEach(tree::put));
            long searchTime = measure(() -> map.forEach((key, value) -> tree.get(value)));
            long deleteTime = measure(() -> map.forEach((key, value) -> tree.delete(key)));
            System.out.println(putTime);
        });
    }
}