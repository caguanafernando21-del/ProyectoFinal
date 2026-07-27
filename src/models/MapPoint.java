package models;

import java.awt.Color;

public class MapPoint {

    // Nombre del nodo 
    private String id;
    // Posición del nodo en el mapa
    private int x;
    private int y;

    // Color del nodo
    private Color color;

    // Constructor
    public MapPoint(String id, int x, int y) {

        this.id = id;
        this.x = x;
        this.y = y;

        color = Color.BLUE;
    }

    // Devuelve el nombre del nodo
    public String getId() {
        return id;
    }

    // Devuelve la posición X
    public int getX() {
        return x;
    }

    // Devuelve la posición Y
    public int getY() {
        return y;
    }

    // Devuelve el color
    public Color getColor() {
        return color;
    }

    // Cambia el color
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "PuntoMapa [id=" + id + ", x=" + x + ", y=" + y + ", color=" + color + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MapPoint otro = (MapPoint) obj;
        return id.equals(otro.id);
    }
    
    

}