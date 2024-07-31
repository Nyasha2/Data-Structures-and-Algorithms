package edu.caltech.cs2.lab06;

import java.util.*;
import java.util.stream.Collectors;

public class PebblingGraph {
    public final Set<PebblingNode> vertices;

    public static class PebblingNode {
        public final int id;
        public final Set<PebblingNode> neighbors;
        public int inDegree;

        public PebblingNode(int id) {
            this.id = id;
            this.neighbors = new HashSet<>();
            this.inDegree = 0;
        }

        public String toString() {
            return this.id + " -> {" + this.neighbors.stream().map(v -> "" + v.id).sorted().collect(Collectors.joining(", ")) + "}";
        }
    }

    public PebblingGraph() {
        this.vertices = new HashSet<>();
    }

    public PebblingNode addVertex(int id) {
        PebblingNode v = new PebblingNode(id);
        this.vertices.add(v);
        return v;
    }

    public void addEdge(PebblingNode fromVertex, PebblingNode toVertex) {
        if (!this.vertices.contains(fromVertex) || !this.vertices.contains(toVertex)) {
            throw new IllegalArgumentException("Vertices don't exist in graph");
        }

        fromVertex.neighbors.add(toVertex);
        toVertex.inDegree++;
    }

    public List<Integer> toposort() {
        // set up
        List<Integer> output = new ArrayList<>();
        Map<PebblingNode, Integer> dependencies = new HashMap<>();
        List<PebblingNode> worklist = new ArrayList<>();
        for (PebblingNode vertex : this.vertices) {
            dependencies.put(vertex, vertex.inDegree);
            if (dependencies.get(vertex) == 0) {
                worklist.add(vertex);
            }
        }

        // do work
        while (!worklist.isEmpty()){
            PebblingNode vertex = worklist.remove(0);
            output.add(vertex.id);
            for (PebblingNode w : vertex.neighbors) {
                dependencies.put(w, dependencies.get(w) - 1);
                if (dependencies.get(w) == 0) {
                    worklist.add(w);
                }
            }
        }

        return output;
    }
}