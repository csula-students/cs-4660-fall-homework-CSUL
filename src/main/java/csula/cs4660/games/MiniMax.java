package csula.cs4660.games;
import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import java.util.ListIterator;


//
public class MiniMax {
    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        int minimum = Integer.MIN_VALUE;
        int maximum = Integer.MAX_VALUE;

        if(depth==0 ){
            return root;

        }

        if(max){

            Node  best = new Node<>(new MiniMaxState(minimum, minimum));
//            ListIterator it = graph.neighbors(root).listIterator();

            for(Node<MiniMaxState> neightbor: graph.neighbors(root)){
                int newDepth = depth -1;
                Node<MiniMaxState> tempBest = getBestMove(graph, neightbor, newDepth, false);

                if(tempBest.getData().getValue()>((MiniMaxState)best.getData()).getValue()){

                    Node<MiniMaxState> previous = graph.getNode(root).get();
                    previous.getData().setValue((tempBest.getData()).getValue());

                    best = tempBest;

                }

            }

            return best;
        }

        else{

            Node best = new Node<>(new MiniMaxState(maximum, maximum));
            for(Node<MiniMaxState> neightbor: graph.neighbors(root)){
                int newDepth = depth -1;

                Node<MiniMaxState> tempBest = getBestMove(graph, neightbor, newDepth, true);
                //

                if(tempBest.getData().getValue()<((MiniMaxState)best.getData()).getValue()){

                    Node<MiniMaxState> previous = graph.getNode(root).get();
                    previous.getData().setValue(((MiniMaxState)tempBest.getData()).getValue());

                    best=  tempBest;

                }

            }
            return best;
        }

    }

}

