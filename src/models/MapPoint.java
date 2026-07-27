package models;

import java.awt.Color;

public class MapPoint {

    // Nombre del nodo 
    private String nombre;
    // Posición del nodo en el mapa
    private int x;
    private int y;

    // Color del nodo
    private Color color;

    // Constructor
    public MapPoint(String nombre, int x, int y) {

        this.nombre = nombre;
        this.x = x;
        this.y = y;

        color = Color.BLUE;
    }

    // Devuelve el nombre del nodo
    public String getNombre() {
        return nombre;
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

}