package edu.caltech.cs2.coloring;

import edu.caltech.cs2.datastructures.ChainingHashDictionary;
import edu.caltech.cs2.datastructures.ChainingHashSet;
import edu.caltech.cs2.datastructures.MoveToFrontDictionary;
import edu.caltech.cs2.interfaces.IDictionary;
import edu.caltech.cs2.interfaces.ISet;

public class NodeGraph {

    private final Node[] nodes;

    /**
     * Represents one node in the graph
     */
    private static class Node {
        public int name;
        public int color;
        public ISet<Node> neighbors; // The set of this node's neighboring nodes
        public ISet<Integer> neighborColors; // The colors which its neighbors use
        public ISet<Integer> neighborsUncolored; // The names of its neighbors which aren't yet colored

        public Node(int name, int color) {
            this.name = name;
            this.color = color;
            this.neighbors = new ChainingHashSet<>();
            this.neighborColors = new ChainingHashSet<>();
            this.neighborsUncolored = new ChainingHashSet<>();
        }

        public void addNeighbor(Node neighbor) {
            this.neighbors.add(neighbor);
            this.neighborsUncolored.add(neighbor.name);
        }

        @Override
        public String toString() {
            return this.name + "[" + this.color + "]";
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Node)) {
                return false;
            }
            return ((Node)o).name == this.name;
        }

        @Override
        public int hashCode() {
            return this.name;
        }
    }

    /**
     * Creates a new graph
     * @param n The (fixed) number of vertices for the graph to have. They will be
     *          "named" with the numbers 0 through n.
     */
    public NodeGraph(int n) {
        this.nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            this.nodes[i] = new Node(i, 0);
        }
    }

    /**
     * Whether or not a vertex is colored yet
     * @param n The name of the vertex
     * @return True if the vertex has a color, false if it doesn't yet
     */
    public boolean isColored(int n) {
       return this.nodes[n].color != 0;
    }

    /**
     * Gets the color of a given vertex
     * @param n The name of the vertex
     * @return The vertex's color, or 0 if it doesn't have one yet
     */
    public int getColor(int n) {
        return this.nodes[n].color;
    }

    /**
     * Sets the color of a vertex
     * @param n The name of the vertex to set the color of
     * @param color The color to set it to
     */
    public void setColor(int n, int color) {
        if (this.isColored(n)) {
            throw new IllegalArgumentException(n + " already has a color.");
        }
        if (color <= 0) {
            throw new IllegalArgumentException("Only numbers >= 1 are valid colors, not " + color);
        }
        checkColoringIsValid(n, color);
        this.nodes[n].color = color;
        for (Node m : this.nodes[n].neighbors) {
            m.neighborColors.add(color);
            m.neighborsUncolored.remove(n);
        }
    }

    /**
     * Gets a vertex's neighbors
     * @param n The name of the vertex
     * @return A list of the names of that vertex's neighbors
     */
    public ISet<Integer> neighbors(int n) {
        ISet<Integer> adj = new ChainingHashSet<>();

        ISet<Node> neighbors = this.nodes[n].neighbors;
        for (Node neigh : neighbors) {
            adj.add(neigh.name);
        }

        return adj;
    }

    /**
     * Gets the degree of saturation of a given vertex
     * (i.e., the number of unique colors across its neighbors)
     * @param n The name of the vertex
     * @return Its degree of saturation
     */
    public int degreeOfSaturation(int n) {
        return this.nodes[n].neighborColors.size();
    }

    /**
     * Gets the number of uncolored neighbors of a given vertex
     * @param n The name of the vertex
     * @return How many of its neighbors don't yet have colors
     */
    public int numUncoloredNeighbors(int n) {
        return this.nodes[n].neighborsUncolored.size();
    }

    /**
     * Gets the (fixed) number of vertices in the graph
     * @return The total number of vertices
     */
    public int numVertices() {
        return this.nodes.length;
    }

    /**
     * Adds an edge between two vertices
     * @param n The name of one vertex
     * @param m The name of the other vertex
     */
    public void addEdge(int n, int m) {
        this.nodes[n].addNeighbor(this.nodes[m]);
        this.nodes[m].addNeighbor(this.nodes[n]);
    }

    /**
     * Gets the current coloring of the graph
     * @return A map between each vertex's name and its color
     */
    public IDictionary<Integer, Integer> getColoring() {
        IDictionary<Integer, Integer> coloring = new ChainingHashDictionary<>(MoveToFrontDictionary::new);
        for (Node n : this.nodes) {
            coloring.put(n.name, n.color);
        }
        return coloring;
    }

    /**
     * Checks if a vertex can validly be colored with a given color
     * @param n The name of the vertex
     * @param color The color for it to have
     * @throws IllegalColoringException If this would cause an illegal coloring
     */
    private void checkColoringIsValid(int n, int color) {
        ISet<Integer> adjs = this.neighbors(n);
        for (int v : adjs) {
            if (this.nodes[v].color == color) {
                throw new IllegalColoringException();
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (Node n : this.nodes) {
            result.append(n.toString()).append("->").append(n.neighbors).append(", ");
        }
        return result + "}";
    }
}
