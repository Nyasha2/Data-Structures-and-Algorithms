package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {

    private Node<E> head;
    private Node<E> tail;
    private int size;

    private static class Node<E> {
        public final E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(E data) {
            this(data, null, null);
        }

        public Node(E data, Node<E> prev, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }


    @Override
    public void addFront(E e) {
        if (this.head == null){
            this.tail = new Node<E>(e);
            this.head = this.tail;
        }
        else{
            this.head.prev = new Node<E>(e, null, this.head);
            this.head = this.head.prev;
        }
        this.size++;
    }

    @Override
    public void addBack(E e) {
        if (this.tail == null){
            this.head = new Node<E>(e);
            this.tail = this.head;
        }
        else{
            this.tail.next = new Node<E>(e, this.tail, null);
            this.tail = this.tail.next;
        }
        this.size++;
    }

    @Override
    public E removeFront() {
        if (this.size == 0) return null;
        E frontElement = this.head.data;
        if(this.size == 1){
            this.head = null;
            this.tail = null;
        }
        else {
            this.head = this.head.next;
            this.head.prev = null;
        }
        this.size--;
        return frontElement;
    }

    @Override
    public E removeBack() {
        if (this.size == 0) return null;
        E backElement = this.tail.data;
        if(this.size == 1){
            this.head = null;
            this.tail = null;
        }
        else {
            this.tail = this.tail.prev;
            this.tail.next = null;
        }
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
        return this.head.data;
    }

    @Override
    public E peekBack() {
        if (this.size == 0) return null;
        return this.tail.data;
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() { return new LinkedDequeIterator();}

    private class LinkedDequeIterator implements Iterator<E> {
        private Node<E> nextNode;
        private int idx;
        public LinkedDequeIterator() {
            this.nextNode = head;
            this.idx = 0;
        }

        @Override
        public boolean hasNext() {
            return this.idx < size();
        }

        @Override
        public E next() {
            E toReturn = this.nextNode.data;
            this.nextNode = this.nextNode.next;
            this.idx++;
            return toReturn;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        if (this.isEmpty()) {
            return "[]";
        }

        String result = "[";
        Node<E> curr = this.head;
        while (curr.next != null) {
            result += curr.data + ", ";
            curr = curr.next;
        }
        result += curr.data;
        return result + "]";
    }
}
