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

        PriorityQueue<Node> pq = new PriorityQueue<Node>(graph.getNodes().size(), new CompareNode());
        HashMap<Node, Node> previousNode = new HashMap<>();
        List<Edge> path = new ArrayList<>();
        HashMap<Node, Node> nodeList = new HashMap<>();
        Node src = source;
        Node dst = dist;
        src.distance =0.0;


        previousNode.put(src, null);

        pq.add(src);

        while (pq.size() > 0) {

            Node currentNode = pq.poll();
            List<Node> neighbors = graph.neighbors(currentNode);
            if (neighbors == null) continue;

            for (Node nextVertex : neighbors) {
                double newDistance = currentNode.distance + graph.distance(currentNode, nextVertex);

                if(nodeList.containsKey(nextVertex))
                    nextVertex = nodeList.get(nextVertex);

                if (nextVertex.distance == Double.POSITIVE_INFINITY) {
                    nextVertex.distance = newDistance;

                    if (!previousNode.containsKey(nextVertex)) {
                        previousNode.put(nextVertex, currentNode);
                        nodeList.put(nextVertex, nextVertex);
                        pq.add(nextVertex);
                    }


                } else {
                    if (nextVertex.distance > newDistance) {

                        nextVertex.distance = newDistance;
                        previousNode.put(nextVertex, currentNode);
                        nodeList.put(nextVertex, nextVertex);
                        pq.remove(nextVertex);
                        pq.add(nextVertex);
                    }
                }


            }
        }

        ArrayList<Node> nodePath = new ArrayList<Node>();

        Stack<Node> nodePathTemp = new Stack<Node>();
        nodePathTemp.push(dst);

        Node v = dst;

        while (previousNode.containsKey(v) && previousNode.get(v) != null) {
            path.add(getEdge(previousNode.get(v), v, graph));
            v = previousNode.get(v);

            nodePathTemp.push(v);

        }

        while (nodePathTemp.size() > 0)
            nodePath.add(nodePathTemp.pop());

        Collections.reverse(path);

        return path;
    }



    public Edge getEdge(Node from, Node to, Graph graph) {
        for (Edge edge : graph.getEdges()) {
            if (edge.getFrom().equals(from) && edge.getTo().equals(to)) {
                return edge;

            }
        }
        return null;
    }
}
