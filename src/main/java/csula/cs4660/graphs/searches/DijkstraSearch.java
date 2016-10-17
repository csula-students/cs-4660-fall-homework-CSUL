package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.utils.CompareNode;
import org.omg.CORBA.NO_IMPLEMENT;

import java.util.*;

/**
 * As name, dijkstra search using graph structure
 */
public class DijkstraSearch implements SearchStrategy {


    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        final Queue<Node> queue = new PriorityQueue<Node>(200, new CompareNode());


        System.out.println("\n\nSIZE OF NODES IS: " + graph.getNodes().size() + "\n\n");

        for (Node node : graph.getNodes()) {
            if (node.equals(source)) {
                node.distance = 0;
                queue.add(node);
            }
        }

        final List<Node> doneSet = new ArrayList<>();

        while (!queue.isEmpty()) {
            Node src = queue.poll();
            System.out.println("Retrived from the queue: " + src.getData());
            if (!doneSet.contains(src)) {
                doneSet.add(src);

                for (Edge edge : getEdges(src, graph)) {
                    Node currentNode = getAdjacentNode(src, edge); //get from

                    if (!doneSet.contains(currentNode)) {
                        int newDistance = src.distance + edge.getValue();
                        if (newDistance < currentNode.distance) {
                            currentNode.distance = newDistance;
                            queue.add(currentNode);
                        }
                    }
                }
            }
        }

        for(Node node: doneSet){
            System.out.println("NODE: " + node.getData());
        }

        return null;
//        source.distance = 0.0;
//        PriorityQueue<Node> vertexQueue = new PriorityQueue<Node>(new CompareNode());
//        vertexQueue.add(source);
//
//        while (!vertexQueue.isEmpty()) {
//            Node u = vertexQueue.poll();
//
//            // Visit each edge emanating from u
//            for (Node v : graph.neighbors(u)) {
//
//                double weight = getEdge(v, graph).getValue();
//                double distanceThroughU = u.distance + weight;
//                if (distanceThroughU < v.distance) {
//                    vertexQueue.remove(v);
//                    v.distance = distanceThroughU;
//                    v.previousNode = u;
//                    vertexQueue.add(v);
//                }
//            }
//        }
//
//        List<Node> path = new ArrayList<Node>();
//        for (Node vertex = dist; vertex != null; vertex = vertex.previousNode)
//            path.add(vertex);
//        Collections.reverse(path);
//
//        for(Node node: path)
//            System.out.println("NODE: " + node.getData().toString());
//        return null;
//    }
//
//
//
    }
    public List<Edge> getEdges(Node node, Graph graph){
        List<Edge> edges = (List)graph.getEdges();
        System.out.println("graph.getEdges: " + graph.getEdges().size());
        List<Edge> sides = new ArrayList<>();
        for(Edge edge: edges){
            if(edge.getFrom().equals(node)){
//                if(!sides.containss(edge)) {
                    sides.add(edge);
//                }
            }
        }
        System.out.println("GETEDGES RETURNING #: " + sides.size());
        return sides;
    }

    public Node getAdjacentNode (Node node, Edge edge) {
        Node node1 = edge.getFrom();
        Node node2 = edge.getTo();
        return !node.equals(node1) ? node1 : node2;
    }
//
//    public Edge getEdge(Node node, Graph graph){
//        List<Edge> edges = (List)graph.getEdges();
//        Edge side = null;
//        for(Edge edge: edges){
//            if(edge.getTo().equals(node)){
//                return edge;
//
//            }
//        }
//        return null;
//    }
}