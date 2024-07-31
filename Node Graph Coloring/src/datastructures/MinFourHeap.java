package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.IPriorityQueue;

import java.util.Iterator;

public class MinFourHeap<E> implements IPriorityQueue<E> {

    private static final int DEFAULT_CAPACITY = 10;

    private int size;
    private PQElement<E>[] data;
    private IDictionary<E, Integer> keyToIndexMap;

    /**
     * Creates a new empty heap with DEFAULT_CAPACITY.
     */
    public MinFourHeap() {
        this.size = 0;
        this.data = new PQElement[DEFAULT_CAPACITY];
        this.keyToIndexMap = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
    }

    private void pecolateDown(int Index){
        int child = 4 * Index + 1;
        while (child < this.size){
            int smallestChild = calculatechildren(child);
            if (this.data[smallestChild].priority < this.data[Index].priority){
                swap(Index, smallestChild);
                Index = smallestChild;
                child = 4 * Index +1;
            } else{
                break;
            }
        }
    }

    private void pecolateUp(int Index){
        while (Index > 0){
            int parentIndex = (Index - 1)/4;
            if (this.data[Index].priority< this.data[parentIndex].priority){
                swap(parentIndex, Index);
                Index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void swap(int element1, int element2){
        PQElement<E> temp = this.data[element1];
        this.data[element1] = this.data[element2];
        this.data[element2] = temp;
        this.keyToIndexMap.put(this.data[element1].data, element1);
        this.keyToIndexMap.put(this.data[element2].data, element2);
    }

    private int calculatechildren(int index){
        int smallestChild = index;
        for (int i = 1; i <= 4; i ++){
            int child = index + i;
            if (child < this.size && this.data[child].priority <this.data[smallestChild].priority){
                smallestChild = child;
            }
        }
        return smallestChild;
    }

    @Override
    public void increaseKey(PQElement<E> key) {
        if (!this.keyToIndexMap.containsKey(key.data)){
            throw new IllegalArgumentException();
        }
        int index =this.keyToIndexMap.get(key.data);
        PQElement<E> current = this.data[index];
        if (key.priority < current.priority){
            throw new IllegalArgumentException();
        }
        this.data[index] = key;
        pecolateDown(index);
    }

    @Override
    public void decreaseKey(PQElement<E> key) {
        if (!this.keyToIndexMap.containsKey(key.data)){
            throw new IllegalArgumentException();
        }

        int index =this.keyToIndexMap.get(key.data);
        PQElement<E> current = this.data[index];
        if (key.priority > current.priority){
            throw new IllegalArgumentException();
        }
        this.data[index] = key;
        pecolateUp(index);
    }

    @Override
    public boolean enqueue(PQElement<E> epqElement) {
        if (this.keyToIndexMap.containsKey(epqElement.data)){
            throw new IllegalArgumentException();
        }
        if (this.size == this.data.length){
            PQElement<E>[] data = new PQElement[2 * this.data.length];
            System.arraycopy(this.data, 0, data, 0, this.data.length);
            this.data = data;
        }
        this.data[this.size] = epqElement;
        this.keyToIndexMap.put(epqElement.data, this.size);
        pecolateUp(this.size);
        this.size++;
        return true;
    }

    @Override
    public PQElement<E> dequeue() {
        if (this.size == 0){
            return null;
        }
        PQElement<E> toReturn = this.data[0];
        this.data[0] = this.data[this.size - 1];
        this.keyToIndexMap.put(this.data[0].data, 0);
        this.keyToIndexMap.remove(toReturn.data);
        this.size--;
        pecolateDown(0);
        return toReturn;
    }

    @Override
    public PQElement<E> peek() {
        if (this.size == 0){
            return null;
        }
        return this.data[0];
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Iterator<PQElement<E>> iterator() {
        return new heapIterator();
    }

    private class heapIterator implements Iterator<PQElement<E>> {
        private int index;
        public heapIterator() {
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < size();
        }

        @Override
        public PQElement next() {
            PQElement toReturn = data[this.index];
            this.index++;
            return toReturn;
        }
    }
}
