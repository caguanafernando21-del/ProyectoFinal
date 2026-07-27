package models;
import java.util.List;

public class MapaJson {
    private List<NodoJson> nodos;

    public MapaJson(){

    }

    public List<NodoJson> getNodos(){
        return nodos;
    }

    public void setNodos(List<NodoJson> nodos) {
        this.nodos = nodos;
    }
    
}
