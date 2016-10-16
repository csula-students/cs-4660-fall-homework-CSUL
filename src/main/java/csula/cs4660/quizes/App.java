package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.State;

import java.util.*;

/**
 * Here is your quiz entry point and your app
 */
public class App {
    public static void main(String[] args) {

        State dist = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        State initialState = Client.getState("e577aa79473673f6158cc73e0e5dc122").get();

        Queue<State> queue = new LinkedList<>();
        HashMap<State, DTO> roots = new HashMap<State, DTO>();

        Collection<State> visitedNodes = new LinkedList<>();

        roots.put(initialState, null);
        queue.offer(initialState);
        visitedNodes.add(initialState);


        while (!queue.isEmpty()) {
            State curNode = queue.poll();
            curNode = Client.getState(curNode.getId()).get();
//            System.out.println(curNode.getId() + " neighbor length is: " + curNode.getNeighbors().length + "\n");

            for (State child : curNode.getNeighbors()) {
                if (!visitedNodes.contains(child)) {
                    DTO dto = Client.stateTransition(curNode.getId(), child.getId()).get();
                    roots.put(child, dto);
//                    System.out.println(child + ":::::::::::" + dto);
                    queue.add(child);
                    visitedNodes.add(child);
                }
            }
        }

        List <DTO> path = getPath(roots, dist);
        for(DTO dto: path){
            System.out.println(Client.getState(dto.getId()).get().getLocation().getName() + ":" +
                    dto.getAction() + ":" + dto.getEvent().getEffect());
        }
    }


        public static List<DTO> getPath(HashMap<State, DTO> roots, State dist){
            List<DTO> path = new LinkedList<>();
            DTO currentEdge = roots.get(dist);
            while(currentEdge != null){
                path.add(currentEdge);
                currentEdge = roots.get(Client.getState(currentEdge.getId()));

            }

            Collections.reverse(path);
            return  path;
        }

//         to get a state, you can simply call `Client.getState with the id`
//        State state = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
//        System.out.println(state.getLocation().getName());
//
//        // to get an edge between state to its neighbor, you can call stateTransition
////        System.out.println("Now " + Client.stateTransition(state.getId(), state.getNeighbors()[0].getId()));
//        DTO dto = Client.stateTransition(state.getId(), state.getNeighbors()[0].getId()).get();
//        System.out.println(dto.getAction() + ":" + dto.getEvent().getEffect());

//    }


}
