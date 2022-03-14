package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K,V>{
    private BSTNode root;
    private int size;

    @Override
    public Iterator<K> iterator() {
        return null;
    }

    private class BSTNode{
        BSTNode l; //left nodes
        BSTNode r; //right nodes
        K key;
        V value;

        private BSTNode(K k, V v){
            key = k;
            value = v;
        }
    }


    @Override
    public void clear() {
        root = null;
        size = 0;
    }


    public boolean containsKey(K key) {
        if (root == null) {
            return false;
        }
        return getHelper(key, root) != null;
    }


    public V get(K key) { // recursive
        BSTNode result = getHelper(key, root);
        if (result == null) {
            return null;
        }
        return result.value;
    }

    private BSTNode getHelper(K key, BSTNode n){
        if(n == null){
            return null;
        }

        int compareValue = key.compareTo(n.key); // return a value
        if(compareValue < 0){
            return getHelper(key, n.l); // goes left because left have smaller value than right side
        } else if (compareValue > 0) {
            return getHelper(key, n.r);
        } else {
            return n;
        }

    }


    @Override
    public int size() {
        return size;
    }


    public void put(K key, V value) { // helper
        root = putHelper(key, value, root);

    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    private BSTNode putHelper(K key, V value, BSTNode n){
        if(n == null){
            size++;
            return new BSTNode(key, value); // if there is an empty space, then put the node there
        }

        int compareValue = key.compareTo(n.key); // return a value
        if(compareValue < 0){
            n.l = putHelper(key, value,n.l); // goes left because left have smaller value than right side
        } else if (compareValue > 0) {
            n.r = putHelper(key, value, n.r);
        } else {
            n.value = value;
        }
        return n;

    }

    public void printInOrder(){ // helper from root
        printInOrder(root);
        System.out.println();
    }

    private void printInOrder(BSTNode n){
        if (n == null) {
            return; // if the node is empty then exit
        }
        printInOrder(n.l);
        System.out.print("(" + n.key.toString() + " " + n.value.toString() + ")");
        printInOrder(n.r);
        System.out.print("(" + n.key.toString() + " " + n.value.toString() + ")");
    }
}
