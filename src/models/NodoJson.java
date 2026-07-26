package models;

import java.util.List;

public class NodoJson {
    private String id;
    private int x;
    private int y;
    private List<String> vecinos;
    public NodoJson() {
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