package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IFixedSizeQueue;

import java.util.Iterator;

public class CircularArrayFixedSizeQueue<E> implements IFixedSizeQueue<E> {
    private int front;
    private E[] data;
    private int size;
    public CircularArrayFixedSizeQueue(int capacity) {
        this.data = (E[]) new Object[capacity];
        size = 0;
    }

    @Override
    public String toString(){
        if (this.size == 0){
            return "[]";
        }
        else{
            String result = "[";
            for (int i = this.front; i < this.front + this.size; i ++ ){
                result += this.data[i] + ", ";
            }
            result = result.substring(0, result.length() - 2);
            return result + "]";
        }
    }
    @Override
    public boolean isFull() {
        if (this.size == this.data.length){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int capacity() {
        return this.data.length;
    }

    @Override
    public boolean enqueue(E e) {
        if (!this.isFull()){
            this.add(e);
            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public E dequeue() {
        if (size == 0){
            return null;
        }
        else{
            E toReturn = this.data[this.front];
            this.data[this.front] = null;
            if (front == this.capacity() - 1){
                this.front = 0;
            }
            else {
                this.front++;
            }
            this.size--;
            return toReturn;
        }
    }

    @Override
    public E peek() {
        if (size == 0){
            return null;
        }
        else{
            return this.data[this.front];
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void add(E e) {
        if (!this.isFull()){
            if (size == 0){
                this.data[0] = e;
                this.front = 0;
            }
            else if (this.front + this.size >= this.data.length){
               int location = (front + size) % capacity();
               this.data[location] = e;
            }
            else{
                this.data[front + size] = e;
            }
            this.size++;

        }
    }

    @Override
    public void clear() {
        for (int i = this.front; i < this.data.length; i++){
            this.data[i] = null;
        }
        this.size = 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new CircularArrayIterator();
    }

    private class CircularArrayIterator implements Iterator<E> {
        private int index;

        public CircularArrayIterator() {
            this.index = front;
        }

        @Override
        public boolean hasNext() {
            if (this.index < capacity()){
                return data[this.index] != null;
            }
            return false;
        }

        @Override
        public E next() {
            E toReturn = data[this.index];
            if (this.index < capacity() - 1){
                this.index++;
                if (this.index == front){
                    this.index = capacity() + 1;
                }
            }
            else if(this.index == capacity() - 1){
                this.index = 0;
                if (this.index == front){
                    this.index = capacity() + 1;
                }
            }
            return toReturn;
        }
    }
}

