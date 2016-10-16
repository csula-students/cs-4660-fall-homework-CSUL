package csula.cs4660.graphs.searches;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.*;

/**
 * Breadth first search
 */
public class BFS implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
            Queue<Node> queue = new LinkedList<>();
            HashMap<Node , Edge> roots = new HashMap<Node,Edge>();

            Collection<Node> visitedNodes = new LinkedList<>();

            roots.put(source,null);
            queue.offer(source);
            visitedNodes.add(source);


            while(!queue.isEmpty()){
                Node curNode = queue.poll();
                graph.neighbors(curNode).stream().filter(child -> !visitedNodes.contains(child)).forEach(child -> {
                    roots.put(child, new Edge(curNode, child, graph.distance(curNode, child)));
                    queue.add(child);
                    visitedNodes.add(child);
                });
            }


            return getPath(roots, dist);
    }

    private List<Edge> getPath(HashMap <Node, Edge> roots, Node dist){
        List<Edge> path = new LinkedList<>();
        Edge currentEdge = roots.get(dist);
        while(currentEdge != null){
            path.add(currentEdge);
            currentEdge = roots.get(currentEdge.getFrom());
        }

        Collections.reverse(path);
        return  path;
    }
}
