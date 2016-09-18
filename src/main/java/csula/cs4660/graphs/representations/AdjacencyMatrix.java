package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 *
 * TODO: please fill the method body of this class
 */
public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;
    private int numberOfNodes =0;


    public AdjacencyMatrix(File file) {

        Scanner inFile;
        int numNodes;
        try {
            inFile = new Scanner(file);
            numNodes = Integer.parseInt(inFile.nextLine());

            adjacencyMatrix = new int[20][20];

            nodes = new Node[20];
            System.out.println("SIZE: " + nodes.length);
            String[] line;

            while (inFile.hasNextLine()) {
                line = inFile.nextLine().split(":");
                Node from = new Node(Integer.parseInt(line[0]));
                Node to = new Node(Integer.parseInt(line[1]));
                int value = Integer.parseInt(line[2]);

                if(!Arrays.asList(nodes).contains(from))
                    addNode(from);
                if(!Arrays.asList(nodes).contains(to))
                    addNode(to);

                addEdge(new Edge(from, to, value));

            }
//            System.out.println(Arrays.deepToString(adjacencyMatrix));
        } catch (FileNotFoundException e) {
            System.out.print(e.getCause());
        }
    }

    public AdjacencyMatrix() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        return null;
    }

    @Override
    public boolean addNode(Node x) {
        if(Arrays.asList(nodes).contains(x)){
            System.out.println("COntains " + x.getData().toString());
            return false;
        }

//        if(numberOfNodes == nodes.length){
//            expandArray();
//        }
//        System.out.println("Adding NODE...." + x.getData().toString());
        nodes[numberOfNodes] = x;
//        if(x.getData().toString().equals("11")) {
//            System.out.println("Number of nodes " + numberOfNodes);
//        }
        for (int i = 0; i <= numberOfNodes; i++){
//            if(x.getData().toString().equals("11")) {
//                System.out.println("Adding NODE...." + i);
//            }
            adjacencyMatrix[numberOfNodes][i] = 0;
            adjacencyMatrix[i][numberOfNodes] = 0;
        }
        numberOfNodes++;
        return true;
    }

    @Override
    public boolean removeNode(Node x) {
        return false;
    }

    @Override
    public boolean addEdge(Edge x){

        int from = Integer.parseInt(x.getFrom().getData().toString());
        int to = Integer.parseInt(x.getTo().getData().toString());
        int fromIndex = findIndex(x.getFrom());
        int toIndex = findIndex(x.getTo());

        if(adjacencyMatrix[fromIndex][toIndex] == 1 || adjacencyMatrix[toIndex][fromIndex] ==1){
            return false;
        }

        adjacencyMatrix[fromIndex][toIndex] = 1;
        adjacencyMatrix[toIndex][fromIndex] =1;
        return true;
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

//    public void expandArray(){
//        nodes = Arrays.copyOf(nodes, nodes.length + 1);
//        System.out.print("After expanding : " + Arrays.deepToString(nodes));
//    }

    public int findIndex(Node node)
    {
        for (int i = 0; i < numberOfNodes; ++i)
            if (nodes[i].equals(node))
                return i;
        return -1;
    }
}
