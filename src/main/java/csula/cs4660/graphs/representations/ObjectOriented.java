package csula.cs4660.graphs.representations;


import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

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

                line = inFile.nextLine().split(":");
                    edges.add(new Edge(new Node(Integer.parseInt(line[0])), new Node(Integer.parseInt(line[1])), Integer.parseInt(line[2])));

                if(!nodes.contains(new Node(Integer.parseInt(line[0]))))
                    nodes.add(new Node(Integer.parseInt(line[0])));
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
//        if(!nodes.contains(x) || !nodes.contains(y))
//            return false;

        Iterator<Edge> it = edges.iterator();

        while (it.hasNext()){
            Edge e = it.next();
            //this following line shoudl work but doesn't...maybe I'm too tired to see the problem... so I did it some other way
                if(
                        (e.getFrom().equals(x)) && e.getTo().equals(y) || (e.getFrom().equals(y) && e.getTo().equals(x))
//                        (e.getFrom().getData().toString().equals(x.getData().toString()) &&
//                    e.getTo().getData().toString().equals(y.getData().toString()))
//            || (e.getFrom().getData().toString().equals(y.getData().toString()) &&
//                        e.getTo().getData().toString().equals(x.getData().toString()))

                        ) {

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
                System.out.println("FOUND NEIGHBOR!!");
                neighbors.add(e.getTo());
            }
        }
        System.out.println("ArrayList size: " + neighbors.size());
        return neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        if(nodes.contains(x)) {
            System.out.println("Already have " + x.getData().toString() +  " node. Returning False...");
            return false;
        }
        nodes.add(x);
        return true;


    }

    @Override
    public boolean removeNode(Node x) {
        System.out.println("SIze of edges: " + edges.size());
        if(!nodes.contains(x)) {
            System.out.println("NODE MISSING!!!!");
            return false;
        }

        Iterator<Edge> it = edges.iterator();
        int check =0;
        while (it.hasNext()){
            check++;
            System.out.println("Check: " + check);
            Edge e = it.next();
            System.out.println("Left next");
            if(e.getFrom().equals(x) || e.getTo().equals(x)){
                System.out.println("Removing node!");
                edges.remove(e);
//                nodes.remove(x);
//                return true;
            }
        }
        System.out.println("LEft while loop<<<<<<");
        nodes.remove(x);
        System.out.println("Gonna return true...");
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
