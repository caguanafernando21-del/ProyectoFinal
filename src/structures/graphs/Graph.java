package structures.graphs;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import structures.node.Node;
import structures.graphs.implementations.Temperatura;

public class Graph<T> {
    // Mapa: Clave = Nodo, Valor = Conjunto de Nodos vecinos a los que puede ir
    private Map<Node<T>, Set<Node<T>>> graph;


    public Graph(){
        this.graph = new LinkedHashMap<Node<T>, Set<Node<T>>>();
    }

    // Añade un nodo al grafo sin conexiones (aislado)
    public void add(T data){
        Node<T> node = new Node<T>(data);
        // Usamos LinkedHashSet para garantizar que los algoritmos respeten el orden de conexión
        graph.putIfAbsent(node, new LinkedHashSet<Node<T>>());
    }

    // Crea una conexión Bidireccional (Grafo no Dirigido)
    public void addConection(T v1, T v2){
        add(v1); // Asegura que v1 exista
        add(v2); // Asegura que v2 exista

        // SOLUCIÓN: Buscar la instancia REAL del nodo en el grafo, no crear uno nuevo
        Node<T> n1 = getNode(v1);
        Node<T> n2 = getNode(v2);

        if (n1 != null && n2 != null) {
            graph.get(n1).add(n2); // v1 conoce a v2 
            graph.get(n2).add(n1); // v2 conoce a v1
        }
    }

    // Crea una conexión UNIDIRECCIONAL (Grafo Dirigido: v1 apunta a v2, pero no al revés)
    public void addConectionUni(T v1, T v2){
        add(v1); // Asegura que v1 exista con su LinkedHashSet
        add(v2); // Asegura que v2 exista

        // SOLUCIÓN: Buscar la instancia REAL del nodo en el grafo
        Node<T> n1 = getNode(v1);
        Node<T> n2 = getNode(v2);

        if (n1 != null && n2 != null) {
            graph.get(n1).add(n2); // Solo v1 conoce a v2
        }
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
        Node<T> node = getNode(current);
        if (node != null) {
            return graph.get(node);
        }
        return new LinkedHashSet<>();
    }

    public Set<Node<T>> getNodes() {
        return graph.keySet();
    }
    
    public Map<Node<T>, Set<Node<T>>> getGraph(){
        return graph;
    }

    public void removeConnection(T value1, T value2){
        // SOLUCIÓN: Buscar las instancias reales antes de intentar remover
        Node<T> n1 = getNode(value1);
        Node<T> n2 = getNode(value2);
        
        if(n1 != null && n2 != null){
            graph.get(n1).remove(n2);
            graph.get(n2).remove(n1);
        }
    }
    
    public void remove(T data){
        // SOLUCIÓN: Buscar la instancia real para poder limpiar las referencias correctamente
        Node<T> nodoEliminar = getNode(data);
        if (nodoEliminar != null) {
            graph.remove(nodoEliminar);
            for(Set<Node<T>> vecinos : graph.values()){
                vecinos.remove(nodoEliminar);
            }
        }
    }
    
    public boolean contains(T data){
        return getNode(data) != null;
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

    // Obtiene la temperatura del punto/nodo especificado.
    public Temperatura getTemperatura(T data) {
        Node<T> node = getNode(data);
        if (node != null) {
            return node.getTemperatura();
        }
        return null;
    }
}