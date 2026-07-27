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

public class DFSPathFinder<T> implements PathFinder<T> {

    @Override
    public PathResult<T> find(
            Graph<T> graph,
            T start,
            T end,
            PathListener<T> listener) {


        Set<T> visitados = new LinkedHashSet<>();
        List<T> ruta = new ArrayList<>();

        dfs(graph, start, end, visitados, ruta, listener);

        return new PathResult<>(
                new LinkedHashSet<>(visitados),
                new LinkedHashSet<>(ruta)
        );
    }


    private boolean dfs(
            Graph<T> graph,
            T actual,
            T destino,
            Set<T> visitados,
            List<T> ruta,
            PathListener<T> listener) {


        visitados.add(actual);

        if(listener != null){
            listener.onNodeVisited(actual);
        }


        ruta.add(actual);


        if(actual.equals(destino)){
            return true;
        }


        for(Node<T> vecino : graph.getVecinos(actual)){

            T siguiente = vecino.getValue();


            if(!visitados.contains(siguiente)){

                boolean encontrado = dfs(
                        graph,
                        siguiente,
                        destino,
                        visitados,
                        ruta,
                        listener
                );


                if(encontrado){
                    return true;
                }
            }
        }


        // backtracking
        ruta.remove(ruta.size() - 1);

        return false;
    }
}
