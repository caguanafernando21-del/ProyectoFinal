package structures.graphs;

public interface PathFinder<T> {
    // Recibe el grafo, el punto de inicio y el destino, devolviendo un objeto con los resultados.
    PathResult<T> find(Graph<T> graph, T start, T end);
}
