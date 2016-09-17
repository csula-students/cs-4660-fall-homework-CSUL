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
//            for(Map.Entry<Node, Collection<Edge>> entry : adjacencyList.entrySet()){
//                System.out.print(entry.getKey() + "-> ");
//                Collection<Edge> edges = entry.getValue();
//                for(Edge edge: edges){
//                    System.out.print(edge.toString());
//                }
//                System.out.println();
//            }


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

        return  neighbors;
    }

    @Override
    public boolean addNode(Node x) {
        return false;
    }

    @Override
    public boolean removeNode(Node x) {
        return false;
    }

    @Override
    public boolean addEdge(Edge x) {
        return false;
    }

    @Override
    public boolean removeEdge(Edge x) {
        return false;
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
