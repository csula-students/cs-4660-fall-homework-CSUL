package csula.cs4660.graphs.representations;


import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.*;
import java.util.*;

/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
 *
 * TODO: Please fill the body of methods in this class
 */
public class ObjectOriented implements Representation {
    private Collection<Node> nodes;
    private Collection<Edge> edges;

    public ObjectOriented(File file) {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();

        Scanner inFile;
        int numNodes;
        try{
            inFile = new Scanner(file);
            numNodes = Integer.parseInt(inFile.nextLine());
            System.out.println(numNodes);
            String[] line;

            int lineNum =0;
            while(inFile.hasNextLine()){
                nodes.add(new Node(lineNum));
                line = inFile.nextLine().split(":");
                    edges.add(new Edge(new Node(line[0]), new Node(line[1]), Integer.parseInt(line[2])));
                lineNum++;
            }



//        Iterator<Node> it = nodes.iterator();
//            while(it.hasNext()) {
//                System.out.println("Node: " + it.next().getData());
//            }
//
//            System.out.println("EDGES size: " + edges.size());
//            Iterator<Edge> it1 = edges.iterator();
//            while(it1.hasNext()) {
//                Edge e = it1.next();
//                System.out.println(e.getFrom() + ":" + e.getTo() + ":" + e.getValue());
//            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }

    public ObjectOriented() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        ArrayList<Node> neighbors = new ArrayList<>();
        Iterator<Edge> it = edges.iterator();

        while (it.hasNext()){
            Edge e = it.next();
            if(e.getFrom().equals(x)){
                neighbors.add(e.getTo());
            }
        }

        return neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        return false;
    }

    @Override
    public boolean removeNode(Node x) {
        if(!nodes.contains(x))
            return false;

        Iterator<Edge> it = edges.iterator();

        while (it.hasNext()){
            Edge e = it.next();
            if(e.getFrom().equals(x) || e.getTo().equals(x)){
                edges.remove(e);
                nodes.remove(x);
            }
        }
        return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        return false;
    }

    @Override
    public boolean removeEdge(Edge x) {
            return edges.remove(x);
    }

    @Override
    public int distance(Node from, Node to) {
        Iterator<Edge> it = edges.iterator();

        while (it.hasNext()){
            Edge e = it.next();
            if(e.getFrom().equals(from) && e.getTo().equals(to)){
                return e.getValue();
            }
        }
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
