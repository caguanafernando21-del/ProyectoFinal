package models;
public class MapPoint {
    //para un MAPA, los nodos necesitan CONOCER la posicion (dirX, dirY)
    //como posición VISUAL
    private final String id;
    private int x;
    private int y;

    public MapPoint(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    // probablemente --> el HashMap pudo haber GUARDADO un nodo
    //usando el hash anterior (por ejemplo "NA") --> puede provocar conflictos al BUSCAR
    //ENTONCES--> No usar setIDNodo

    /* public void setIdNodo(String idNodo) {
        //this.idNodo = idNodo;
    } */

    public void setCoorX(int x) {
        this.x = x;
    }

    public void setCoorY(int y) {
        this.y = y;
    }

    //hashCode() && equals()
    @Override
    public int hashCode() {
        return id.hashCode(); //si se depende de COORDENADAS
        //y luego se CAMBIAN --> OBJETO CAMBIA HASH tras estar dentro del mapa  --> eliminar el hashCode() habitual
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
        if(getClass() != obj.getClass()){
            return false;
        }
        MapPoint otro = (MapPoint)obj; //<--- CASTEARLO porque SINO se queda con OBJECT
        return id.equals(otro.id); 
        //X y Y no van aqui --> (x,y) indican SOLO donde DIBUJAR --> no PONER
    }

    @Override
    public String toString() {
        return "PuntoMapa [id=" + id + ", x=" + x + ", y=" + y + "]";
    }
    

    

    


}