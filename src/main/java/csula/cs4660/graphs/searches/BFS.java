package csula.cs4660.graphs.searches;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Lists;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Breadth first search
 */
public class BFS implements SearchStrategy {
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
        Collection<Node> allNodes = graph.getNodes();
        Queue<Node> queue = new LinkedList<>();
        List<Edge> path = new ArrayList<>();


//        if(allNodes != null) {
        for (Node node : allNodes) {
            node.distance = Integer.MAX_VALUE;
            node.parent = null;
        }
//        }
        //push source node into the queue

        source.distance = 0;
//        source.parent = null;
        queue.add(source);
//        if(allNodes.contains(source)){
//            for(Node curNode: allNodes){
//                if(curNode.equals(source)){
//                    curNode.distance =0;
//                    curNode.parent = null;
//                    queue.offer(source);
//                    break;
//                }
//            }
//        }

        Node endingNode = null;
        Node parent = null;

        while (!queue.isEmpty()) {
            parent = queue.poll();
            for (Node child : graph.neighbors(parent)) {
                queue.add(child);
                if (child.distance == Integer.MAX_VALUE) {
                    child.distance = graph.distance(parent, child);
                    child.parent = parent;

                }

                if (child.equals(dist)) {
                    endingNode = child;
                }

            }
        }

        while (endingNode.parent != null) {
            path.add(new Edge(endingNode.parent, endingNode, graph.distance(endingNode.parent, endingNode)));
            endingNode = endingNode.parent;
        }

        Edge list2[] = new Edge[path.size()];
        list2 = path.toArray(list2);

        System.out.println("\n\nBEFORE SORTING:");
        for (Edge edge : list2) {
            System.out.println("EDGE: " + edge.getFrom().getData());
        }

        Lists.reverse(path);
        List<Edge> tempList = Arrays.asList(list2);
        Collections.reverse(tempList);

        System.out.println("\n\nAFTER SORTING:");
        for (Edge edge : tempList) {
            System.out.println("EDGE: " + edge.getFrom().getData());
        }

        return path;
//        return getPath(graph, endingNode);
    }
}

