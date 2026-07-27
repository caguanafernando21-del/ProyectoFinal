// Ubicacion sugerida: src/view/MapPanel.java
package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import models.VisualizationMode.ModoEdicion; 

public class MapPanel extends JPanel {
    
    // VARIABLES DE DIBUJO Y ALMACENAMIENTO
    private Image imagenMapa;
    private List<Point> nodos;            // Lista que guarda todos los nodos
    private List<Point[]> conexiones;     // Lista que guarda pares de puntos 
    
    // VARIABLES DE CONTROL DE ESTADO
    private ModoEdicion modoActual = ModoEdicion.NINGUNO; // Controla que accion hara el raton al hacer clic
    private final int RADIO_NODO = 8;                     // Tamano del circulo que representa al nodo
    private Point nodoSeleccionadoTemp = null;            // Guarda el primer nodo clickeado al conectar/desconectar

    public MapPanel() {
        // Inicializamos las listas en blanco
        nodos = new ArrayList<>();
        conexiones = new ArrayList<>();

        //Cargar la imagen
        java.net.URL ruta = getClass().getResource("/mapas.png");
        if (ruta != null) {
            imagenMapa = new ImageIcon(ruta).getImage();
        } else {
            System.err.println("Error: No se encontro mapas.png en los recursos.");
        }

        // Cargar los nodos y conexiones ya establecidas
        cargarDatosDelGrafo();

        // Eventos al hacer click
        // Escucha los clics que el usuario hace sobre este panel
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                manejarClic(e.getPoint()); // Pasa las coordenadas X,Y del clic a nuestro metodo
            }
        });
    }

    
    private void cargarDatosDelGrafo() {
        Map<String, Point> mapaDiccionario = new HashMap<>();

        
        mapaDiccionario.put("N1", new Point(204, 19));
        mapaDiccionario.put("N2", new Point(128, 195));
        mapaDiccionario.put("N4", new Point(55, 306));
        mapaDiccionario.put("N5", new Point(21, 286));
        mapaDiccionario.put("N6", new Point(4, 284));
        mapaDiccionario.put("N7", new Point(71, 293));
        mapaDiccionario.put("N8", new Point(94, 307));
        mapaDiccionario.put("N9", new Point(109, 318));
        mapaDiccionario.put("N10", new Point(151, 349));
        mapaDiccionario.put("N11", new Point(139, 367));
        mapaDiccionario.put("N12", new Point(121, 384));
        mapaDiccionario.put("N13", new Point(167, 359));
        mapaDiccionario.put("N14", new Point(244, 384));
        mapaDiccionario.put("N15", new Point(220, 400));
        mapaDiccionario.put("N16", new Point(201, 420));
        mapaDiccionario.put("N17", new Point(148, 486));
        mapaDiccionario.put("N18", new Point(173, 508));
        mapaDiccionario.put("N19", new Point(212, 459));
        mapaDiccionario.put("N20", new Point(236, 475));
        mapaDiccionario.put("N21", new Point(293, 423));
        mapaDiccionario.put("N21-2", new Point(280, 411));
        mapaDiccionario.put("N22", new Point(246, 483));
        mapaDiccionario.put("N22-2", new Point(271, 500));
        mapaDiccionario.put("N23", new Point(302, 435));
        mapaDiccionario.put("N24", new Point(334, 462));
        mapaDiccionario.put("N25", new Point(340, 473));
        mapaDiccionario.put("N25-2", new Point(359, 485));
        mapaDiccionario.put("N26", new Point(378, 497));
        mapaDiccionario.put("N27", new Point(472, 482));
        mapaDiccionario.put("N28", new Point(484, 435));
        mapaDiccionario.put("N29", new Point(514, 383));
        mapaDiccionario.put("N30", new Point(484, 357));
        mapaDiccionario.put("N30-2", new Point(462, 349));
        mapaDiccionario.put("N31", new Point(404, 298));
        mapaDiccionario.put("N32", new Point(544, 285));
        mapaDiccionario.put("N33", new Point(557, 274));
        mapaDiccionario.put("N34", new Point(533, 274));
        mapaDiccionario.put("N35", new Point(470, 218));
        mapaDiccionario.put("N36", new Point(496, 184));
        mapaDiccionario.put("N37", new Point(485, 170));
        mapaDiccionario.put("N38", new Point(424, 126));
        mapaDiccionario.put("N39", new Point(355, 218));
        mapaDiccionario.put("N40", new Point(342, 233));
        mapaDiccionario.put("N41", new Point(287, 199));
        mapaDiccionario.put("N42", new Point(268, 186));

        // Transferimos todos los puntos creados a la lista
        nodos.addAll(mapaDiccionario.values());

        // Conexiones
        String[][] aristas = {
            {"N1", "N2"},
            {"N2", "N7"},
            {"N4", "N5"}, 
            {"N4", "N7"},
            {"N5", "N6"},
            {"N7", "N8"},
            {"N8", "N9"},
            {"N9", "N10"},
            {"N10", "N11"}, 
            {"N10", "N13"},
            {"N11", "N12"},
            {"N13", "N15"},
            {"N14", "N15"}, 
            {"N14", "N21"},
             {"N14", "N21-2"},
            {"N15", "N16"},
            {"N16", "N17"},
            {"N17", "N18"},
            {"N18", "N19"},
            {"N19", "N20"},
            {"N20", "N22"},
            {"N21", "N21-2"}, 
            {"N21", "N23"},
             {"N21", "N31"},
            {"N22", "N22-2"},
            {"N23", "N24"},
            {"N24", "N25"},
            {"N25", "N25-2"},
            {"N25-2", "N26"},
            {"N26", "N27"},
            {"N27", "N28"},
            {"N28", "N29"},
            {"N29", "N30"},
            {"N30", "N30-2"}, 
            {"N30", "N32"},
            {"N30-2", "N31"},
            {"N31", "N35"},
            {"N32", "N33"}, 
            {"N32", "N34"},
            {"N34", "N35"},
            {"N35", "N36"},
            {"N36", "N37"},
            {"N37", "N38"},
            {"N38", "N39"},
            {"N39", "N40"},
            {"N40", "N41"},
            {"N41", "N42"}
        };

        // Recorremos los pares y si ambos nodos existen se conectan
        for (String[] par : aristas) {
            Point p1 = mapaDiccionario.get(par[0]);
            Point p2 = mapaDiccionario.get(par[1]);
            
            if (p1 != null && p2 != null) {
                conexiones.add(new Point[]{p1, p2});
            }
        }
    }

  
    public void setModoActual(ModoEdicion modo) {
        this.modoActual = modo;
        resetearSeleccionTemporal(); 
    }

    
     // Limpia la variable que guarda el click al conectar o desconectar.
     
    public void resetearSeleccionTemporal() {
        nodoSeleccionadoTemp = null;
        repaint(); // Vuelve a dibujar el panel para quitar colores temporales
    }

    
    private void manejarClic(Point clic) {
        switch (modoActual) {
            case AGREGAR_NODO:
                //Agregar un punto donde el usuario hizo click
                nodos.add(clic);
                break;

            case ELIMINAR_NODO:
                // Busca si el usuario hizo clic encima o muy cerca de un nodo existente
                Point nodoEliminar = encontrarNodoCercano(clic);
                if (nodoEliminar != null) {
                    nodos.remove(nodoEliminar);
                    // Borra las conexiones que involucren a ese nodo
                    conexiones.removeIf(conexion -> conexion[0].equals(nodoEliminar) || conexion[1].equals(nodoEliminar));
                }
                break;

            case CONECTAR_NODOS:
                Point nodoParaConectar = encontrarNodoCercano(clic);
                if (nodoParaConectar != null) {
                    if (nodoSeleccionadoTemp == null) {
                        // Si no hay nodo guardado, este clic es el origen
                        nodoSeleccionadoTemp = nodoParaConectar;
                    } else {
                        if (!nodoSeleccionadoTemp.equals(nodoParaConectar)) {
                            conexiones.add(new Point[]{nodoSeleccionadoTemp, nodoParaConectar});
                        }
                        nodoSeleccionadoTemp = null;
                    }
                }
                break;

            case ELIMINAR_CONEXION:
                Point nodoParaDesconectar = encontrarNodoCercano(clic);
                if (nodoParaDesconectar != null) {
                    if (nodoSeleccionadoTemp == null) {
                        nodoSeleccionadoTemp = nodoParaDesconectar;
                    } else {
                        conexiones.removeIf(conexion -> 
                            (conexion[0].equals(nodoSeleccionadoTemp) && conexion[1].equals(nodoParaDesconectar)) ||
                            (conexion[1].equals(nodoSeleccionadoTemp) && conexion[0].equals(nodoParaDesconectar))
                        );
                        nodoSeleccionadoTemp = null;
                    }
                }
                break;
                
            default:
                break;
        }
        
        repaint(); // Actualiza los graficos en pantalla
    }

    /**
     * Dado un clic del raton recorre la lista para ver si el clic
     * ocurrio cerca de alguno de los nodos dentro de su radio.
     */
    private Point encontrarNodoCercano(Point clic) {
        for (Point nodo : nodos) {
            if (clic.distance(nodo) <= RADIO_NODO * 2) {
                return nodo;
            }
        }
        return null;
    }

   
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo del mapa
        if (imagenMapa != null) {
            g2d.drawImage(imagenMapa, 0, 0, getWidth(), getHeight(), this);
        }

        // Lineas de conexion
        g2d.setColor(new Color(41, 128, 185)); 
        g2d.setStroke(new BasicStroke(3));     // Grosor de la linea
        for (Point[] conexion : conexiones) {
            g2d.drawLine(conexion[0].x, conexion[0].y, conexion[1].x, conexion[1].y);
        }

        // CAPA 3: PUNTOS (NODOS)
        for (Point nodo : nodos) {
            if (nodo.equals(nodoSeleccionadoTemp)) {
                g2d.setColor(new Color(241, 196, 15)); //Se pone amarillo al hacer click
            } else {
                g2d.setColor(new Color(231, 76, 60)); 
            }
            
            // Dibuja el circulo relleno centrado en las coordenadas
            g2d.fillOval(nodo.x - RADIO_NODO, nodo.y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);
            
            // Dibuja un borde sutil alrededor de cada nodo
            g2d.setColor(new Color(44, 62, 80));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(nodo.x - RADIO_NODO, nodo.y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);
        }
    }
}