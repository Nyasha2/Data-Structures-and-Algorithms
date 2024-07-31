package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.ICollection;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IQueue;

import java.util.Iterator;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ChainingHashDictionary<K, V> implements IDictionary<K, V> {
    private Supplier<IDictionary<K, V>> chain;
    private int size;
    private IDictionary<K, V>[] hashTable;
    private int capacity;
    private static int[] PRIMES = {11, 23, 47, 97, 197, 397, 797,1597, 3203, 6421, 12841, 25693, 25693, 51407,102829, 205661, 411337, 500009};

    public ChainingHashDictionary(Supplier<IDictionary<K, V>> chain) {
        this.hashTable = new IDictionary[11];
        this.capacity = 11;
        this.size = 0;
        this.chain = chain;
    }

    private void resizeIfNecessary(){
        if (this.size >= 0.7 * this.capacity){
            for (int i = 0; i < 17; i ++){
                if (PRIMES[i] > this.capacity){
                    IDictionary<K, V>[] newTable = new IDictionary[PRIMES[i]];
                    for (K key: this.keys()){
                        int Index = key.hashCode() % PRIMES[i];
                        if (Index < 0){
                            Index += PRIMES[i];
                        }
                        if (newTable[Index] == null){
                            newTable[Index] = chain.get();
                        }
                        newTable[Index].put(key, this.get(key));
                    }
                    this.capacity = PRIMES[i];
                    this.hashTable = newTable;
                    return;
                }
            }
        }
    }

    /**
     * @param key
     * @return value corresponding to key
     */
    @Override
    public V get(K key) {
        int Index = key.hashCode() % this.capacity;
        if (Index < 0){
            Index += this.capacity;
        }
        if (this.hashTable[Index]== null){
            return null;
        }
        return this.hashTable[Index].get(key);
    }

    @Override
    public V remove(K key) {

        int Index = key.hashCode() % this.capacity;
        if (Index < 0){
            Index += this.capacity;
        }
        if (this.hashTable[Index] != null) {
            V toReturn = this.hashTable[Index].remove(key);
            if (toReturn != null){
                size--;
            }
            return toReturn;
        }
        return null;
    }

    @Override
    public String toString(){
        return "";
    }

    @Override
    public V put(K key, V value) {
        resizeIfNecessary();
        int Index = key.hashCode() % this.capacity;
        if (Index < 0){
            Index += this.capacity;
        }
        if (this.hashTable[Index] == null){
            this.hashTable[Index] = this.chain.get();
        }
        V toReturn = this.hashTable[Index].put(key, value);
        if (toReturn == null){
            this.size++;
        }
        return toReturn;

    }

    @Override
    public boolean containsKey(K key) {
        return this.keys().contains(key);
    }

    /**
     * @param value
     * @return true if the HashDictionary contains a key-value pair with
     * this value, and false otherwise
     */
    @Override
    public boolean containsValue(V value) {
        return this.values().contains(value);
    }

    /**
     * @return number of key-value pairs in the HashDictionary
     */
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public ICollection<K> keys() {
        IDeque<K> toReturn = new ArrayDeque<>();
        for (int i = 0; i < this.capacity; i++){
            if (this.hashTable[i] != null){
                toReturn.addAll(this.hashTable[i].keys());
            }
        }
        return toReturn;
    }

    @Override
    public ICollection<V> values() {
        IDeque<V> toReturn = new ArrayDeque<>();
        for (int i = 0; i < this.capacity; i++){
            if (this.hashTable[i] != null){
                toReturn.addAll(this.hashTable[i].values());
            }
        }
        return toReturn;
    }

    /**
     * @return An iterator for all entries in the HashDictionary
     */
    @Override
    public Iterator<K> iterator() {
        return keys().iterator();
    }
}
