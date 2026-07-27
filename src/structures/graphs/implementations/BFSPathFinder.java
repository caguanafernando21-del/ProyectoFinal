package structures.graphs.implementations;
import listeners.PathListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class BFSPathFinder<T> implements PathFinder<T>{

    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end, PathListener<T> listener) {
        Queue<T> queue = new LinkedList<>();
        LinkedHashSet<T> visitados = new LinkedHashSet<>();
        Map<T, T> parent = new LinkedHashMap<>();
        LinkedHashSet<T> visited = new LinkedHashSet<>();

        queue.add(start);
        visitados.add(start);
        parent.put(start, null);
        while(!queue.isEmpty()) {
            T current = queue.poll(); // Saca el valor que se encuentra primero en la cola
            visited.add(current); //Añade el valor sacado de la cola al LinkedHashSet del visited
            if(listener != null){
                listener.onNodeVisited(current);
            }
            if(current.equals(end)) { // pregunta si el valor sacado de la cola es igual al valor final que buscamos  (end)

                return new PathResult<>(visited, buildPath(parent, end));
            }
            //
            for(Node<T> vecino : graph.getVecinos(current)) { // vecino  pasa a ser los valores que conoce el nodo
                T valorVecino = vecino.getValue();
                if(!visitados.contains(valorVecino)) { //(Si visitados no contiene al valor del vecino)
                    visitados.add(valorVecino); // Se añade al LinkedHashSet
                    parent.put(valorVecino, current); // Indica que en el vecino añade otro al valor sacado de la cola
                    queue.add(valorVecino); // Se añade el valor del vecino en la cola queue
                }

            }
        }
        return new PathResult<>(visited, new LinkedHashSet<>());
    }

    private LinkedHashSet<T> buildPath(Map<T, T> parent, T end) {
        LinkedList<T> path = new LinkedList<>();
        T actual = end;

        while(actual != null){
            path.addFirst(actual);
            actual = parent.get(actual);
        }
        return new LinkedHashSet<>(path);
    }
}
