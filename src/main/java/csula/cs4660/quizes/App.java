package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Here is your quiz entry point and your app
 */
public class App {
    public static void main(String[] args) {

        State dist = Client.getState("e577aa79473673f6158cc73e0e5dc122").get();
        State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();

        Queue<State> queue = new LinkedList<>();
        HashMap<State, DTO> roots = new HashMap<State, DTO>();
        HashMap<DTO, State> parents = new HashMap<DTO, State>();
        Collection<State> visitedNodes = new LinkedList<>();

        roots.put(initialState, null);
        queue.offer(initialState);
        visitedNodes.add(initialState);


        while (!queue.isEmpty()) {
            State curNode = queue.poll();

            for (State child : curNode.getNeighbors()) {
                if (!visitedNodes.contains(child)) {
                    DTO dto = Client.stateTransition(curNode.getId(), child.getId()).get();
                    child = Client.getState(child.getId()).get();
                    roots.put(child, dto);
                    parents.put(dto, curNode);
//                    System.out.println(child + ":::::::::::" + dto);
                    queue.add(child);
                    visitedNodes.add(child);
                }
            }
        }
//        System.out.println("Done with BFS");

        List <DTO> path = getPath(roots, dist, parents);
//        System.out.println("Got all DTOs");
        for(DTO dto: path){
            System.out.println(parents.get(dto).getLocation().getName() + ":" +
                    dto.getAction() + ":" + dto.getEvent().getEffect() + " --> " + dto.getId());
        }
    }


        public static List<DTO> getPath(HashMap<State, DTO> roots, State dist, HashMap<DTO, State> parents){
            List<DTO> path = new LinkedList<>();
            DTO currentEdge = roots.get(dist);
            while(currentEdge != null){
                path.add(currentEdge);
//                System.out.println("added one...");
                currentEdge = roots.get(parents.get(currentEdge));
//                System.out.println("got next one...");
            }

            Collections.reverse(path);
            return  path;
        }

////         to get a state, you can simply call `Client.getState with the id`
//        State intialState = Client.getState("6817508e23d48bda345f3b84bfcb8652").get();
//        System.out.println(intialState.getLocation().getName());
//
//        // to get an edge between state to its neighbor, you can call stateTransition
////        System.out.println("Now " + Client.stateTransition(state.getId(), state.getNeighbors()[0].getId()));
////        DTO dto = Client.stateTransition(state.getId(), state.getNeighbors()[0].getId()).get();
////        System.out.println(dto.getAction() + ":" + dto.getEvent().getEffect());
//        for(State child: intialState.getNeighbors()){
//            System.out.println("Child: " + child.getId());
//        }

//    }


}
