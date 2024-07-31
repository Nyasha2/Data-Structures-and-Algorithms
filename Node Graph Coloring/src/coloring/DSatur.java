package edu.caltech.cs2.coloring;

import edu.caltech.cs2.datastructures.ArrayDeque;
import edu.caltech.cs2.datastructures.ChainingHashSet;
import edu.caltech.cs2.datastructures.MinFourHeap;
import edu.caltech.cs2.interfaces.IDeque;
import edu.caltech.cs2.interfaces.IPriorityQueue;
import edu.caltech.cs2.interfaces.ISet;


public class DSatur {
    public static void color(NodeGraph g) {
        IPriorityQueue<Integer> queue = new MinFourHeap<>();
        for (int i =0; i < g.numVertices(); i++){
            double priority = (g.numVertices() - g.degreeOfSaturation(i))  - ((float)g.numUncoloredNeighbors(i)/g.numVertices());
            IPriorityQueue.PQElement<Integer> toAdd = new IPriorityQueue.PQElement<>(i, priority);
            queue.enqueue(toAdd);
        }
//        tieBreak(queue, g);
        while (queue.size() != 0) {

            int currNode = queue.dequeue().data;
            int lowestColor = 1;
            IDeque<Integer> colors = new ArrayDeque<>();
            for (int node : g.neighbors(currNode)) {
                if (g.isColored(node)) {
                    colors.add(g.getColor(node));
                }
            }
            while (lowestColor < g.numVertices()) {
                if (colors.contains(lowestColor)) {
                    lowestColor++;
                } else {
                    break;
                }
            }
            g.setColor(currNode, lowestColor);
            ISet<Integer> neighbors = g.neighbors(currNode);
            for (int node : neighbors) {
                if (!g.isColored(node)) {
                            double newPriority = (g.numVertices() - g.degreeOfSaturation(node))  - ((float)g.numUncoloredNeighbors(node)/g.numVertices());
                            IPriorityQueue.PQElement<Integer> updated = new IPriorityQueue.PQElement<>(node, newPriority);
                            try {
                                queue.decreaseKey(updated);
                            }
                            catch(Exception e){
                                continue;
                            }
                }
            }


        }
    }
}
