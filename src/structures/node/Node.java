package structures.node;
import structures.graphs.implementations.Temperatura;
import java.util.LinkedHashSet;
import java.util.Set;

public class Node<T> {
    //nodos AHORA tienen que SERVIR PARA RUTAS
    //ya no PARA ÁRBOLES BINARIOS por AHORA
    private T value;
    private Set<Node<T>> vecinos;
    private Temperatura temperatura;
    

    // Constructor ES LO QUE CREA EL NODO
    // NECESTIO SOLO EL VALOR, LAS REFERENCIAS SE INICIALIZAN EN NULL
    public Node(T value) {
        this.value = value;
        this.vecinos = new LinkedHashSet<>(); //vecinos en NODOS NO PUEDEN REPETIRSE: SET
        this.temperatura = null; // 3. Inicializamos en null

    }

    // --- MÉTODOS DE TEMPERATURA ---
    public Temperatura getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Temperatura temperatura) {
        this.temperatura = temperatura;
    }

    public boolean tieneTemperatura() {
        return this.temperatura != null;
    }

    public T getValue() {
        return value;
    }


    public void setValue(T value) {
        this.value = value;
    }
   
    public Set<Node<T>> getVecinos() {
        return vecinos;
    }

    @Override
    public String toString() {
        return "Nodo [" + value + "]"; //representacion del NODO
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof Node<?> == false){ //evitar escribir por ejemplo UN STRING y que sea CONSIDERADO COMO NODO
            return false;
        }
        Node<?> otroNodo = (Node<?>) obj; //CONVIERTE a NODO para que no se quede en OBJ
        return value.equals(otroNodo.value); 
    }

    @Override
    public int hashCode() {
        return value.hashCode(); //se debe producir un MISMO HASHCODE 
        //en el caso de que DOS OBJETOS SEAN IGUALES
    }
    public void setVecinos(Set<Node<T>> vecinos) {
        this.vecinos = vecinos;
    }

     public void añadirVecinos(Node<T> nodoAñadir){
        vecinos.add(nodoAñadir);
    }
}