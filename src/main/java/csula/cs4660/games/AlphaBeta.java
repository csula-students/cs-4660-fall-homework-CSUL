package csula.cs4660.games;
import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;


public class AlphaBeta {
    public static Node getBestMove(Graph graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max) {
        int minimum = Integer.MIN_VALUE;
        int maximum = Integer.MAX_VALUE;

        if(depth==0 ){
            return source;

        }

        if(max){
            Node<MiniMaxState> best = new Node<>(new MiniMaxState(minimum, minimum));

            for(Node<MiniMaxState> neightbor: graph.neighbors(source)){
                int newDepth = depth -1;
                Node<MiniMaxState> tempBest = getBestMove(graph, neightbor, newDepth, alpha, beta, false);

                if(tempBest.getData().getValue()>((MiniMaxState)best.getData()).getValue()){
                    best = tempBest;
                }

                    Node<MiniMaxState> previous = graph.getNode(source).get();
                    previous.getData().setValue((best.getData()).getValue());
                    if(tempBest.getData().getValue() >= beta)
                        return tempBest;

                    if(tempBest.getData().getValue() >= alpha)
                        alpha = tempBest.getData().getValue();


//                    best = tempBest;


            }

            return best;
        }

        else{
            Node<MiniMaxState>  best = new Node<>(new MiniMaxState(maximum, maximum));

            for(Node<MiniMaxState> neightbor: graph.neighbors(source)){
                int newDepth = depth -1;
                Node<MiniMaxState> tempBest = getBestMove(graph, neightbor, newDepth, alpha, beta, true);

                if(tempBest.getData().getValue() < ((MiniMaxState)best.getData()).getValue()){
                    best = tempBest;
                }

                Node<MiniMaxState> previous = graph.getNode(source).get();
                previous.getData().setValue((best.getData()).getValue());
                if(tempBest.getData().getValue() <= alpha)
                    return tempBest;

                if(tempBest.getData().getValue() <= beta)
                    beta = tempBest.getData().getValue();

            }

            return best;
        }

    }
}