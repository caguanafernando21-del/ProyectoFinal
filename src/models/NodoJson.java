package models;

import java.util.List;

public class NodoJson {
    private String id;
    private int x;
    private int y;
    private List<String> vecinos;
    public NodoJson() {
    }
    
    public NodoJson(String id, int x, int y, List<String> vecinos) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vecinos = vecinos;
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
    public List<String> getVecinos() {
        return vecinos;
    }
    
}