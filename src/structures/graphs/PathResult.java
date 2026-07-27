package structures.graphs;
import java.util.LinkedHashSet;
import java.util.Set;

public class PathResult<T> {
    private final LinkedHashSet<T> visitados;
    private final LinkedHashSet<T> path;

    public PathResult(LinkedHashSet<T> visitados, LinkedHashSet<T> path) {
        this.visitados = visitados;
        this.path = path;
    }
    
    public Set<T> getVisitados() {
        return visitados;
    }


    public Set<T> getPath() {
        return path;
    }

    // Imprime de forma legible si encontró ruta o no.
    @Override
    public String toString() {
        return "PathResult"
            + "\nvisitados=" + visitados +  "\n" 
            // Usa un operador ternario: Si el path NO está vacío lo imprime, si no, da un mensaje de error.
            + (!path.isEmpty() ? "path=" + path + "]" : "No se encontro camino entre los nodos");
    }
}
