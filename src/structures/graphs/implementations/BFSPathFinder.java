package structures.graphs.implementations;
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
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        Queue<T> queue = new LinkedList<>();
        Set<T> visitados = new LinkedHashSet<>();
        Map<Node<T>, Node<T>> parent = new LinkedHashMap<>();
        Set<T> visited = new LinkedHashSet<>();

        queue.add(start);
        visitados.add(start);
        parent.put(new Node<>(start), null);
        while(!queue.isEmpty()) {
            T current = queue.poll(); // Saca el valor que se encuentra primero en la cola
            visited.add(current); //Añade el valor sacado de la cola al LinkedHashSet del visited
            if(current.equals(end)) { // pregunta si el valor sacado de la cola es igual al valor final que buscamos  (end)

                return new PathResult<>(visited, buildPath(parent, end));
            }
            //
            for(Node<T> vecino : graph.getVecinos(current)) { // vecino  pasa a ser los valores que conoce el nodo
                if(!visitados.contains(vecino.getValue())) { //(Si visitados no contiene al valor del vecino)
                    visitados.add(vecino.getValue()); // Se añade al LinkedHashSet
                    parent.put(vecino, new Node<>(current)); // Indica que en el vecino añade otro al valor sacado de la cola
                    queue.add(vecino.getValue()); // Se añade el valor del vecino en la cola queue
                }

            }
        }
        return new PathResult<>(visited, new HashSet<>());
    }

    private Set<T> buildPath(Map<Node<T>, Node<T>> parent, T end) {
        Set<T> path = new LinkedHashSet<>();
        Node<T> nEnd = new Node<>(end);
        // for(int i = 0; i<size; i = i + 1)
        // Una variable de tipo Node<T> llamado at = nEnd (nodo end), 
        

        // entonces después at parent se vuelve el nodo que conoce al NE siendo ND
        for(Node<T> at = nEnd; at != null; at =  parent.get(at)) {
            // mientras at sea diferente de null, en path se añade al valor de at (si es NE, entra solo su valor E)
            path.add(at.getValue());
        }
        return path;
    }
}
