package structures.graphs.implementations;

import java.util.LinkedHashSet;
import java.util.Set;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class DFSPathFinder<T> implements PathFinder<T>{
    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end) {
        Set<T> visited = new LinkedHashSet<>(); // Nodos explorados
        Set<T> path = new LinkedHashSet<>(); // El camino actual que estamos probando

        boolean encontrado = dfs(graph, start, end, visited, path);

        if(!encontrado) {
            
            path.clear(); // Si no se conectan, el camino queda vacío o lo limpiamos
        }
        
        return new PathResult<>(new LinkedHashSet<>(visited), new LinkedHashSet<>(path));
    }

    // Método recursivo que hace el trabajo pesado
    private boolean dfs(Graph<T> graph, T current, T end, Set<T> visited,  Set<T> path){

        visited.add(current); // Marcamos el nodo actual como visitado
        path.add(current); // Lo añadimos al posible camino

        
        Node<T> nC = new Node<T>(current);
        Node<T> nE = new Node<T>(end);

        //CASO BASE: Si el actual es la meta, terminamos con éxito
        if(nC.equals(nE)){
            return true;

        }

        // Si no es la meta, intentamos avanzar por cada vecino
        for(Node<T> vecino : graph.getVecinos(current)) {
            if( ! visited.contains(vecino.getValue())){
                // Llamada recursiva (entra al vecino)
                boolean encon = dfs(graph, vecino.getValue(), end, visited, path);
                if(encon){
                    return true; // Si por esta rama encontramos la meta, detenemos la búsqueda
                }
            }
        }
        // BACKTRACKING: Si exploramos todos los vecinos y ninguno llevó a la meta, 
        // sacamos este nodo del camino porque no sirve para llegar al destino.
        path.remove(current);
        return false;
    }
}
