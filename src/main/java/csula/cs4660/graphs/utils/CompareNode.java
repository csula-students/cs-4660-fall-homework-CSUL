package csula.cs4660.graphs.utils;

import csula.cs4660.graphs.Node;
import java.util.Comparator;

public class CompareNode implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2){
       return Double.compare(node1.distance, node2.distance);
    }
}