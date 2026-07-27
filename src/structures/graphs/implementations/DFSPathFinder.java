package structures.graphs.implementations;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import listeners.PathListener;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.node.Node;

public class DFSPathFinder<T> implements PathFinder<T>{
    @Override
    public PathResult<T> find(Graph<T> graph, T start, T end, PathListener<T> listener) {
        Set<T> visited = new LinkedHashSet<>();
        List<T> path = new ArrayList<>();

        boolean encontrado = dfs(graph, start, end, visited, path, listener);

        if(!encontrado) {
            //Limpiar
            path.clear();
        }
        
        return new PathResult<>(new LinkedHashSet<>(visited), new LinkedHashSet<>(path));
    }

    private boolean dfs(Graph<T> graph, T current, T end, Set<T> visited,  List<T> path, PathListener<T> listener){

        visited.add(current);

        if(listener != null){
            listener.onNodeVisited(current); //LO VISITA
        }
        path.add(current);

        //CASO BASE
        //Node<T> nC = new Node<T>(current);
        //Node<T> nE = new Node<T>(end); --> NO SE USAN YA
        if(current.equals(end)){ //SI SE encuentra EL FINAL
            return true;

        }        

        for(Node<T> vecino : graph.getVecinos(current)) {
            if( ! visited.contains(vecino.getValue())){
                boolean encon = dfs(graph, vecino.getValue(), end, visited, path, listener);
                if(encon){
                    return true;
                }
            }
        }
        path.remove(path.size()-1);
        return false;
    }
}
