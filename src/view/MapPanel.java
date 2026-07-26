package view;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import listeners.PathListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import models.MapPoint;
import java.util.List;

public class MapPanel extends JPanel implements PathListener<MapPoint> { //ahora ES UN PANEL porque HEREDA CARACTERISTICAS DEL PANEL
    private Image mapa;
    private List<MapPoint> nodosMapa; // [("NA", 100, 300), ("NB",400, 200)]

    public MapPanel() {
        System.out.println("CREANDO MAP PANEL");
        //en el MapPanel todo se va a DIBUJAR en un mismo PANEL, si se puede hacerlo en un JLabel
        //pero el problema es QUE ES POSIBLE que los nodos no se puedan ver porque el JLabel los tapa
        //se podría DIBUJAR todo en un mismo panel
        setBackground(new Color(240, 240, 240)); //color de FONDO del PANEL pricipal
        URL ruta = getClass().getResource("/mapas.png");        
        if(ruta != null){
            mapa = new ImageIcon(ruta).getImage(); //se dibuja sobre EL
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró mapas.png", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setNodos(List<MapPoint> nodosMapa){
        this.nodosMapa = nodosMapa; // asignar THIS a que apunte a NODOSMAPA
        repaint(); //metodo de hacer
    }
    @Override
    protected void paintComponent(Graphics graficos){
        super.paintComponent(graficos);
        if(mapa != null)
            graficos.drawImage(mapa, 0, 0, 670, 520, this); //esto hace que la IMAGEN CREZCA con el PANEL
        graficos.setFont(new Font("Arial", Font.BOLD, 24));
        graficos.setColor(Color.BLACK);
        graficos.drawString("MAPA", 20, 30);
        System.out.println("Nodos: " + nodosMapa);
        
        if(nodosMapa != null){
            for(MapPoint puntosMapa : nodosMapa){
                //primero x o y 
                //coordenadas reales: (x,y) ---> entonces ---> importa el orden?
                // ---> ---> SI
                int x = puntosMapa.getX();
                int y = puntosMapa.getY();
                graficos.setColor(new Color(47, 201, 235));
                graficos.drawOval(x-7, y-7, 14, 14);

                // centro
                graficos.setColor(new Color(28, 134, 255));
                graficos.fillOval(x-3, y-3, 6, 6);

                // nombre
                graficos.setColor(Color.WHITE);
                graficos.drawString(puntosMapa.getId(), x+10, y-10);
            }
        }

    }
    @Override
    public void onNodeVisited(MapPoint node){
        System.out.println("Visited: " + node);
        repaint();
    }
}


