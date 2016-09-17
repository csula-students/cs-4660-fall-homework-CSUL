package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 *
 * TODO: please implement the method body
 */
public class AdjacencyList implements Representation {
    private Map<Node, Collection<Edge>> adjacencyList;

    public AdjacencyList(File file) {
        System.out.println("In constructor!!!!");
        adjacencyList = new HashMap<>();

        Scanner inFile;
        int numNodes;
        try{
            inFile = new Scanner(file);
            numNodes = Integer.parseInt(inFile.nextLine());

            String[] line;

            while(inFile.hasNextLine()){
                line = inFile.nextLine().split(":");

                Node from = new Node(Integer.parseInt(line[0]));
                Node to = new Node(Integer.parseInt(line[1]));
                int value = Integer.parseInt(line[2]);

                if(adjacencyList.containsKey(from))
                    adjacencyList.get(from).add(new Edge(from, to, value));
                else{
                    ArrayList <Edge> edge = new ArrayList<>();
                    edge.add(new Edge(from, to, value));
                    adjacencyList.put(from, edge);
                }

            }
//DEBUG
            for(Map.Entry<Node, Collection<Edge>> entry : adjacencyList.entrySet()){
                System.out.print(entry.getKey() + "-> ");
                Collection<Edge> edges = entry.getValue();
                for(Edge edge: edges){
                    System.out.print(edge.toString());
                }
                System.out.println();
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public AdjacencyList() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {

                Collection<Edge> edges = adjacencyList.get(x);

                for (Edge edge : edges) {
                    if (edge.getTo().equals(y)) {
                        return true;
                    }

                }

        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        ArrayList<Node> neighbors = new ArrayList<>();
        Collection<Edge> edges = adjacencyList.get(x);

        for(Edge edge: edges){
            neighbors.add(edge.getTo());
        }
        if(x.getData().toString().equals("9"))
            System.out.println("Array size in LIST is: " + neighbors.size());
        return  neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        if(adjacencyList.containsKey(x)) {
            return false;
        }

        adjacencyList.put(new Node(x), new ArrayList<Edge>());
        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        if(!adjacencyList.containsKey(x)){
            return false;
        }


        for(Map.Entry<Node, Collection<Edge>> entry : adjacencyList.entrySet()){
                Collection<Edge> edges = entry.getValue();
                ArrayList<Edge> toBeRemoved = new ArrayList<>();
                for(Edge edge: edges){
                   if(edge.getTo().equals(x)){
                       toBeRemoved.add(edge);
                   }
                }

                //now remove
                for(Edge remEdge: toBeRemoved){
                    edges.remove(remEdge);
                }
            }
        adjacencyList.remove(x);
            return true;
    }

    @Override
    public boolean addEdge(Edge x) {
        if(adjacencyList.get(x.getFrom()).contains(x)){
            return false;
        }

        adjacencyList.get(x.getFrom()).add(x);
        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        if(!adjacencyList.get(x.getFrom()).contains(x)){
            return false;
        }
        adjacencyList.remove(x.getFrom());
        return true;
    }

    @Override
    public int distance(Node from, Node to) {
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
