package structures.node;

import java.util.ArrayList;
import java.util.List;

public class Node<T> {
    //nodos AHORA tienen que SERVIR PARA RUTAS
    //ya no PARA ÁRBOLES BINARIOS por AHORA
    private T value;
    private double x;
    private double y;
    private List<Node<T>> vecinos;

    // Constructor ES LO QUE CREA EL NODO
    // NECESTIO SOLO EL VALOR, LAS REFERENCIAS SE INICIALIZAN EN NULL
    public Node(T value, double x, double y) {
        this.value = value;
        this.x = x;
        this.y = y;
        this.vecinos = new ArrayList<>(); //vecinos PUEDEN REPETIRSE: ARRAYLIST
    }

    public T getValue() {
        return value;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public List<Node<T>> getVecinos() {
        return vecinos;
    }
    public void añadirVecinos(Node<T> nodoAñadir){
        vecinos.add(nodoAñadir);
    }
    @Override
    public String toString() {
        return "Nodo [" + value + "]"; //representacion del NODO
    }

}
