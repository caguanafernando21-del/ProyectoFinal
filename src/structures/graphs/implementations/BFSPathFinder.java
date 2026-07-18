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
        Queue<T> queue = new LinkedList<>();  // Cola para procesar nodos en orden de llegada
        Set<T> visitados = new LinkedHashSet<>(); // Evita ciclos guardando los que ya encolamos
        Map<Node<T>, Node<T>> parent = new LinkedHashMap<>(); // Guarda "quién conoció a quién" para armar el camino final
        Set<T> visited = new LinkedHashSet<>(); // Registra el orden real en que analizamos los nodos

        // Inicializamos con el nodo de inicio
        queue.add(start);
        visitados.add(start);
        parent.put(new Node<>(start), null); // El inicio no tiene padre
        while(!queue.isEmpty()) {
            T current = queue.poll(); // Saca el primer nodo de la cola
            visited.add(current); // Se marca como analizado
            // Si llegamos al destino, se construye el camino hacia atrás y se acaba
            if(current.equals(end)) { // pregunta si el valor sacado de la cola es igual al valor final que buscamos  (end)

                return new PathResult<>(visited, buildPath(parent, end));
            }
            // Se explora todos los vecinos del nodo actual
            for(Node<T> vecino : graph.getVecinos(current)) { // vecino  pasa a ser los valores que conoce el nodo
                if(!visitados.contains(vecino.getValue())) { // Si no lo encolamos antes
                    visitados.add(vecino.getValue()); // Lo marcamos
                    parent.put(vecino, new Node<>(current)); // Se registra que "current" encontró a "vecino"
                    queue.add(vecino.getValue()); // Se añade el valor del vecino en la cola para después analizarlo
                }

            }
        }
        // Si la cola se vacía y no encontramos 'end', retornamos sin camino
        return new PathResult<>(visited, new HashSet<>());
    }

    // Reconstruye el camino desde el destino hasta el inicio usando el mapa 'parent'
    private Set<T> buildPath(Map<Node<T>, Node<T>> parent, T end) {
        Set<T> path = new LinkedHashSet<>(); // Usamos LinkedHashSet para mantener el orden
        
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
