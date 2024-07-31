package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IDictionary;

import java.util.Iterator;

public class BSTDictionary<K extends Comparable<? super K>, V>
        implements IDictionary<K, V> {

    private BSTNode<K, V> root;
    private int size;

    /**
     * Class representing an individual node in the Binary Search Tree
     */
    private static class BSTNode<K, V> {
        public final K key;
        public V value;

        public BSTNode<K, V> left;
        public BSTNode<K, V> right;

        /**
         * Constructor initializes this node's key, value, and children
         */
        public BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public BSTNode(BSTNode<K, V> o) {
            this.key = o.key;
            this.value = o.value;
            this.left = o.left;
            this.right = o.right;
        }

        public boolean isLeaf() {
            return this.left == null && this.right == null;
        }

        public boolean hasBothChildren() {
            return this.left != null && this.right != null;
        }
    }

    /**
     * Initializes an empty Binary Search Tree
     */
    public BSTDictionary() {
        this.root = null;
        this.size = 0;
    }


    @Override
    public V get(K key) {
        if (this.root == null){
            return null;
        }

        BSTNode<K, V> currNode = this.root;
        while (currNode != null){
            if (currNode.key.compareTo(key) == 0){
                return currNode.value;
            }
            else if(currNode.key.compareTo(key) < 0){
                currNode = currNode.right;
            }
            else if(currNode.key.compareTo(key) > 0){
                currNode = currNode.left;
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (this.root == null){
            return null;
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (this.root == null){
            this.root = new BSTNode<>(key, value);
            this.size++;
            return null;
        }
        BSTNode<K, V> currNode = this.root;
        while (currNode != null) {
            if (currNode.key.compareTo(key) == 0) {
                V toReturn = currNode.value;
                currNode.value = value;
                return toReturn;
            }
            if (currNode.isLeaf()){
                BSTNode<K, V> toAdd = new BSTNode<>(key, value);
                if (currNode.key.compareTo(key) < 0){
                    currNode.right = toAdd;
                }
                else if (currNode.key.compareTo(key) > 0){
                    currNode.left = toAdd;
                }
                size++;
                return null;
            }
            else if(currNode.hasBothChildren()){
                if(currNode.key.compareTo(key)  < 0){
                    currNode = currNode.right;
                }
                else if(currNode.key.compareTo(key)> 0){
                    currNode = currNode.left;
                }
            }
            else{
                if (currNode.key.compareTo(key) < 0){
                    if (currNode.right != null) {
                        currNode = currNode.right;
                    }
                    else{
                        currNode.right = new BSTNode<>(key, value);
                        size++;
                        return null;
                    }
                }
                else if(currNode.key.compareTo(key) > 0){
                    if (currNode.left != null) {
                        currNode = currNode.left;
                    }
                    else{
                        currNode.left = new BSTNode<>(key, value);
                        size++;
                        return null;
                    }
                }
            }

        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        return this.keys().contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of key/value pairs in the BST
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        IDeque<K> keys = new ArrayDeque<>();
        fillKeys(keys, this.root);
        return keys;
    }

    private void fillKeys(ICollection keys, BSTNode<K, V> currNode){
        if (currNode != null && currNode.isLeaf()){
            keys.add(currNode.key);
            return;
        }
        if (currNode != null) {
            keys.add(currNode.key);
            fillKeys(keys, currNode.left);
            fillKeys(keys, currNode.right);
        }
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> values = new ArrayDeque<>();
        for (K key: this.keys()){
            values.add(this.get(key));
        }
        return values;
    }

    /**
     * Implementation of an iterator over the BST
     */

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }

    @Override
    public String toString() {
        if (this.root == null) {
            return "{}";
        }

        StringBuilder contents = new StringBuilder();

        IQueue<BSTNode<K, V>> nodes = new ArrayDeque<>();
        BSTNode<K, V> current = this.root;
        while (current != null) {
            contents.append(current.key + ": " + current.value + ", ");

            if (current.left != null) {
                nodes.enqueue(current.left);
            }
            if (current.right != null) {
                nodes.enqueue(current.right);
            }

            current = nodes.dequeue();
        }

        return "{" + contents.toString().substring(0, contents.length() - 2) + "}";
    }
}
