package view;

import javax.swing.JPanel;
import java.awt.Graphics;
import models.MapPoint;
import java.util.List;

public class MapPanel extends JPanel { //ahora ES UN PANEL porque HEREDA CARACTERISTICAS DEL PANEL
    private List<MapPoint> nodosMapa; // [("NA", 100, 300), ("NB",400, 200)]
    public void setNodos(List<MapPoint> nodosMapa){
        this.nodosMapa = nodosMapa; // asignar THIS a que apunte a NODOSMAPA
        repaint(); //metodo de hacer
    }
    @Override
    protected void paintComponent(Graphics graficos){
        super.paintComponent(graficos);
        if(nodosMapa != null){
            for(MapPoint puntosMapa : nodosMapa){ //cada UNO SE PINTA
                graficos.fillOval(puntosMapa.getX(), puntosMapa.getY(), 20, 20);
                graficos.drawString(puntosMapa.getId(), puntosMapa.getX(), puntosMapa.getY());
                //primero x o y 
                //coordenadas reales: (x,y) ---> entonces ---> importa el orden?
                // ---> ---> SI
            }
        }
    }
}
