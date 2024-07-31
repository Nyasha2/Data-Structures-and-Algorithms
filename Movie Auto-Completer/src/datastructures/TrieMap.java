package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.ITrieMap;


import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Iterator;


public class TrieMap<A, K extends Iterable<A>, V> implements ITrieMap<A, K, V> {
    private TrieNode<A, V> root;
    private Function<IDeque<A>, K> collector;
    private int size;

    public TrieMap(Function<IDeque<A>, K> collector) {
        this.root = null;
        this.collector = collector;
        this.size = 0;
    }
    

    @Override
    public boolean isPrefix(K key) {
        TrieNode<A, V> currNode = this.root;
        if (currNode == null){
            return false;
        }
        for (A a: key){
            if (currNode != null) {
                if (!currNode.pointers.containsKey(a)) {
                    return false;
                }
                currNode = currNode.pointers.get(a);
            }
        }
        return true;
    }

    @Override
    public ICollection<V> getCompletions(K prefix) {
        ICollection<V> completions = new ArrayDeque<>();
        if (isPrefix(prefix)){
            TrieNode<A, V> currNode = this.root;
            for (A a: prefix){
                if (currNode != null) {
                    currNode = currNode.pointers.get(a);
                }
            }
            getCompletionsHelper(completions, currNode);
        }
        return completions;
    }

    private void getCompletionsHelper(ICollection list, TrieNode<A, V> currNode){
        if (currNode != null) {
            if (currNode.value != null && !list.contains(currNode.value)) {
                list.add(currNode.value);
            }
            for (A a : currNode.pointers.keySet()) {
                getCompletionsHelper(list, currNode.pointers.get(a));
            }
        }
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public V get(K key) {
        TrieNode<A, V> currNode = this.root;
        for (A a: key){
            if (currNode !=null) {
                if (!currNode.pointers.containsKey(a)) {
                    return null;
                } else {
                    currNode = currNode.pointers.get(a);
                }
            }
        }
        if (currNode != null){
            return currNode.value;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        V toReturn = this.get(key);
        TrieNode<A, V> currNode = this.root;
        TrieNode<A, V> prevNode = null;
        if (currNode != null && currNode.value == toReturn){
            this.root.pointers.clear();
            this.root = null;
            return toReturn;
        }
        else{
            A pointer = null;
            for (A a: key){
                if (currNode != null){
                    if (!currNode.pointers.containsKey(a)) {
                        return null;
                    } else {
                        pointer = a;
                        prevNode = currNode;
                        currNode = currNode.pointers.get(a);
                    }
                }
            }
            if (getCompletions(key).size() > 1) {
                currNode.value = null;
            }
            else{
                if (prevNode != null && this.getCompletions(key).size() == 1) {
                    prevNode.pointers.remove(pointer);
                    removeHelper(key, this.root);
                }
            }

        }
        this.size--;
        return toReturn;
    }

    private void removeHelper(K key, TrieNode<A, V> currNode){
        TrieNode<A, V> prevNode = null;
        IDeque<A> keyDeque = new ArrayDeque<>();
        for (A a: key){
            keyDeque.addBack(a);
            if (currNode != null){
                 if (this.getCompletions((K)keyDeque) == null && currNode.value != null){
                    currNode.pointers.clear();
                    return;
                 }
                 else if (this.getCompletions((K)keyDeque) == null && currNode.value == null){
                    prevNode.pointers.remove(a);
                    removeHelper(key, this.root);
                    break;
                }
                prevNode = currNode;
                currNode = currNode.pointers.get(a);
            }
        }
//        for (A a: key){
//            keyDeque.addBack(a);
//            if (currNode != null) {
//                if (currNode.pointers.isEmpty() && currNode.value != null) {
//                    return;
//                } else if (currNode.pointers.isEmpty()) {
//                    prevNode.pointers.remove(a);
//                    removeHelper((K)keyDeque, this.root);
//                    break;
//                } else {
//                    prevNode = currNode;
//                    currNode = currNode.pointers.get(a);
//                }
//            }
//        }
    }

    @Override
    public V put(K key, V value) {
        if (this.root == null) {
            this.root = new TrieNode<>();
        }
        TrieNode<A, V> currNode = this.root;
        for (A a: key){
            if (!currNode.pointers.containsKey(a)) {
                TrieNode<A, V> nextNode = new TrieNode<>();
                currNode.pointers.put(a, nextNode);
            }
            currNode = currNode.pointers.get(a);
        }
        V previousValue = currNode.value;
        if (previousValue == null){
            size++;
        }
        currNode.value = value;
        return previousValue;
    }

    @Override
    public boolean containsKey(K key) {

        if (get(key) == null){
            return false;
        }
        else{
            return true;
        }

    }

    @Override
    public boolean containsValue(V value) {
        return containsValueHelper(value, this.root);
    }

    private boolean containsValueHelper(V value, TrieNode<A, V> currNode){
        if (currNode.value == value){
            return true;
        }
        for (A a: currNode.pointers.keySet()){
            if (containsValueHelper(value, currNode.pointers.get(a))){
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        ICollection<K> collectionOfKeys = new ArrayDeque<>();
        IDeque<A> semiKey = new ArrayDeque<>();
        keysHelper(collectionOfKeys, this.root, semiKey);
        return collectionOfKeys;
    }

    private void keysHelper(ICollection list, TrieNode<A, V> currNode, IDeque semiKey){
        if (currNode != null) {
            if (currNode.value != null) {
                list.add(this.collector.apply(semiKey));
            }
            for (A a : currNode.pointers.keySet()) {
                semiKey.addBack(a);
                keysHelper(list,currNode.pointers.get(a) , semiKey);
                semiKey.removeBack();
            }
        }
    }

    @Override
    public ICollection<V> values() {
        ICollection<V> collected = new ArrayDeque<>();
        valuesHelper(collected, this.root);
        return collected;
    }

    private void valuesHelper(ICollection list, TrieNode<A, V> currNode){
        if (currNode != null) {
            if (currNode.value != null) {
                list.add(currNode.value);
            }
            for (A a : currNode.pointers.keySet()) {
                valuesHelper(list, currNode.pointers.get(a));
            }
        }
    }

    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }

    
    @Override
    public String toString() {
        return this.root.toString();
    }
    
    private static class TrieNode<A, V> {
        public final Map<A, TrieNode<A, V>> pointers;
        public V value;

        public TrieNode() {
            this(null);
        }

        public TrieNode(V value) {
            this.pointers = new HashMap<>();
            this.value = value;
        }

        @Override
        public String toString() {
            StringBuilder b = new StringBuilder();
            if (this.value != null) {
                b.append("[" + this.value + "]-> {\n");
                this.toString(b, 1);
                b.append("}");
            }
            else {
                this.toString(b, 0);
            }
            return b.toString();
        }

        private String spaces(int i) {
            StringBuilder sp = new StringBuilder();
            for (int x = 0; x < i; x++) {
                sp.append(" ");
            }
            return sp.toString();
        }

        protected boolean toString(StringBuilder s, int indent) {
            boolean isSmall = this.pointers.entrySet().size() == 0;

            for (Map.Entry<A, TrieNode<A, V>> entry : this.pointers.entrySet()) {
                A idx = entry.getKey();
                TrieNode<A, V> node = entry.getValue();

                if (node == null) {
                    continue;
                }

                V value = node.value;
                s.append(spaces(indent) + idx + (value != null ? "[" + value + "]" : ""));
                s.append("-> {\n");
                boolean bc = node.toString(s, indent + 2);
                if (!bc) {
                    s.append(spaces(indent) + "},\n");
                }
                else if (s.charAt(s.length() - 5) == '-') {
                    s.delete(s.length() - 5, s.length());
                    s.append(",\n");
                }
            }
            if (!isSmall) {
                s.deleteCharAt(s.length() - 2);
            }
            return isSmall;
        }
    }
}
