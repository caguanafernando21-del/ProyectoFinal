package structures.graphs.implementations;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import listeners.PathListener;
import models.MapPoint;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class DFSPathFinder<T> implements PathFinder<T> {
    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end, PathListener<T> listener) {
        Set<T> visited = new LinkedHashSet<>();
        List<T> path = new ArrayList<>();

        boolean encontrado = dfs(graph, start, end, visited, path, listener);

        if (!encontrado) {
            path.clear();
        }
        
        return new PathResult<>(new LinkedHashSet<>(visited), new LinkedHashSet<>(path));
    }

    private boolean dfs(Graph<T> graph, T current, T end, Set<T> visited, List<T> path, PathListener<T> listener) {

        // 1. Añadir al camino actual y marcar como visitado
        visited.add(current);
        path.add(current);

        if (listener != null) {
            listener.onNodeVisited(current); // Notifica a la interfaz visualmente
        }

        // 2. CASO BASE: Si encontramos el destino
        if (current.equals(end)) {
            return true;
        }        

        // 3. Explorar vecinos ordenándolos por cercanía al nodo final (Greedy-DFS)
        Set<Node<T>> vecinosSet = graph.getVecinos(current);
        if (vecinosSet != null) {
        List<Node<T>> vecinosList = new ArrayList<>(vecinosSet);
    
        // Si tus nodos son MapPoint o contienen coordenadas, los ordenamos por distancia al destino
        if (end instanceof MapPoint && !vecinosList.isEmpty() && vecinosList.get(0).getValue() instanceof MapPoint) {
        vecinosList.sort((n1, n2) -> {
            MapPoint p1 = (MapPoint) n1.getValue();
            MapPoint p2 = (MapPoint) n2.getValue();
            MapPoint pDest = (MapPoint) end;
        
            double d1 = Math.hypot(p1.getX() - pDest.getX(), p1.getY() - pDest.getY());
            double d2 = Math.hypot(p2.getX() - pDest.getX(), p2.getY() - pDest.getY());
            return Double.compare(d1, d2); // El más cercano primero
        });
    }

        for (Node<T> vecino : vecinosList) {
            T valorVecino = vecino.getValue();
            if (!visited.contains(valorVecino)) {
                boolean encon = dfs(graph, valorVecino, end, visited, path, listener);
                if (encon) {
                    return true;
                }
            }
        }
    }

        // 4. BACKTRACKING: Si los vecinos no llevaron al destino, 
        // removemos del camino y desmarcamos de visitados para permitir rutas alternas válidas
        path.remove(path.size() - 1);
        visited.remove(current); 
        
        return false;
    }
}