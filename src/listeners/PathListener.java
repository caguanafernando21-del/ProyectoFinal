package listeners;

public interface PathListener<T> {
    void onNodeVisited(T node);
}
