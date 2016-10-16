package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.utils.CompareNode;

import java.util.*;

/**
 * As name, dijkstra search using graph structure
 */
public class DijkstraSearch implements SearchStrategy {


    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        source.distance = 0.0;
        PriorityQueue<Node> vertexQueue = new PriorityQueue<Node>(new CompareNode());
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Node u = vertexQueue.poll();

            // Visit each edge emanating from u
            for (Node v : graph.neighbors(u)) {

                double weight = getEdge(v, graph).getValue();
                double distanceThroughU = u.distance + weight;
                if (distanceThroughU < v.distance) {
                    vertexQueue.remove(v);
                    v.distance = distanceThroughU;
                    v.previousNode = u;
                    vertexQueue.add(v);
                }
            }
        }

        List<Node> path = new ArrayList<Node>();
        for (Node vertex = dist; vertex != null; vertex = vertex.previousNode)
            path.add(vertex);
        Collections.reverse(path);

        for(Node node: path)
            System.out.println("NODE: " + node.getData().toString());
        return null;
    }



    public List<Edge> getEdges(Node node, Graph graph){
        List<Edge> edges = (List)graph.getEdges();
        List<Edge> sides = new ArrayList<>();
        for(Edge edge: edges){
            if(edge.getFrom().equals(node)){
//                if(!sides.contains(edge)) {
                    sides.add(edge);
//                }
            }
        }
        return sides;gt
    }

    public Edge getEdge(Node node, Graph graph){
        List<Edge> edges = (List)graph.getEdges();
        Edge side = null;
        for(Edge edge: edges){
            if(edge.getTo().equals(node)){
                return edge;

            }
        }
        return null;
    }
}