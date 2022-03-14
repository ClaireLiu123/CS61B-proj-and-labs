package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private HashSet<K> keySet;
    private static final int defaultInitialSize = 16;
    private static final double defaultLoadFactor = 0.75;
    private double loadFactor;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(defaultInitialSize,defaultLoadFactor);

    }

    public MyHashMap(int initialSize) {
        this(initialSize,defaultLoadFactor);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = new Collection[initialSize];
        loadFactor = maxLoad;
        size = 0;
        keySet = new HashSet<>();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new ArrayList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] a= new Collection[tableSize];
        return a;
    }

    @Override
    public void clear(){
        size = 0;
        buckets = createTable(defaultInitialSize);
    }

    @Override
    public boolean containsKey(K key){
        return keySet.contains(key);
    }

    private Node getNode(K key){
        int index = findB(key);
        Collection<Node> bList = buckets[index];
        if (bList != null) {
            for (Node n : bList) {
                if (n.key.equals(key)) {
                    return n;
                }
            }
        }
        return null;
    }

    private int findB(K key) {
        return findB(key, buckets.length);
    }

    private int findB(K key, int targetSize) {
        return Math.floorMod(key.hashCode(), targetSize);
    }

    /**
     * Check if the key is mapped then
     * Returns the value to which
     * if the map contains no mapping for the key
     * then return null
     */
    @Override
    public V get(K key){
        Node a = getNode(key);
        if (a == null) {
            return null;
        }
        return a.value;
    }

    private void bucketAgain(int targetSize){
        Collection<Node>[] newB = createTable(targetSize);
        for (K key : keySet) {
            int index  = findB(key, targetSize);
            if (newB[index] == null) {
                newB[index] = createBucket();
            }
            newB[index].add(getNode(key));
        }
        buckets = newB;
    }
    /**
     * Returns the number of key-value mappings in this map.
     * which will be the size of this map
     */
    @Override
    public int size(){
        return size;
    }

    /**
     * map the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value will be replaced.
     */
    @Override
    public void put(K key, V value) {
        Node n = getNode(key);
        if (n != null) {
            n.value = value;
            return;
        }
        if (((double) size) / buckets.length > loadFactor) {
            bucketAgain(buckets.length * 2);
        }
        size += 1;
        keySet.add(key);
        int index = findB(key);
        Collection<Node> BList = buckets[index];
        if (BList == null) {
            BList = createBucket();
            buckets[index] = BList;
        }
        BList.add(createNode(key, value));
    }

    /**
     * Returns a Set of the keys contained in this map.
     */
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    /**
     * Removes the mapping for the specified
     * key from this map if present. */
    @Override
    public V remove(K key) {
        return remove(key,null);
    }

    /**
     * Removes the entry for this key
     * only if the key is mapped to the current
     * specified value. If don't implement this, throw an
     * UnsupportedOperationException.
     */
    @Override
    public V remove(K key, V value) {
        int i = findB(key);
        Collection<Node> b = buckets[i];
        if (b!=null){
            for (Node n : b){
                if (n.key.equals(key) && (value == null || n.value.equals(value))){
                    b.remove(n);
                    size -= 1;
                    keySet.remove(key);
                    return n.value;
                }
            }
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet.iterator();
    }





}
