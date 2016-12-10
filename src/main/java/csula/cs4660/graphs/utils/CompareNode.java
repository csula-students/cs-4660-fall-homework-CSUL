package csula.cs4660.graphs.utils;

import csula.cs4660.graphs.Node;
import java.util.Comparator;

public class CompareNode implements Comparator<Node> {

    @Override
    public int compare(Node node1, Node node2) {
        if (node1.distance > node2.distance) {
            return 1;
        } else if (node1.distance < node2.distance) {
            return -1;
        }
        return 0;
    }
}