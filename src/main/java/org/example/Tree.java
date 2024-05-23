package org.example;

public interface Tree<K, V> {
    void put(K key, V value);
    V get(K key);
    int size();
    void delete(K key);
}
