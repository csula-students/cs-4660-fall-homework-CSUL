package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Breadth first search
 */
public class BFS implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        Collection<Node> allNodes = graph.getNodes();
        Queue<Node> queue = new LinkedList<>();

        Node endingNode = null;

        for (Node node : allNodes) {
            node.distance = Integer.MAX_VALUE;
            node.parent = null;
        }

        //push source node into the queue
        source.distance = 0;
        source.parent = null;
        queue.add(source);

        Node parent;

        while (!queue.isEmpty()) {
            parent = queue.poll();
            for (Node child : graph.neighbors(parent)) {
                if (child.distance == Integer.MAX_VALUE) {
                    child.distance = graph.distance(parent, child);
                    child.parent = parent;
                    queue.offer(child);
                }
                if (child.equals(dist)) {
                    endingNode = child;
                }
            }
        }

        return getPath(graph, endingNode);
    }

    private List<Edge> getPath(Graph graph, Node endNode) {
        return null;
    }
}
