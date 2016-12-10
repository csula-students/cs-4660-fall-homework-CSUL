package csula.cs4660.games;

import java.util.*;

class Player {

    static Node src = null;
    static List<Node<Tile>> visitedNodes;
    static Map <Coordinate,Tile> tileMap = new HashMap<Coordinate,Tile>();
    static int P;

    static final int rows = 20;
    static final int columns = 30;
    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        String currentMove = null, previousMove = "";


        int board[][] = new int[rows][columns];


        Graph graphs = parseIntoTiles(board);

        List<Coordinate> opponentsLocations = new ArrayList<Coordinate>();
        while (true) {
            int N = in.nextInt();
            P = in.nextInt();
            int currentRow =0;
            int currentColumn = 0;
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt();
                int Y0 = in.nextInt();
                int X1 = in.nextInt();
                int Y1 = in.nextInt();

                if(X0 != -1){
                    board[Y1][X1] = i+1;



                    Optional<Node> beforeUpdatedNode =  graphs.getNode(new Node<Tile>(new Tile(Y1,X1,"0")));
                    Node<Tile> afterUpdateNode = ((Node<Tile>) beforeUpdatedNode.get());
                    afterUpdateNode.setData(new Tile(Y1,X1,(i+1)+""));
                    if(i == P){
                        currentRow = Y1;
                        currentColumn = X1;
                    }
                    else{
                        opponentsLocations.add(new Coordinate(Y1, X1));
                    }
                }
            }
            String initialState = currentRow+"+"+currentColumn;
            for(Coordinate eachOpponentLocation : opponentsLocations){
                initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
            }
            src = null;
            if(isEnemyClose( new Coordinate(currentRow, currentColumn), opponentsLocations , 7 )){
                Graph graphMinMax = new Graph();
                graphMinMax = buildGraph(board,currentColumn,currentRow,opponentsLocations);
                Node best = getBestMove(graphMinMax, new Node<MiniMaxState>(new MiniMaxState(board, 0 , initialState)), 4, true);
                int rowInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[0]);
                int columnInitial = Integer.parseInt(initialState.split("#")[P].split("\\+")[1]);
                int row = Integer.parseInt(((MiniMaxState) best.getData()).getMoves().split("#")[P].split("\\+")[0]);
                int column = Integer.parseInt(((MiniMaxState) best.getData()).getMoves().split("#")[P].split("\\+")[1]);
                currentMove = getMove(rowInitial,columnInitial,row,column);
            }else{
                Optional<Node> currentNode =  graphs.getNode(new Node<Tile>(new Tile(currentRow,currentColumn,(P+1)+"")));
                Node currentNodeOfPlayer = (Node) currentNode.get();
                Node<Tile> longestPathNode = null;
                int max = 0;
                for(Node<Tile> eachNode : graphs.neighbors(new Node<Tile>(new Tile(currentRow,currentColumn,(P+1)+"")))){
                    visitedNodes =  new ArrayList<Node<Tile>>();
                    int lengthOfTheChildNode = getLongestPathNode(graphs,eachNode);
                    if(max < lengthOfTheChildNode){
                        max = lengthOfTheChildNode;
                        longestPathNode = eachNode;
                    }
                }
                currentMove = getMove(currentRow,currentColumn,longestPathNode.getData().getX(),longestPathNode.getData().getY());
            }


            if(currentMove.equals("")){
                currentMove = avoidBlock(board, currentRow+"+"+currentColumn,previousMove);
            }
            System.out.println(currentMove);
            previousMove = currentMove;
            opponentsLocations =  new ArrayList<Coordinate>();

        }
    }
    private static boolean isEnemyClose(Coordinate myLocation, List<Coordinate> opponentsLocations, int radius) {
        for(Coordinate eachOpponentLocation : opponentsLocations){
            int dx = Math.abs(myLocation.getX() - eachOpponentLocation.getX());
            int dy = Math.abs(myLocation.getY() - eachOpponentLocation.getY());
            if((dx + dy) < 12 ){
                return true;
            }
        }

        return false;
    }
    private static int getLongestPathNode(Graph graphs,Node<Tile> eachNode) {
        visitedNodes.add(eachNode);
        if(! hasUnvisitedNode(graphs.neighbors(eachNode))){
            return 0;
        }else{
            int maxDepth = 0;

            for(Node<Tile> child : graphs.neighbors(eachNode)){
                if(!visitedNodes.contains(child) && child.getData().getType() == "0" ){
                    maxDepth = Math.max(maxDepth, getLongestPathNode(graphs,child));
                }
            }
            return maxDepth + 1;
        }
    }
    private static boolean hasUnvisitedNode(List<Node> neibhourNodes) {
        for(Node<Tile> eachNode : neibhourNodes){
            if(! visitedNodes.contains(eachNode)){
                return true;
            }
        }
        return false;
    }
    private static String getMove(int rowInitial, int columnInitial, int row, int column) {
        if(row < rowInitial ){
            return "UP";
        }
        else if(row > rowInitial ){
            return "DOWN";
        }
        else if(column < columnInitial ){
            return "LEFT";
        }
        else if (column > columnInitial ){
            return "RIGHT";
        }
        else{
            return "";
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
                                    List<Coordinate> opponentsLocations) {
        Graph graphMinMax = new Graph();
        String initialState = currentRow+"+"+currentColumn;
        for(Coordinate eachOpponentLocation : opponentsLocations){
            initialState += "#" + eachOpponentLocation.getX() + "+" + eachOpponentLocation.getY();
        }


        Node<MiniMaxState> start = new Node<MiniMaxState>(new MiniMaxState(board,0,initialState));
        Queue<Node<MiniMaxState>> frontier = new LinkedList<Node<MiniMaxState>>();
        frontier.add(start);


        TreeMap<String, String> possibleMoves = new TreeMap<String, String>();
        Node<MiniMaxState> lastNode = start;
        int level = 1;
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





        return graphMinMax;
    }

    private static TreeMap<String, String> generatePossibleMove(Node<MiniMaxState> start) {
        // TODO Auto-generated method stub
        String players[] = start.getData().getMoves().split("#");
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
                tileMap.put(new Coordinate(i,j),new Tile(i,j,"0"));
                graph.addNode(new Node<Tile>(new Tile(i,j,"0")));
            }
        }

        for (Map.Entry<Coordinate,Tile> entry : tileMap.entrySet())
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
        Coordinate newCoord;
        Tile newTile;
        switch(direction){
            case "N" :
                x = fromTile.getX();
                y = fromTile.getY()-1;
                newCoord = new Coordinate(x,y);
                newTile =(Tile) tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }
                break;
            case "S" :
                x = fromTile.getX();
                y = fromTile.getY()+1;
                newCoord = new Coordinate(x,y);
                newTile =(Tile) tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }

                break;
            case "E" :

                x = fromTile.getX()+1;
                y = fromTile.getY();
                newCoord = new Coordinate(x,y);
                newTile = tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }

                break;
            case "W" :
                x = fromTile.getX()-1;
                y = fromTile.getY();
                newCoord = new Coordinate(x,y);
                newTile = tileMap.get(newCoord);
                if(tileMap.containsKey(newCoord)){
                    graph.addEdge(new Edge(new Node<Tile>(fromTile),new Node<Tile>(newTile),1));
                }
                break;

        }

    }
    private static String avoidBlock(int[][] board, String currentXY,String previousMove) {
        // TODO Auto-generated method stub
        return getPossibleMoves(board,currentXY);


    }
    private static TreeMap<String,String> getPossibleMovesTreeMap(int[][] board, String currentXY) {
        TreeMap<String,String> possibleMoves = new TreeMap<String, String>();
        String XY[] = currentXY.split("\\+");
        int row = Integer.parseInt(XY[0]);
        int column = Integer.parseInt(XY[1]);

        if(row + 1 < rows && board[row +1][column] < 1){
            possibleMoves.put("DOWN",(row+1)+"+"+column);
        }
        if(row -1 >= 0 && board[row  - 1][column] < 1){
            possibleMoves.put("UP",(row-1)+"+"+column);
        }
        if(column + 1 < columns && board[row][column + 1] < 1){
            possibleMoves.put("RIGHT",row+"+"+(column+1));
        }
        if(column - 1 >= 0 && board[row][column - 1] < 1){
            possibleMoves.put("LEFT",row+"+"+(column-1));
        }
        return possibleMoves;
    }
    private static  String getPossibleMoves(int[][] board, String currentXY) {
        HashMap<String,String> possibleMoves = new HashMap<String, String>();
        String XY[] = currentXY.split("\\+");
        int row = Integer.parseInt(XY[0]);
        int column = Integer.parseInt(XY[1]);

        if(row + 1 < rows && board[row +1][column] < 1){
            possibleMoves.put("DOWN",(row+1)+"+"+column);
        }
        if(row -1 >= 0  && board[row  - 1][column] < 1){
            possibleMoves.put("UP",(row-1)+"+"+column);
        }
        if(column + 1 < columns && board[row][column + 1] < 1){
            possibleMoves.put("RIGHT",row+"+"+(column+1));
        }
        if(column - 1 >= 0 && board[row][column - 1] < 1){
            possibleMoves.put("LEFT",row+"+"+(column-1));
        }
        String result = possibleMoves.keySet().iterator().next();
        return result;
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
    public static Node getBestMove(Graph graph, Node<MiniMaxState> root, Integer depth, Boolean max) {
        int minimum = Integer.MIN_VALUE;
        int maximum = Integer.MAX_VALUE;



        if (depth == 0 || graph.neighbors(root).size() == 0) {

            int value =evaluvateState(root);
            root.getData().setValue(value);
            return root;
        }

        if(max){

            Node  best = new Node<>(new MiniMaxState(null, Integer.MIN_VALUE,""));
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

            Node best = new Node<>(new MiniMaxState(null, Integer.MAX_VALUE,""));
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



    private static int evaluvateState(Node<MiniMaxState> source) {

        Queue<String> frontier = new LinkedList<>();
        int playerCountCellCount,otherPlayersCellCount,wall = 9;
        playerCountCellCount = otherPlayersCellCount = 0;
        int [][] currentBoardState = deepCopyIntMatrix(source.getData().getState());
        String playerCurrentLocation[] = source.getData().getMoves().split("#");
        HashMap<String, Integer> cellWithParents = new HashMap<>();
        int i = 0;
        for(String eachPlayerLocation : playerCurrentLocation){
            if(! cellWithParents.containsKey(eachPlayerLocation)){
                cellWithParents.put(eachPlayerLocation, i+1);
            }
            frontier.add(eachPlayerLocation);
            i++;
        }

        while(! frontier.isEmpty()){
            String key = frontier.poll();
            String rowColumn[] = key.split("\\+");
            int x = Integer.parseInt(rowColumn[0]);
            int y = Integer.parseInt(rowColumn[1]);

            if(x + 1 < rows && (currentBoardState[x + 1][y] == 0 || currentBoardState[x + 1][y] > 4 && currentBoardState[x + 1][y] <= 20)){
                String newKey = (x+1) + "+" + y;
                if(currentBoardState[x + 1][y] == 0){
                    cellWithParents.put(newKey, cellWithParents.get(key));
                    currentBoardState[x+1][y] = cellWithParents.get(key) + 4;
                    if(P + 1 + 4 == currentBoardState[x+1][y]){
                        playerCountCellCount += 1;
                    }else{
                        otherPlayersCellCount += 1;
                    }
                    frontier.add(newKey);
                }else{
                    if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                        if(currentBoardState[x + 1][y] > 4 && currentBoardState[x + 1][y] != wall){
                            if(P + 1 + 4 == currentBoardState[x + 1][y]){
                                playerCountCellCount -= 1;
                            }else{
                                otherPlayersCellCount -= 1;
                            }
                            currentBoardState[x + 1][y] = wall;
                        }
                    }
                }
            }
            if(x - 1 >= 0 && (currentBoardState[x - 1][y] == 0 || currentBoardState[x - 1][y] > 4 && currentBoardState[x - 1][y] <= 20)){
                String newKey = (x-1) + "+" + y;
                if(currentBoardState[x - 1][y] == 0){
                    cellWithParents.put(newKey, cellWithParents.get(key));
                    currentBoardState[x-1][y] = cellWithParents.get(key) + 4;
                    if(P + 1 + 4 == currentBoardState[x-1][y]){
                        playerCountCellCount += 1;
                    }else{
                        otherPlayersCellCount += 1;
                    }
                    frontier.add(newKey);
                }else{
                    if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                        if(currentBoardState[x - 1][y] > 4 && currentBoardState[x - 1][y] != wall){
                            if(P + 1 + 4 == currentBoardState[x-1][y]){
                                playerCountCellCount -= 1;
                            }else{
                                otherPlayersCellCount -= 1;
                            }
                            currentBoardState[x - 1][y] = wall;
                        }
                    }
                }
            }
            if(y + 1 < columns && (currentBoardState[x][y + 1] == 0 || currentBoardState[x][y + 1] > 4 && currentBoardState[x][y + 1] <= 20)){
                String newKey = x + "+" + (y + 1);
                if(currentBoardState[x][y+1] == 0){
                    cellWithParents.put(newKey, cellWithParents.get(key));
                    currentBoardState[x][y+1] = cellWithParents.get(key) + 4;
                    if(P + 1 + 4 == currentBoardState[x][y + 1]){
                        playerCountCellCount += 1;
                    }else{
                        otherPlayersCellCount += 1;
                    }
                    frontier.add(newKey);
                }else{
                    if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                        if(currentBoardState[x][y + 1] > 4  && currentBoardState[x][y + 1] != wall){
                            if(P + 1 + 4 == currentBoardState[x][y + 1]){
                                playerCountCellCount -= 1;
                            }else{
                                otherPlayersCellCount -= 1;
                            }
                            currentBoardState[x][y + 1] = wall;
                        }
                    }
                }
            }
            if(y - 1 >= 0 && (currentBoardState[x][y - 1] == 0 || currentBoardState[x][y - 1] > 4 && currentBoardState[x][y - 1] <= 20)){
                String newKey = x + "+" + (y - 1);
                if(currentBoardState[x][y-1] == 0){
                    cellWithParents.put(newKey, cellWithParents.get(key));
                    currentBoardState[x][y-1] = cellWithParents.get(key) + 4;
                    if(P + 1 + 4 == currentBoardState[x][y - 1]){
                        playerCountCellCount += 1;
                    }else{
                        otherPlayersCellCount += 1;
                    }
                    frontier.add(newKey);
                }else{
                    if(cellWithParents.get(newKey) != cellWithParents.get(key)){
                        if(currentBoardState[x][y-1] > 4  && currentBoardState[x][y - 1] != wall){
                            if(P + 1 + 4 == currentBoardState[x][y - 1]){
                                playerCountCellCount -= 1;
                            }else{
                                otherPlayersCellCount -= 1;
                            }
                            currentBoardState[x][y-1] = wall;
                        }
                    }
                }
            }

        }

        return playerCountCellCount - otherPlayersCellCount;
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

class Coordinate {
    final int x;
    final int y;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public Coordinate(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate coord = (Coordinate) o;

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
        return result;
    }
}
class MiniMaxState {
    private int[][] state;
    private String moves;
    private int value;

    public MiniMaxState(int[][] state, int value, String recentMoves) {
        this.state = state;
        this.value = value;
        this.moves = recentMoves;
    }

    public String getMoves() {
        return moves;
    }

    public void setMoves(String recentMoves) {
        this.moves = recentMoves;
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


class Graph {
    private Map<Node, Collection<Edge>> adjacencyList;




    public Graph() {
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

        adjacencyList.put(x, new ArrayList<Edge>());
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