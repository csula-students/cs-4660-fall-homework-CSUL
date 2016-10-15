package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;
import org.omg.CORBA.NO_IMPLEMENT;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
//            System.out.println("SIZE: " + nodes.length);
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

//            for(int i =0; i < nodes.length; ++i){
//
//                System.out.println(nodes[i] + " ");
//            }
//            System.out.println(Arrays.deepToString(adjacencyMatrix));
        } catch (FileNotFoundException e) {
            System.out.print(e.getCause());
        }
    }

    public AdjacencyMatrix() {

    }

    @Override
    public boolean adjacent(Node x, Node y) {
        int fromIndex = findIndex(x);
        int toIndex = findIndex(y);

        if(adjacencyMatrix[fromIndex][toIndex] == 1 || adjacencyMatrix[toIndex][fromIndex] ==1){
            return true;
        }
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
        ArrayList<Node> neighbors = new ArrayList<>();
        int fromIndex = findIndex(x);
        if(x.getData().toString().equals("4")) {
            System.out.println("FromIndex is: " + fromIndex);
        }
//        System.out.println("Checking neighbor for Node: " + x.getData().toString());
        if(x.getData().toString().equals("4")){
            for(int i =0; i < nodes.length; ++i){

                System.out.println(nodes[i] + " ");
            }
        }

         for (int i = 0; i <= numberOfNodes; i++){
            if(adjacencyMatrix[fromIndex][i] == 1){

                    neighbors.add(nodes[i]);
                    if(x.getData().toString().equals("4")) {
                    System.out.println("Match!: " + i);
                    System.out.println("Adding NODE: " + nodes[i]);
                }
            }

        }

        int [] nums = new int[neighbors.size()];
        for(int i =0; i < nums.length; ++i){
            nums[i] = Integer.parseInt(neighbors.get(i).getData().toString());
        }
        Arrays.sort(nums);
        ArrayList<Node> newNeighbors = new ArrayList<>();
        for(int i =0; i < nums.length; ++i){
            newNeighbors.add(new Node(nums[i]));
        }
        return newNeighbors;
    }

    @Override
    public boolean addNode(Node x) {
        if(Arrays.asList(nodes).contains(x)){
//            System.out.println("COntains " + x.getData().toString());
            return false;
        }


        nodes[numberOfNodes] = x;

        for (int i = 0; i <= numberOfNodes; i++){

            adjacencyMatrix[numberOfNodes][i] = 0;
            adjacencyMatrix[i][numberOfNodes] = 0;
        }
        numberOfNodes++;
        return true;
    }

    @Override
    public boolean removeNode(Node x) {

        if (!Arrays.asList(nodes).contains(x)) {
            return false;
        }
        for (int k = 0; k < numberOfNodes; k++) {
            if (x.equals(nodes[k])) {

                //decrement the number of nodes
                numberOfNodes--;

                for (int i = k; i < numberOfNodes; i++) {
                    nodes[i] = nodes[i + 1];
                }

                for (int i = k; i < numberOfNodes; i++) {
                    for (int j = 0; j <= numberOfNodes; j++) {
                        adjacencyMatrix[i][j] = adjacencyMatrix[i + 1][j];
                    }
                }

                for (int i = k; i < numberOfNodes; i++) {
                    for (int j = 0; j < numberOfNodes; j++) {
                        adjacencyMatrix[j][i] = adjacencyMatrix[j][i + 1];
                    }
                }
            }

            //

        }
        return true;
    }


    @Override
    public boolean addEdge(Edge x){

        int from = Integer.parseInt(x.getFrom().getData().toString());
        int to = Integer.parseInt(x.getTo().getData().toString());
        int fromIndex = findIndex(x.getFrom());
        int toIndex = findIndex(x.getTo());

        if(from == 4){
            System.out.println("Adding adjecent to 4 -> " + to);

        }
        if(adjacencyMatrix[fromIndex][toIndex] == 1 ){
            return false;
        }

        adjacencyMatrix[fromIndex][toIndex] = 1;
//        adjacencyMatrix[toIndex][fromIndex] = 0;
        return true;
    }

    @Override
    public boolean removeEdge(Edge x) {
        int from = Integer.parseInt(x.getFrom().getData().toString());
        int to = Integer.parseInt(x.getTo().getData().toString());
        int fromIndex = findIndex(x.getFrom());
        int toIndex = findIndex(x.getTo());

        if(adjacencyMatrix[fromIndex][toIndex] == 0 && adjacencyMatrix[toIndex][fromIndex] ==0){
            return false;
        }

        adjacencyMatrix[fromIndex][toIndex] = 0;
//        adjacencyMatrix[toIndex][fromIndex] =0;

        return true;
    }

    @Override
    public int distance(Node from, Node to) {
        return 1;
    }


    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }

    @Override
    public Collection<Node> getNodes() {

        return Arrays.asList(nodes);
    }

    public int findIndex(Node node)
    {
        for (int i = 0; i < numberOfNodes; ++i)
            if (nodes[i].equals(node))
                return i;
        return -1;
    }
}
