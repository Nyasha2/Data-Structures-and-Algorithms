package edu.caltech.cs2.datastructures;

import edu.caltech.cs2.interfaces.IGraph;
import edu.caltech.cs2.interfaces.ISet;

public class Graph<V, E> implements IGraph<V, E> {

    @Override
    public boolean addVertex(V vertex) {
        return false;
    }

    @Override
    public boolean addEdge(V src, V dest, E e) {
        return false;
    }

    @Override
    public boolean addUndirectedEdge(V n1, V n2, E e) {
        return false;
    }

    @Override
    public boolean removeEdge(V src, V dest) {
        return false;
    }

    @Override
    public ISet<V> vertices() {
        return null;
    }

    @Override
    public E adjacent(V i, V j) {
        return null;
    }

    @Override
    public ISet<V> neighbors(V vertex) {
        return null;
    }
}