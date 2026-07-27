package structures.graphs;

import listeners.PathListener;

public interface PathFinder<T> {
    PathResult<T> find(Graph<T> graph, T start, T end, PathListener<T> listener);
}
