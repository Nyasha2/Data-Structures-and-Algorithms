package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROW_FACTOR = 2;
    private E[] data;
    private int size;

    public ArrayDeque(){
        this(DEFAULT_CAPACITY);
    }

    public ArrayDeque(int initialCapacity){
        this.data = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    @Override
    public void addFront(E e) {
        if (this.size == 0){
            this.data[0] = e;
            this.size++;
        }
        else {
            E backElement = this.peekBack();
            for (int i = this.size - 1; i > 0; i--) {
                this.data[i] = this.data[i - 1];
            }
            this.data[0] = e;
            this.addBack(backElement);
        }
    }

    @Override
    public void addBack(E e) {
        if (this.size == this.data.length){
            E[] newData = (E[]) new Object[this.data.length * GROW_FACTOR];
            System.arraycopy(this.data, 0, newData, 0, this.data.length);
            this.data = newData;
        }
        this.data[this.size] = e;
        this.size++;
    }

    @Override
    public E removeFront() {
        if (this.size == 0) return null;
        E frontElement = this.data[0];
        for(int i = 0; i < this.size - 1; i++){
            this.data[i] = this.data[i+1];
        }
        this.data[this.size - 1] = null;
        this.size--;
        return frontElement;
    }

    @Override
    public E removeBack() {
        if (this.size == 0) return null;
        E backElement = this.data[this.size - 1];
        this.data[this.size - 1] = null;
        this.size--;
        return backElement;
    }

    @Override
    public boolean enqueue(E e) {
        this.addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        return this.removeBack();
    }

    @Override
    public boolean push(E e) {
        this.addBack(e);
        return true;
    }

    @Override
    public E pop() {
        return this.removeBack();
    }

    @Override
    public E peekFront() {
        if (this.size == 0) return null;
        return this.data[0];
    }

    @Override
    public E peekBack() {
        if (this.size == 0) return null;
        return this.data[this.size - 1];
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() { return new ArrayDequeIterator();}

    private class ArrayDequeIterator implements Iterator<E> {
        private int idx;
        public ArrayDequeIterator() {
            this.idx = 0;
        }

        @Override
        public boolean hasNext() {
            return this.idx < size();
        }

        @Override
        public E next() {
            E toReturn = data[this.idx];
            this.idx++;
            return toReturn;
        }
    }

    @Override
    public int size() { return this.size;}

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        }

        String result = "[";
        for (int i = 0; i < this.size; i++) {
            result += this.data[i] + ", ";
        }

        result = result.substring(0, result.length() - 2);
        return result + "]";
    }

}

