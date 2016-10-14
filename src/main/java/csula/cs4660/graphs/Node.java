package csula.cs4660.graphs;

/**
 * The fundamental class to hold data
 *
 * We will be using Generic Programming to hold dynamic type of data --
 * http://www.tutorialspoint.com/java/java_generics.htm
 */
public class Node<T> {

    public int distance;
    public Node parent;

    private final T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
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
//package csula.cs4660.graphs;
//
///**
// * The fundamental class to hold data
// *
// * We will be using Generic Programming to hold dynamic type of data --
// * http://www.tutorialspoint.com/java/java_generics.htm
// */
//public class Node<T> {
//    private final T data;
//
//    public Node(T data) {
//        this.data = data;
//    }
//
//    public T getData() {
//        return data;
//    }
//
//    @Override
//    public String toString() {
//        return "Node{" +
//            "data=" + data +
//            '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof Node)) {
//            System.out.println("NO EQUAL!!!!!!!!");
//            return false;
//        }
//
//        Node<?> node = (Node<?>) o;
//
////        if(!getData().toString().equals(node.getData().toString())){
//////            System.out.println("ITs EMPTYYYYY wrong!!!!!!!!");
//////
//////            System.out.println(getData().toString() + ":" + node.getData().toString());
////            return false;
////        }
//
//
////    return getData().toString().equals(node.getData().toString());
//        return getData() != null ? getData().toString().equals(node.getData().toString()) : node.getData() == null;
//
//    }
//
//    @Override
//    public int hashCode() {
//        return getData() != null ? getData().hashCode() : 0;
//    }
//}
