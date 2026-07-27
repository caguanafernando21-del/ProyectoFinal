// Ubicacion sugerida: src/view/MapPanel.java
package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

import listeners.PathListener;
import models.MapPoint;
import models.VisualizationMode.TipoVisualizacion;
import structures.graphs.Graph; 
import structures.node.Node;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class MapPanel extends JPanel implements PathListener<MapPoint> {
    
    private Image imagenMapa;   
    private Graph<MapPoint> grafo; 
    private TipoVisualizacion modoVisualizacion = TipoVisualizacion.EXPLORATION;
    private List<MapPoint> visitadosADibujar = new ArrayList<>();
    private List<MapPoint> pathADibujar = new ArrayList<>();
    private Queue<MapPoint> colaDeVisitados = new LinkedList<>();
    private Queue<MapPoint> colaExploracion = new LinkedList<>();
    private Timer timerExploration;
    // VARIABLES DE CONTROL DE ESTADO
    private final int RADIO_NODO = 8;                     // Tamano del circulo que representa al nodo

    public MapPanel(Graph<MapPoint> grafo) {
        System.out.println("CREANDO MAP PANEL");
        //en el MapPanel todo se va a DIBUJAR en un mismo PANEL, si se puede hacerlo en un JLabel
        //pero el problema es QUE ES POSIBLE que los nodos no se puedan ver porque el JLabel los tapa
        //se podría DIBUJAR todo en un mismo panel
        this.grafo = grafo;
        setBackground(Color.DARK_GRAY);
        // Inicializamos las listas en blanco
        // Cargar la imagen
        java.net.URL ruta = getClass().getResource("/mapas.png");
        if (ruta != null) {
            imagenMapa = new ImageIcon(ruta).getImage();
        } else {
            JOptionPane.showMessageDialog(this, "No se encontró mapas.png");
        }
    }
    public void setModoVisualizacion(TipoVisualizacion modoV){
        this.modoVisualizacion = modoV;
        repaint();
    }
    public void limpiarVisualizacion(){
        if(timerExploration != null){
            timerExploration.stop();
        }
        visitadosADibujar.clear();
        pathADibujar.clear();
        colaDeVisitados.clear();
        repaint();
    }
    

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        Graphics2D graficosEn2D = (Graphics2D) graficos;
        
        // Fondo del mapa
        if (imagenMapa != null){
            graficosEn2D.drawImage(imagenMapa, 0, 0, 670, 520, this);
        }

        // DIBUJO DE CONEXIONES
        graficosEn2D.setColor(new Color(41, 128, 185)); 
        graficosEn2D.setStroke(new BasicStroke(3)); 
        Set<String>  conexionesDibujadas = new HashSet<>();
        for(Node<MapPoint> nodo : grafo.getNodes()){
            MapPoint puntoOrigen = nodo.getValue();
            for(Node<MapPoint> vecino : grafo.getVecinos(puntoOrigen)){
                MapPoint puntoDestino = vecino.getValue();
                String conexion = puntoOrigen.getId() + "-" + puntoDestino.getId();
                String inversa = puntoDestino.getId() + "-" + puntoOrigen.getId();
                if(!conexionesDibujadas.contains(inversa)){
                    graficosEn2D.drawLine(puntoOrigen.getX(), puntoOrigen.getY(), puntoDestino.getX(), puntoDestino.getY());
                    conexionesDibujadas.add(conexion);
                }
            }
        }
        for(Node<MapPoint> nodo : grafo.getNodes()){
            MapPoint punto = nodo.getValue();
            int x = punto.getX();
            int y = punto.getY();
            graficosEn2D.setColor(new Color(47, 201, 235)); //las FIGURAS se MANTIENEN en la INTERFAZ
            graficosEn2D.fillOval(x-RADIO_NODO, y-RADIO_NODO, RADIO_NODO*2, RADIO_NODO*2);
            graficosEn2D.setColor(new Color(28, 134, 255));
            graficosEn2D.fillOval(x-3, y-3, 6, 6);
            graficosEn2D.setColor(Color.WHITE);
            graficosEn2D.setFont(new Font("Arial", Font.BOLD, 12));
            graficosEn2D.drawString(punto.getId(), x+10, y-10);
        }
        for(MapPoint punto : visitadosADibujar){
            graficosEn2D.setColor(Color.ORANGE);
            graficosEn2D.fillOval(punto.getX()-6, punto.getY()-6, 12, 12);
        }
        //DIBUJO DE LA RUTA FINAL
        if(pathADibujar.size() >1){
            graficosEn2D.setColor(Color.GREEN);
            graficosEn2D.setStroke(new BasicStroke(5));
            for(int i = 0; i < pathADibujar.size()-1; i++){
                MapPoint puntoActual = pathADibujar.get(i);
                MapPoint puntoSiguiente = pathADibujar.get(i+1);
                graficosEn2D.drawLine(puntoActual.getX(), puntoActual.getY(), puntoSiguiente.getX(), puntoSiguiente.getY());
            }
        }
        //punto que MARCAN la RUTA FINAL
        for(MapPoint punto : pathADibujar){
            graficosEn2D.setColor(Color.GREEN);
            graficosEn2D.fillOval(punto.getX()-6, punto.getY()-6, 12, 12);
        }
    }
    @Override
    public void onNodeVisited(MapPoint nodoPunto){
        System.out.println("MAP PANEL RECIBE:" + nodoPunto);
        System.out.println("VISITANDO:" + nodoPunto.getId());
        colaExploracion.add(nodoPunto);
        if(timerExploration == null){
            timerExploration = new Timer(300, e -> {
                if(!colaExploracion.isEmpty()){
                    MapPoint puntoSiguiente = colaExploracion.poll();
                    visitadosADibujar.add(puntoSiguiente);
                    repaint();
                } else {
                    timerExploration.stop();
                    timerExploration = null;
                }
            });
            timerExploration.start();
        }
    }
    
    //METODO MOSTRAR RUTA
    public void mostrarRuta(List<MapPoint> rutaExploracion){
        pathADibujar.clear();
        Timer timer = new Timer(300, null);
        final int[] indice = {0};
        timer.addActionListener(e -> {
            if(indice[0] < rutaExploracion.size()){
                pathADibujar.add(rutaExploracion.get(indice[0]));
                indice[0]++;
                repaint();
            } else {
                timer.stop();
            }
        });
        timer.start();
    }
    //METODO EXPLORACION TERMINADA
    public boolean exploracionTerminada(){
        return colaDeVisitados.isEmpty() && (timerExploration == null || !timerExploration.isRunning());
    }

    public void setGrafo(Graph<MapPoint> grafo){
        this.grafo = grafo;
        repaint();
    }
}