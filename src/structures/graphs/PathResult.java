package structures.graphs;
import java.util.Set;

public class PathResult<T> {
    private final Set<T> visitados; // Historial de todos los nodos que se revisaron (útil para ver eficiencia)
    private final Set<T> path; // El camino final y limpio desde 'start' hasta 'end'

    public PathResult(Set<T> visitados,Set<T> path) {
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
