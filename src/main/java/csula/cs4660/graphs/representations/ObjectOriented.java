package csula.cs4660.graphs.representations;


import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.*;
import java.util.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


/**
 * Object oriented representation of graph is using OOP approach to store nodes
 * and edges
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

            String[] line;

            int lineNum =0;
            while(inFile.hasNextLine()){

                line = inFile.nextLine().split(":");
                edges.add(new Edge(new Node(Integer.parseInt(line[0])), new Node(Integer.parseInt(line[1])), Integer.parseInt(line[2])));

                if(!nodes.contains(new Node(Integer.parseInt(line[0]))))
                    nodes.add(new Node(Integer.parseInt(line[0])));
                lineNum++;
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }

    public ObjectOriented() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    @Override
    public boolean adjacent(Node x, Node y) {

        Iterator<Edge> it = edges.iterator();

        while (it.hasNext()){
            Edge e = it.next();
            if((e.getFrom().equals(x)) && e.getTo().equals(y) || (e.getFrom().equals(y) && e.getTo().equals(x))) {
                return true;
            }
        }

        return  false;
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
        if(nodes.contains(x)) {
            return false;
        }
        nodes.add(x);
        return true;


    }

    @Override
    public boolean removeNode(Node x) {
        if(!nodes.contains(x)) {
            return false;
        }

        Iterator<Edge> it = edges.iterator();
        int check =0;
        ArrayList <Edge> toBeRemoved = new ArrayList<>();
        while (it.hasNext()){
            check++;
            Edge e = it.next();
            if(e.getFrom().equals(x) || e.getTo().equals(x)){
                toBeRemoved.add(e);
            }
        }

        //now remove the edges
        Iterator rm = toBeRemoved.iterator();
        while (rm.hasNext()){
            edges.remove(rm.next());
        }

        return nodes.remove(x);

    }

    @Override
    public boolean addEdge(Edge x) {
        if(edges.contains(x))
            return false;

        edges.add(x);
        return true;
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

    @Override
    public Collection<Node> getNodes(){
        return nodes;
    }

    @Override
    public Collection<Edge> getEdges(){
//        System.out.println("\n\nOBJECT ORIENTED RETURNING: " + edges.size());
        return edges;
    }


    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = nodes.iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
}
