package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDictionary;
import java.util.Iterator;


public class MoveToFrontDictionary<K, V> implements IDictionary<K,V> {
    private Node<K, V> root;
    private int size;

    public MoveToFrontDictionary() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V remove(K key) {
        V value = get(key);
        if (this.root == null) {
            return null;
        }
        if (value != null) {
            this.root = this.root.Next;
            size--;
        }
        return value;
    }

    @Override
    public V put(K key, V value) {
//        if (size == 0){
//            DictNode<K, V> toAdd = new DictNode<>();
//            toAdd.put(key, value);
//            this.root = toAdd;
//            size++;
//            return null;
//        }
//        if (this.root == null){
//            this.root = new DictNode<>(key, value);
//            this.size++;
//            return null;
//        }
        V toReturn = this.get(key);
        if (toReturn == null){
            Node<K, V> toAdd = new Node<>(key, value, null);
            toAdd.Next = this.root;
            this.root = toAdd;
            size++;
            return null;
        }
        this.root.value = value;
        return toReturn;
    }

    @Override
    public boolean containsKey(K key) {
        return this.get(key) != null;
    }

    @Override
    public String toString(){
        return "";
    }

    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection dictKeys = new ArrayDeque();
        Node currNode = this.root;
        while (currNode != null && currNode.key != null){
            dictKeys.add(currNode.key);
            currNode = currNode.Next;
        }
        return dictKeys;
    }

    @Override
    public ICollection<V> values() {
        ICollection dictValues = new ArrayDeque();
        Node currNode = this.root;
        while (currNode != null && currNode.value != null){
            dictValues.add(currNode.value);
            currNode = currNode.Next;
        }
        return dictValues;
    }

    public V get(K key) {
        if (this.root == null){
            return null;
        }
//        if (this.size() == 1){
//            return this.root.value;
//        }

//            new DictNode<>();
        Node<K, V> prevNode;
        prevNode = null;
        Node<K, V> currNode;
        currNode = this.root;
        if (this.root.key.equals(key)){
            return this.root.value;
        }
        while (currNode != null){
            if (key.equals(currNode.key)){
                V toReturn = currNode.value;
                prevNode.Next = currNode.Next;
                currNode.Next = this.root;
                this.root = currNode;
                return toReturn;
            }
            prevNode = currNode;
            currNode = currNode.Next;
        }
        return null;

    }

    @Override
    public Iterator<K> iterator() {
        return this.keys().iterator();
    }
//
//    private class iterator<K> implements Iterator<K>{
//
//        private K key;
//        private DictNode<V> thisNode;
//
//        public iterator(){
//            this.key = root.getKey();
//            this.thisNode =root;
//        }
//
//        public boolean hasNext(){
//            if (this.thisNode == null){
//                return false;
//            }
//            return true;
//        }
//
//        public K next(){
//            K toReturn = this.key;
//            this.thisNode = this.thisNode.Next;
//            this.key = this.thisNode.getKey();
//            return toReturn;
//        }
//
//    }




//        public K getKey(){
//            return this.key;
//        }
//
//        public V getValue(K key){
//            if (key == this.key){
//                return this.value;
//            }
//            else return null;
//        }
    private static class Node<K, V> {
        private final K key;
        private Node<K, V> Next;
        private V value;

        public Node(K key, V value, Node<K, V> Next) {
            this.key = key;
            this.value = value;
            this.Next = Next;
        }
    }
}
