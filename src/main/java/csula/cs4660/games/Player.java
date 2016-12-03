package csula.cs4660.games;
import java.util.*;

class Player {
    static int floodFillCount = 0;
    static Node rootNode = null;
    static Integer Alpha = Integer.MIN_VALUE;
    static Integer Beta = Integer.MAX_VALUE;
    static Map <PointXY,Tile> tileMap = new HashMap<PointXY,Tile>();
    static int P;
    private long startTime;
    static final int rows = 20;
    static final int columns = 30;
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String currentMove = "", previousMove = "";
        long startTime = System.currentTimeMillis();
        //code
        int board[][] = new int[rows][columns];
        Graph graphs = parseIntoTiles(board);
        long endTime = System.currentTimeMillis();
        System.err.println("Took "+(endTime - startTime) + " ms");
        List<PointXY> opponentsLocations = new ArrayList<PointXY>();
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            P = in.nextInt(); // your player number (0 to 3).
            int currentRow =0;
            int currentColumn = 0;
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                board[Y1][X1] = i+1;
                Optional<Node> beforeUpdatedNode =  graphs.getNode(new Node<Tile>(new Tile(Y1,X1,"0")));
                Node<Tile> converted = ((Node<Tile>) beforeUpdatedNode.get());
                converted.setData(new Tile(Y1,X1,(i+1)+""));

                //((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
                if(i == P){
                    System.err.println(X0+"-"+Y0+"-"+X1+"-"+Y1);
                    currentRow = Y1;
                    currentColumn = X1;
                }
                else{
                    opponentsLocations.add(new PointXY(Y1, X1));
                }
            }
            Graph graphMinMax = new Graph();
            graphMinMax = buildGraph(board,currentColumn,currentRow,opponentsLocations);
            String initialState = currentRow+"+"+currentColumn;
            for(PointXY eachOpponentLocation : opponentsLocations){
                initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
            }
            Node best = getBestMove(graphMinMax, new Node<MiniMaxState>(new MiniMaxState(board, 0 , initialState)), 4, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            //debugBoard(board);
            currentMove = avoidBlock(board, currentRow+"+"+currentColumn,previousMove);
//            int rowInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[0]);
//            int columnInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[1]);
//            int row = Integer.parseInt(((MiniMaxState) best.getData()).getRecentMoves().split("#")[P].split("\\+")[0]);
//            int column = Integer.parseInt(((MiniMaxState) best.getData()).getRecentMoves().split("#")[P].split("\\+")[1]);
//            currentMove = getMove(rowInitial,columnInitial,row,column);
            System.out.println(currentMove);
            previousMove = currentMove;
            opponentsLocations =  new ArrayList<PointXY>();
            // A single line with UP, DOWN, LEFT or RIGHT
        }
    }
    private static String getMove(int rowInitial, int columnInitial, int row, int column) {
        System.err.println(rowInitial + "-"+columnInitial + "-" + row + "-" + column);
        if(row < rowInitial ){
            return "UP";
        }
        else if(row > rowInitial ){
            return "DOWN";
        }
        else if(column < columnInitial ){
            return "LEFT";
        }
        else{
            return "RIGHT";
        }

    }
    public static int[][] deepCopyIntMatrix(int[][] input) {
        if (input == null)
            return null;
        int[][] result = new int[input.length][];
        for (int r = 0; r < input.length; r++) {
            result[r] = input[r].clone();
        }
        return result;
    }
    private static Graph buildGraph(int[][] board, int currentColumn, int currentRow,
                                    List<PointXY> opponentsLocations) {
        Graph graphMinMax = new Graph();
        String initialState = currentRow+"+"+currentColumn;
        for(PointXY eachOpponentLocation : opponentsLocations){
            initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
        }

        long startTime = System.currentTimeMillis();


        Node<MiniMaxState> start = new Node<MiniMaxState>(new MiniMaxState(board,0,initialState));
        Queue<Node<MiniMaxState>> frontier = new LinkedList<Node<MiniMaxState>>();
        frontier.add(start);


        TreeMap<String, String> possibleMoves = new TreeMap<String, String>();
        Node<MiniMaxState> lastNode = start;
        int level = 3;
        while(level > 0 && !frontier.isEmpty()){
            Node<MiniMaxState> currentNode = frontier.poll();
            if(currentNode.equals(lastNode)){
                lastNode = null;
                level -=1;
            }
            graphMinMax.addNode(currentNode);
            possibleMoves = generatePossibleMove(currentNode);
            graphMinMax = addPossibleStates(possibleMoves, graphMinMax,currentNode);

            int count = 0;

            for(Node<MiniMaxState> eachNode: graphMinMax.neighbors(currentNode)){
                frontier.add(eachNode);
                if(count == graphMinMax.neighbors(currentNode).size() - 1){
                    if(lastNode == null){
                        lastNode = eachNode;
                    }
                }
                count += 1;
            }
        }
        

        long endTime = System.currentTimeMillis();
        System.err.println("Took this much for graph build"+(endTime - startTime) + " ms");



        return graphMinMax;
    }

    private static TreeMap<String, String> generatePossibleMove(Node<MiniMaxState> start) {
        // TODO Auto-generated method stub
        String players[] = start.getData().getRecentMoves().split("#");
        TreeMap<String, String> possibleMoves = new TreeMap<String,String>();
        int i = 0;
        for(String eachPlayer : players){
            String x = eachPlayer.split("\\+")[0];
            String y = eachPlayer.split("\\+")[1];
            if(i == 0){
                possibleMoves = getPossibleMovesTreeMap(start.getData().getState(), x +"+"+ y );
            }else{
                possibleMoves = findPossibleCombinations(possibleMoves,getPossibleMovesTreeMap(start.getData().getState(),x +"+"+ y));
            }
            i++;
        }
        return possibleMoves;
    }
    private static Graph addPossibleStates(TreeMap<String, String> possibleMoves, Graph graphMinMax,
                                           Node<MiniMaxState> start) {
        int boardCopy[][];
        for(Map.Entry<String, String> entry1 :  possibleMoves.entrySet()){
            String playerMoves[]  = entry1.getValue().split("#");
            boardCopy = deepCopyIntMatrix(start.getData().getState());
            for(int i = 1; i <= playerMoves.length ; i++ ){
                int x = Integer.parseInt(playerMoves[i-1].split("\\+")[0]);
                int y = Integer.parseInt(playerMoves[i-1].split("\\+")[1]);
                boardCopy[x][y] = i;
            }
            Node<MiniMaxState> child = new Node<MiniMaxState>(new MiniMaxState(boardCopy, 0,entry1.getValue()));
            graphMinMax.addNode(child);
            graphMinMax.addEdge(new Edge(start,child,1));
        }
        return graphMinMax;
    }

    private static TreeMap<String, String> findPossibleCombinations(TreeMap<String, String> possibleMoves1,
                                                                    TreeMap<String, String> possibleMoves2) {
        TreeMap<String,String> result = new TreeMap<String, String>();
        for(Map.Entry<String, String> entry1 : possibleMoves1.entrySet()){
            for(Map.Entry<String, String> entry2 : possibleMoves2.entrySet()){
                String key = entry1.getKey() + "#"+entry2.getKey();
                String value = entry1.getValue() + "#" + entry2.getValue();
                result.put(key, value);
            }
        }

        return result;
    }

    private static Graph parseIntoTiles(int[][] board) {
        Graph graph = new Graph();
        //to add the nodes
        for(int i = 0;i< board.length ;i++){
            for(int j = 0;j< board[0].length ;j++){
                tileMap.put(new PointXY(i,j),new Tile(i,j,"0"));
                graph.addNode(new Node<Tile>(new Tile(i,j,"0")));
            }
        }

        for (Map.Entry<PointXY,Tile> entry : tileMap.entrySet())
        {
            pushEdges(graph,entry.getValue(),"N",entry.getKey().getX(),entry.getKey().getY());
            pushEdges(graph,entry.getValue(),"E",entry.getKey().getX(),entry.getKey().getY());
            pushEdges(graph,entry.getValue(),"S",entry.getKey().getX(),entry.getKey().getY());
            pushEdges(graph,entry.getValue(),"W",entry.getKey().getX(),entry.getKey().getY());
        }
        return graph;
    }

    private static void pushEdges(Graph graph,Tile fromTile, String direction,int row,int column) {
        int x;
        int y;
        PointXY newCoord;
        Tile newTile;
        switch(direction){
            case "N" :
                x = fromTile.getX();
                y = fromTile.getY()-1;
                newCoord = new PointXY(x,y);
                newTile =(Tile) tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }
                break;
            case "S" :
                x = fromTile.getX();
                y = fromTile.getY()+1;
                newCoord = new PointXY(x,y);
                newTile =(Tile) tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }

                break;
            case "E" :

                x = fromTile.getX()+1;
                y = fromTile.getY();
                newCoord = new PointXY(x,y);
                newTile = tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }

                break;
            case "W" :
                x = fromTile.getX()-1;
                y = fromTile.getY();
                newCoord = new PointXY(x,y);
                newTile = tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }
                break;

        }

    }
    private static String avoidBlock(int[][] board, String currentXY,String previousMove) {
        // TODO Auto-generated method stub
        HashMap<String , String > possibleMoves = getPossibleMoves(board,currentXY);
        if(possibleMoves.containsKey(previousMove)){
            return previousMove;
        }
        for(String eachPossibleMove: possibleMoves.keySet()){
            return eachPossibleMove;
        }
        return "Right";
    }
    private static TreeMap<String,String> getPossibleMovesTreeMap(int[][] board, String currentXY) {
        TreeMap<String,String> possibleMoves = new TreeMap<String, String>();
        String XY[] = currentXY.split("\\+");
        int row = Integer.parseInt(XY[0]);
        int column = Integer.parseInt(XY[1]);

        if(row + 1 < rows && board[row +1][column] < 1){
            possibleMoves.put("DOWN",(row+1)+"+"+column);
        }
        if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
            possibleMoves.put("UP",(row-1)+"+"+column);
        }
        if(column + 1 < columns && board[row][column + 1] < 1){
            possibleMoves.put("RIGHT",row+"+"+(column+1));
        }
        if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
            possibleMoves.put("LEFT",row+"+"+(column-1));
        }

        return possibleMoves;
    }
    private static HashMap<String,String> getPossibleMoves(int[][] board, String currentXY) {
        HashMap<String,String> possibleMoves = new HashMap<String, String>();
        String XY[] = currentXY.split("\\+");
        int row = Integer.parseInt(XY[0]);
        int column = Integer.parseInt(XY[1]);

        if(row + 1 < rows && board[row +1][column] < 1){
            possibleMoves.put("DOWN",(row+1)+"+"+column);
        }
        if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
            possibleMoves.put("UP",(row-1)+"+"+column);
        }
        if(column + 1 < columns && board[row][column + 1] < 1){
            possibleMoves.put("RIGHT",row+"+"+(column+1));
        }
        if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
            possibleMoves.put("LEFT",row+"+"+(column-1));
        }

        return possibleMoves;
    }
    private static String getFirstPossibleMove(int[][] board, String currentXY) {
        HashMap<String,String> possibleMoves = new HashMap<String, String>();
        String XY[] = currentXY.split("\\+");
        int row = Integer.parseInt(XY[0]);
        int column = Integer.parseInt(XY[1]);

        if(row + 1 < rows && board[row +1][column] < 1){
            return "Down";
        }
        else if((row -1 < rows && row -1 >= 0) && board[row  - 1][column] < 1){
            return "UP";
        }
        else if(column + 1 < columns && board[row][column + 1] < 1){
            return "RIGHT";
        }
        else if((column - 1 < columns && column - 1 >= 0) && board[row][column - 1] < 1){
            return "LEFT";
        }
        return null;
    }

    private static void debugBoard(int[][] Board){
        for(int i=0; i < 20;i ++){
            for(int j=0; j < 30;j ++){
                System.err.print(Board[i][j]+ " ");
            }
            System.err.println();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Node getBestMove(Graph graph, Node<MiniMaxState> source, Integer depth, Integer alpha, Integer beta, Boolean max) {
        // TODO: implement your alpha beta pruning algorithm here

        if(rootNode == null){
            rootNode = source;
        }
        Node bestValue;
        if (depth == 0 || graph.neighbors(source).size() == 0) {
//	            evaluvateState(source);
//	            int value = floodFillCount;
//	            source.getData().setValue(value);
            return source; // return a number
        }

        if (max) {
            bestValue =new Node<>(new MiniMaxState(null, Integer.MIN_VALUE,"")); // negative infinite
            for (Node eachNode: graph.neighbors(source)) {
                Node value = getBestMove(graph,eachNode, depth - 1, alpha, beta, !max);
                if(alpha < ((MiniMaxState)value.getData()).getValue() ){
                    alpha = ((MiniMaxState)value.getData()).getValue();
                    if(beta < alpha){
                        bestValue = compareNodesMin(bestValue, value);
                        break;
                    }
                }
                bestValue = compareNodesMax(bestValue, value);
            }
            if( !(((MiniMaxState) source.getData()).getRecentMoves()).equals(((MiniMaxState) rootNode.getData()).getRecentMoves())){
                bestValue = new Node(new MiniMaxState( ((MiniMaxState) source.getData()).getState() , ((MiniMaxState)bestValue.getData()).getValue(), ((MiniMaxState) source.getData()).getRecentMoves() ));
            }
            ((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
            return bestValue;
        } else {
            bestValue =new Node<>(new MiniMaxState(null, Integer.MAX_VALUE,"")); // positive infinite
            for (Node eachNode: graph.neighbors(source)) {
                Node value = getBestMove(graph,eachNode, depth - 1, alpha, beta, !max);
                if(beta > ((MiniMaxState)value.getData()).getValue() ){
                    beta = ((MiniMaxState)value.getData()).getValue();
                    if(beta < alpha){
                        bestValue = compareNodesMin(bestValue, value);
                        break;
                    }
                }
                bestValue = compareNodesMin(bestValue, value);
            }
            if( !(((MiniMaxState) source.getData()).getRecentMoves()).equals(((MiniMaxState) rootNode.getData()).getRecentMoves())){
                bestValue = new Node(new MiniMaxState( ((MiniMaxState) source.getData()).getState() , ((MiniMaxState)bestValue.getData()).getValue(), ((MiniMaxState) source.getData()).getRecentMoves() ));
            }
            ((MiniMaxState) graph.getNode(source).get().getData()).setValue(((MiniMaxState)bestValue.getData()).getValue());
            return bestValue;
        }

    }

    private static int evaluvateState(Node<MiniMaxState> source) {
        int x = Integer.parseInt(((source.getData().getRecentMoves()).split("#")[P]).split("\\+")[0]);
        int y = Integer.parseInt(((source.getData().getRecentMoves()).split("#")[P]).split("\\+")[1]);
        String firstMove =  getFirstPossibleMove(source.getData().getState(), x+"+"+y);
        floodFillCount = 0;
        if(firstMove.equals("Down")){
            floodFill(x + 1,y,firstMove,source.getData().getState());
        }
        else if(firstMove.equals("UP")){
            floodFill(x - 1,y,firstMove,source.getData().getState());
        }
        else if(firstMove.equals("RIGHT")){
            floodFill(x,y + 1,firstMove,source.getData().getState());
        }
        else{
            floodFill(x,y - 1,firstMove,source.getData().getState());
        }
        return floodFillCount;
    }
    public static void floodFill( int x, int y, String move,int[][] board) {
        if ( (x >=0 && x < 20 && y >=0 && y <30) && (board[x][y] == 0) ) {
            board[x][y] = P +1;
            floodFillCount++;
            // System.err.println("FLOOD FILL COUNT: " + Player.floodFillCount);

            floodFill( x, y -1, "LEFT" ,board);
            floodFill( x+1, y, "DOWN",board);
            floodFill( x-1, y, "UP",board);
            floodFill( x, y+1, "RIGHT" ,board);
        } else {
            // System.err.println("No move");
            //System.err.println("FLOOD FILL COUNT: " + floodFillCount);
            return;
        }
    }
    private static Node compareNodesMin(Node<MiniMaxState> bestValue, Node<MiniMaxState> value) {
        if(((MiniMaxState) bestValue.getData()).getValue() < value.getData().getValue()){
            return bestValue;
        }
        return value;
    }

    private static Node<MiniMaxState> compareNodesMax(Node<MiniMaxState> bestValue, Node<MiniMaxState> value) {
        if(( bestValue.getData()).getValue() > value.getData().getValue()){
            return bestValue;
        }
        return value;
    }
}

class PointXY {
    final int x;
    final int y;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public PointXY(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof PointXY)) {
            return false;
        }

        PointXY coord = (PointXY) o;

        return  coord.x == x && coord.y == y;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + x;
        result = 31 * result + y;
        return result;
    }
}

class Edge {
    private Node from;
    private Node to;
    private int value;

    public Edge(Node from, Node to, int value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;

        if (getValue() != edge.getValue()) return false;
        if (getFrom() != null ? !getFrom().equals(edge.getFrom()) : edge.getFrom() != null)
            return false;
        return !(getTo() != null ? !getTo().equals(edge.getTo()) : edge.getTo() != null);

    }

    @Override
    public String toString() {
        return "Edge{" +
                "from=" + from +
                ", to=" + to +
                ", value=" + value +
                '}';
    }

    @Override
    public int hashCode() {
        int result = getFrom() != null ? getFrom().hashCode() : 0;
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        result = 31 * result + getValue();
        return result;
    }
}

class Tile {
    private final int x;
    private final int y;
    private final String type;

    public Tile(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (getX() != tile.getX()) return false;
        if (getY() != tile.getY()) return false;
        return getType() != null ? getType().equals(tile.getType()) : tile.getType() == null;

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
}
class MiniMaxState {
    private int[][] state;
    private String recentMoves;
    private int value;

    public MiniMaxState(int[][] state, int value, String recentMoves) {
        this.state = state;
        this.value = value;
        this.recentMoves = recentMoves;
    }

    public String getRecentMoves() {
        return recentMoves;
    }

    public void setRecentMoves(String recentMoves) {
        this.recentMoves = recentMoves;
    }

    public void setState(int[][] state) {
        this.state = state;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[][] getState() {
        return state;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MiniMaxState)) return false;

        MiniMaxState that = (MiniMaxState) o;

        return Arrays.deepEquals(getState(), that.getState());
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(getState());
    }
}
class Node<T>{
    private T data;
    //private final int value;


    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node<?> node = (Node<?>) o;

        return getData() != null ? getData().equals(node.getData()) : node.getData() == null;

    }

    @Override
    public int hashCode() {
        return getData() != null ? getData().hashCode() : 0;
    }
}

class Graph  {
    private Map<Node, Collection<Edge>> adjacencyList;





    protected Graph() {
        adjacencyList = new HashMap<>();
    }

    public boolean adjacent(Node x, Node y) {

        Collection<Edge> edges = adjacencyList.get(x);

        for (Edge edge : edges) {
            if (edge.getTo().equals(y)) {
                return true;
            }

        }

        return false;
    }

    public List<Node> neighbors(Node x) {
        ArrayList<Node> neighbors = new ArrayList<>();
        if(adjacencyList.containsKey(x)) {
            Collection<Edge> edges = adjacencyList.get(x);

            for (Edge edge : edges) {
                neighbors.add(edge.getTo());
            }
        }

        return  neighbors;
    }

    public boolean addNode(Node x) {
        if(adjacencyList.containsKey(x)) {
            return false;
        }

        adjacencyList.put(new Node(x), new ArrayList<Edge>());
        return true;
    }

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

    public boolean addEdge(Edge x) {
        if(adjacencyList.get(x.getFrom()).contains(x)){
            return false;
        }

        adjacencyList.get(x.getFrom()).add(x);
        return true;
    }

    public boolean removeEdge(Edge x) {
        if(!adjacencyList.get(x.getFrom()).contains(x)){
            return false;
        }
        adjacencyList.remove(x.getFrom());
        return true;
    }

    public int distance(Node from, Node to) {
        if(adjacencyList.containsKey(from)) {
            Collection<Edge> edges = adjacencyList.get(from);
            for (Edge edge : edges) {
                if (edge.getTo().equals(to)) {
                    return edge.getValue();
                }
            }
        }
        return 0;

    }

    public Optional<Node> getNode(int index) {
        return null;
    }


    public Collection<Node> getNodes() {
        return adjacencyList.keySet();
    }

    public Collection<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        for (Node node: adjacencyList.keySet()) {

            edges.addAll(adjacencyList.get(node));
        }

        // private Map<Node, Collection<Edge>> adjacencyList;
//        System.out.println("\n\nADJ LIST RETURNING: " + edges.size());

        return  edges;
    }


    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = adjacencyList.keySet().iterator();
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
