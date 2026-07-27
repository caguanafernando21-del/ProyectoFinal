package structures.node;

import structures.graphs.implementations.Temperatura;

public class Node<T> {
    private T value; // El dato real que guarda el nodo
    private Node<T> left; // Referencia para árboles (hijo izquierdo)
    private Node<T> right; // Referencia para árboles (hijo derecho)
    private Temperatura temperatura;

    // Constructor: Crea el nodo con su valor, sin conexiones iniciales
    public Node(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    // Cuando creas un nodo, puedes asignarle una temperatura
    public void setTemperatura(Temperatura temperatura) {
        this.temperatura = temperatura;
    }

    public Temperatura getTemperatura() {
        return this.temperatura;
    }

    // Método rápido para saber si el nodo tiene datos de temperatura
    public boolean tieneTemperatura() {
        return this.temperatura != null;
    }

    // Getters y Setters estándar
    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node<T> right) {
        this.right = right;
    }

    @Override
    public String toString() {
        return "Node [" + value + "]";
    }

    // hashCode genera un número único basado en el valor.
    // Importante para guardar Nodos en HashMaps o HashSets.
    @Override
    public int hashCode() {
        return value.hashCode();
    }

    // equals compara si dos nodos son Exactamente iguales basándose en su valor interno.


    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj)
            return true; // Es el mismo objeto en memoria
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false; // Tipos diferentes o nulos
        Node<T> other = (Node<T>) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value)) // Compara los valores reales
            return false;
        return true;
    }

}
