package org.example;

public class BTree<K extends Comparable<K>, V> implements Tree<K, V> {
    private static final int T = 4;

    private Node root;
    private int height;
    private int size;

    private static final class Node {
        private int m;
        private Entry[] children = new Entry[T];

        private Node(int k) {
            m = k;
        }
    }

    private static class Entry {
        private Comparable key;
        private final Object value;
        private Node next;

        public Entry(Comparable key, Object value, Node next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public BTree() {
        root = new Node(0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        return find(root, key, height);
    }

    @Override
    public void delete(K key) {
        put(key, null);
    }

    @Override
    public void put(K key, V val) {
        Node newRoot = insert(root, key, val, height);
        size++;
        if (newRoot == null) return;

        Node rawRoot = new Node(2);
        rawRoot.children[0] = new Entry(root.children[0].key, null, root);
        rawRoot.children[1] = new Entry(newRoot.children[0].key, null, newRoot);
        root = rawRoot;
        height++;
    }

    private V find(Node x, K key, int height) {
        Entry[] children = x.children;

        if (height == 0) {
            for (int j = 0; j < x.m; j++) {
                if (((Comparable) key).compareTo(children[j].key) == 0) return (V) children[j].value;
            }
        }

        else {
            for (int j = 0; j < x.m; j++) {
                if (j + 1 == x.m || ((Comparable) key).compareTo(children[j + 1].key) < 0)
                    return find(children[j].next, key, height - 1);
            }
        }
        return null;
    }

    private Node insert(Node node, K key, V value, int height) {
        int j;
        Entry t = new Entry(key, value, null);

        if (height == 0) {
            for (j = 0; j < node.m; j++) {
                if (((Comparable) key).compareTo(node.children[j].key) < 0) break;
            }
        }

        else {
            for (j = 0; j < node.m; j++) {
                if ((j + 1 == node.m) || ((Comparable) key).compareTo(node.children[j + 1].key) < 0) {
                    Node u = insert(node.children[j++].next, key, value, height - 1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = node.m; i > j; i--)
            node.children[i] = node.children[i - 1];
        node.children[j] = t;
        node.m++;
        if (node.m < T) return null;
        else return split(node);
    }

    private Node split(Node node) {
        Node t = new Node(T / 2);
        node.m = T / 2;
        for (int j = 0; j < T / 2; j++)
            t.children[j] = node.children[T / 2 + j];
        return t;
    }
}