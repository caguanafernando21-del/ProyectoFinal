package view;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import models.MapPoint;
import java.util.List;

public class MapPanel extends JPanel { //ahora ES UN PANEL porque HEREDA CARACTERISTICAS DEL PANEL
    private Image mapa;
    private List<MapPoint> nodosMapa; // [("NA", 100, 300), ("NB",400, 200)]

    public MapPanel() {
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
            graficos.drawImage(mapa, 0, 0, getWidth(), getHeight(), this); //esto hace que la IMAGEN CREZCA con el PANEL
        graficos.setFont(new Font("Arial", Font.BOLD, 24));
        graficos.setColor(Color.BLACK);
        graficos.drawString("MAPA", 20, 30);

        if(nodosMapa != null){
            for(MapPoint puntosMapa : nodosMapa){
                //primero x o y 
                //coordenadas reales: (x,y) ---> entonces ---> importa el orden?
                // ---> ---> SI
                graficos.setColor(Color.RED);
                graficos.fillOval(puntosMapa.getX()-8, puntosMapa.getY()-8, 16, 16);
                graficos.setColor(Color.BLACK);
                graficos.drawString(puntosMapa.getId(), puntosMapa.getX()+10, puntosMapa.getY() -10);
            }
        }
    }
}


