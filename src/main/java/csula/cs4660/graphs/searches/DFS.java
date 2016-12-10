package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Depth first search
 */
public class DFS implements SearchStrategy {

    List<Edge> path = new ArrayList<>();
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        dfsHelper(graph, source, dist);
        Collections.reverse(path);
        return path;
    }

        public boolean dfsHelper(Graph graph, Node source, Node dist) {
            if (source.equals(dist))
                return true;

            for(Node child: graph.neighbors(source)){
                if (dfsHelper(graph, child, dist)) {
                    path.add(new Edge(source, child, graph.distance(source, child)));
                    return true;
                }
            }
            return false;
        }
}
