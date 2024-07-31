package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class ArrayDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private E[] data;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;
    private static final int GROW_FACTOR = 2;

    public ArrayDeque(int initialCapacity){
        this.data = (E[]) new Object[initialCapacity];
        this.size = 0;
    }

    public ArrayDeque(){
        this(DEFAULT_CAPACITY);
    }

    //ensure that the capacity of the arraydeque is always not less than the size.
    private void ensureCapacity(int size){
        if (this.data.length < size){
            E[] newData = (E[]) new Object[size*GROW_FACTOR];
            for (int i = 0; i <this.size; i ++){
                newData[i] = this.data[i];
            }
            this.data = newData;
        }
    }

    @Override
    public String toString(){
        if (this.isEmpty()){
            return"[]";
        }

        String result = "[";
        for (int i = 0; i < this.size; i++){
            result += this.data[i] + ", ";
        }
        result = result.substring(0, result.length() - 2);
        return result + "]";
    }
    @Override
    public void addFront(E e) {
        this.ensureCapacity(size +1);
        for (int i = this.size; i > 0; i--){
            this.data[i] = this.data[i-1];
        }
        this.data[0] = e;
        this.size++;

    }

    // to add to the back of the list, move everything one step forward and put the element you want to add at index 0.
    @Override
    public void addBack(E e) {

        this.ensureCapacity(size + 1);
        this.data[this.size] = e;
        this.size++;
    }

    @Override
    public E removeFront() {
        // first check if the deque is empty, in which case you should return null
        if (this.isEmpty()){
            return null;
        }
        else {
            // create a new instance of the class, copy everything except the last element

            E front = this.data[0];
            for (int i = 0; i < this.size-1; i++) {
                this.data[i] = this.data[i+1];
            }
            this.size--;
            return front;
        }
    }

    @Override
    public E removeBack() {
        //first check if the deque is not empty, in which case you should return null
        if (this.isEmpty()){
            return null;
        }
        else {
            // create a new instance of the class, copy everything except the last element

            E back = this.data[this.size - 1];
            this.data[this.size -1 ] = null;
            this.size--;
            return back;
        }
    }

    //WE HAVE alot of mistakes in the below code
    @Override
    public boolean enqueue(E e) {
        this.addFront(e);
        if (this.contains(e)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public E dequeue() {
        return removeBack();
    }

    @Override
    public boolean push(E e) {
        //adds an element to the top of the stack
        //this.data[this.size] = e;
        //if (this.contains(e))
        //{
            //this.size++;
            //return true;
        //}
        //else{
            //return false;
        //}
        this.addBack(e);
        if (this.contains(e)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public E pop() {
        //access the last item in the arrayDeque
        return removeBack();
    }

    @Override
    public E peekFront() {
        //first check if the deque is not empty
        if (this.isEmpty()){
            return null;
        }
        else {
            return this.data[0];
        }
    }

    @Override
    public E peekBack() {
        //first check if the deque is not null
        if (!this.isEmpty()) {
            return this.data[this.size -1];
        }
        return null;
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<E> {
        private int index;
        public ArrayDequeIterator() {
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < size();
        }

        @Override
        public E next() {
            E toReturn = data[this.index];
            this.index++;
            return toReturn;
        }
    }



    @Override
    public int size() {
        return this.size;
    }
}

