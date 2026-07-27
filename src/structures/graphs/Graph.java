package structures.graphs;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import structures.node.Node;
import structures.graphs.implementations.Temperatura;

public class Graph<T> {
    // Mapa: Clave = Nodo, Valor = Conjunto de Nodos vecinos a los que puede ir
        private Map<Node<T>, Set<Node<T>>> graph;

    public Graph(){
        this.graph = new HashMap<Node<T>, Set<Node<T>>>();
    }

    // Añade un nodo al grafo sin conexiones (aislado)
    public void add(T data){
        Node<T> node = new Node<T>(data);
        graph.putIfAbsent(node, new HashSet<Node<T>>()); // putIfAbsent evita sobrescribir si ya existe
    }

    // Crea una conexión Bidireccional (Grafo no Dirigido)
    public void addConection(T v1, T v2){
        Node<T> nv1 = new Node<T>(v1);
        Node<T> nv2 = new Node<T>(v2);

        add(v1); // Asegura que v1 exista
        add(v2); // Asegura que v2 exista

        graph.get(nv1).add(nv2); // v1 conoce a v2 
        graph.get(nv2).add(nv1); // v2 conoce a v1
    }

    // Crea una conexión UNIDIRECCIONAL (Grafo Dirigido: v1 apunta a v2, pero no al revés)
    public void addConectionUni(T v1, T v2){
        Node<T> nv1 = new Node<T>(v1);
        Node<T> nv2 = new Node<T>(v2);

        this.graph.putIfAbsent(nv1, new HashSet<>());
        this.graph.putIfAbsent(nv2, new HashSet<>());
        this.graph.get(nv1).add(nv2); // Solo v1 conoce a v2
    }
    
    // Imprime el grafo mostrando cada nodo y quiénes son sus vecinos
    public void printGraph(){
        for (Map.Entry<Node<T>, Set<Node<T>>> entry : graph.entrySet()){
            System.out.println(entry.getKey() + " -> ");
            for (Node<T> coneccion : entry.getValue()){
                System.out.println(coneccion);
            }
            System.out.println();
        } 
    }
    
    // Devuelve los vecinos de un nodo específico (o un conjunto vacío si no tiene/no existe)
    public Set<Node<T>> getVecinos(T current) {
        return graph.getOrDefault(new Node<T>(current), new HashSet<Node<T>>());
    }

    // Método auxiliar: Busca y retorna el objeto Node<T> en el grafo usando su valor (T)
    public Node<T> getNode(T data) {
        for (Node<T> node : graph.keySet()) {
            if (node.getValue().equals(data)) {
                return node;
            }
        }
        return null;
    }

    // Asigna o actualiza la temperatura de un punto/nodo en el mapa
    public void setTemperatura(T data, Temperatura temperatura) {
        Node<T> node = getNode(data);
        if (node != null) {
            node.setTemperatura(temperatura);
        }
    }

    // Obtiene la temperatura del punto/nodo especificado
    public Temperatura getTemperatura(T data) {
        Node<T> node = getNode(data);
        if (node != null) {
            return node.getTemperatura();
        }
        return null;
    }
}
