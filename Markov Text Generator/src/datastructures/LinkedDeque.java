package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IQueue;
import edu.caltech.cs2.interfaces.IStack;

import java.util.Iterator;

public class LinkedDeque<E> implements IDeque<E>, IQueue<E>, IStack<E> {
    private Node<E> head;

    private Node<E> tail;
    private int size;
    public LinkedDeque(){
        this.head = null;
        this.size = 0;
    }
    private static class Node<E>{

        public final E data;
        public Node<E> Next;
        public Node<E> Prev;

        public Node(E data){
            this(data, null, null);
        }

        public Node(E data, Node<E> next){
            this(data, next, null);
        }

        public Node(E data, Node<E> next, Node<E> prev){
            this.data = data;
            this.Next = next;
            this.Prev = prev;
        }
        public E getData(){
            return this.data;
        }
    }

    @Override
   public String toString(){
        if (this.head == null){
            return "[]";
        }
        Node<E> current = this.head;
        String result = "[";
        while (current != null){
            result += current.data + ", ";
            current = current.Next;
        }

        result = result.substring(0, result.length() - 2);
        return result + "]";

    }
    @Override
    public void addFront(E e) {
        if (this.head == null){
            Node nextNode = new Node(e);
            this.head = nextNode;
            this.tail = nextNode;
        }

        else{
            Node frontNode = new Node(e, this.head);
            this.head.Prev = frontNode;
            this.head = frontNode;

        }
        size ++;
    }

    @Override
    public void addBack(E e) {
        Node nextNode;
        if (this.head == null){
            nextNode = new Node(e);
            this.head = nextNode;
        }
        else{

            nextNode = new Node(e, null, this.tail);
            this.tail.Next = nextNode;
        }
        this.tail = nextNode;
        size++;
    }

    @Override
    public E removeFront() {
        if (this.head == null){
            return null;
        }
        else if(this.size ==1){
            E data = this.head.data;
            this.head = null;
            this.tail = null;
            size--;
            return data;
        }
        else{
            E data = this.head.data;
            this.head = this.head.Next;
            this.head.Prev = null;
            this.size--;
            return data;
        }
    }

    @Override
    public E removeBack() {
        if (this.head == null){
            return null;
        }
        else if(this.size ==1){
            E data = this.head.data;
            this.head = null;
            this.tail = null;
            size--;
            return data;
        }
        else{
            E data = this.tail.getData();
            this.tail = this.tail.Prev;
            this.tail.Next = null;
            this.size--;
            return data;
        }
    }

    @Override
    public boolean enqueue(E e) {
        this.addFront(e);
        return true;
    }

    @Override
    public E dequeue() {
        if (this.head == null){
            return null;
        }
        else {
            E data = this.tail.data;
            this.removeBack();
            return data;
        }
    }

    @Override
    public boolean push(E e) {
        addBack(e);
        return true;
    }

    @Override
    public E pop() {
        if (this.head == null){
            return null;
        }
        else {
            //this
            E element = this.tail.getData();
            this.removeBack();
            return element;
        }
    }

    @Override
    public E peekFront() {
        if (this.head == null){
            return null;
        }
        else {
            E data = this.head.data;
            return data;
        }
    }

    @Override
    public E peekBack() {
        if (this.tail == null){
            return null;
        }
        else {
            E data = this.tail.data;
            return data;
        }
    }

    @Override
    public E peek() {
        return this.peekBack();
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }
    private class LinkedDequeIterator implements Iterator<E> {
        private Node<E> thisNode;
        public LinkedDequeIterator() {
            this.thisNode = head;
        }
        @Override
        public boolean hasNext() { return !(this.thisNode == null);
        }

        @Override
        public E next() {
            E toReturn = this.thisNode.data;
            this.thisNode = this.thisNode.Next;
            return toReturn;
        }
    }

    @Override
    public int size() {
        return this.size;
    }
}
