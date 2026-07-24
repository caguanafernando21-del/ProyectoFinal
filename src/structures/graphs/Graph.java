package structures.graphs;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import structures.node.Node;

public class Graph<T> {
        private Map<Node<T>, Set<Node<T>>> graph;

    public Graph(){
        this.graph = new HashMap<Node<T>, Set<Node<T>>>();
    }

    public void add(T data){
        Node<T> node = new Node<T>(data);
        graph.putIfAbsent(node, new HashSet<Node<T>>());
    }

    public void addConection(T v1, T v2){
        Node<T> nv1 = new Node<T>(v1);
        Node<T> nv2 = new Node<T>(v2);

        add(v1);
        add(v2);

        graph.get(nv1).add(nv2);
        graph.get(nv2).add(nv1);
    }

    public void addConectionUni(T v1, T v2){
        Node<T> nv1 = new Node<T>(v1);
        Node<T> nv2 = new Node<T>(v2);

        this.graph.putIfAbsent(nv1, new HashSet<>());
        this.graph.putIfAbsent(nv2, new HashSet<>());
        this.graph.get(nv1).add(nv2);
    }
    
    public void printGraph(){
        for (Map.Entry<Node<T>, Set<Node<T>>> entry : graph.entrySet()){
            System.out.println(entry.getKey() + " -> ");
            for (Node<T> coneccion : entry.getValue()){
                System.out.println(coneccion);
            }
            System.out.println();
        } 
    }

    public Set<Node<T>> getVecinos(T current) {
        return graph.getOrDefault(new Node<T>(current), new HashSet<Node<T>>());
    }

}
