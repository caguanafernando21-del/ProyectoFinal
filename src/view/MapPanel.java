package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import listeners.PathListener;
import models.MapPoint;
import models.VisualizationMode.ModoEdicion;
import models.VisualizationMode.TipoVisualizacion;
import structures.graphs.Graph; 
import structures.node.Node;

public class MapPanel extends JPanel implements PathListener<MapPoint> {

    private Image imagenMapa;   
    private Graph<MapPoint> grafo; 
    private TipoVisualizacion modoVisualizacion = TipoVisualizacion.EXPLORATION;
    private ModoEdicion modoActual;
    
    private List<MapPoint> visitadosADibujar = new ArrayList<>();
    private List<MapPoint> pathADibujar = new ArrayList<>();
    private Queue<MapPoint> colaExploracion = new LinkedList<>();
    private Timer timerExploration;

    // Variables de control visual
    private final int RADIO_NODO = 7;
    private boolean mostrarTemperaturas = false;

    // Variables temporales para selección y conexión
    private MapPoint nodoSeleccionadoConexion = null;
    private MapPoint nodoSeleccionado = null; 

    // Variables para la selección de búsqueda por clics (Inicio y Destino)
    private boolean modoSeleccionBusqueda = false;
    private String tipoBusquedaActual = null;
    private MapPoint nodoInicioBusqueda = null;
    private MapPoint nodoFinBusqueda = null;

    // Callback para comunicar las búsquedas con MainFrame
    private SearchCallback searchCallback;

    // Botón flotante interno para limpiar búsqueda
    private Rectangle botonLimpiarBounds = new Rectangle(0, 0, 0, 0);

    public interface SearchCallback {
        void onSearch(String tipo, MapPoint inicio, MapPoint fin);
    }

    public MapPanel(Graph<MapPoint> grafo) {
        this.grafo = grafo;
        setBackground(Color.DARK_GRAY);
        setLayout(null); // Permite posicionar componentes libres si es necesario, o usar eventos de clic

        // Cargar la imagen del mapa
        java.net.URL ruta = getClass().getResource("/mapas.png");
        if (ruta != null) {
            imagenMapa = new ImageIcon(ruta).getImage();
        } else {
            System.out.println("No se encontró mapas.png en /resources");
        }

        // LISTENER UNIFICADO: Maneja clics de edición, búsqueda por clics, botón limpiar y selección normal
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                // 0. VERIFICAR SI HIZO CLIC EN EL BOTÓN FLOTANTE "LIMPIAR BÚSQUEDA"
                if (botonLimpiarBounds.contains(mouseX, mouseY)) {
                    limpiarVisualizacion();
                    return;
                }

                // 1. SI ESTAMOS EN MODO EDICIÓN
                if (modoActual != null && grafo != null) {
                    switch (modoActual) {
                        case AGREGAR_NODO:
                            int maxId = 0;
                            if (grafo.getNodes() != null) {
                                for (Node<MapPoint> n : grafo.getNodes()) {
                                    String idStr = n.getValue().getId();
                                    try {
                                        if (idStr.startsWith("N")) {
                                            int numero = Integer.parseInt(idStr.substring(1));
                                            if (numero > maxId) {
                                                maxId = numero;
                                            }
                                        }
                                    } catch (NumberFormatException ex) {
                                        // Ignorar formatos personalizados
                                    }
                                }
                            }
                            
                            String idNuevo = "N" + (maxId + 1);
                            MapPoint nuevoPunto = new MapPoint(idNuevo, mouseX, mouseY);
                            grafo.add(nuevoPunto); 
                            repaint();
                            break;

                        case ELIMINAR_NODO:
                            Node<MapPoint> nodoAEliminar = buscarNodoCercano(mouseX, mouseY);
                            if (nodoAEliminar != null) {
                                grafo.remove(nodoAEliminar.getValue()); 
                                repaint();
                            }
                            break;

                        case CONECTAR_NODOS:
                            Node<MapPoint> nodoClick1 = buscarNodoCercano(mouseX, mouseY);
                            if (nodoClick1 != null) {
                                if (nodoSeleccionadoConexion == null) {
                                    nodoSeleccionadoConexion = nodoClick1.getValue();
                                    JOptionPane.showMessageDialog(MapPanel.this, "Nodo " + nodoSeleccionadoConexion.getId() + " seleccionado. Haz clic en el segundo nodo.");
                                } else {
                                    grafo.addConection(nodoSeleccionadoConexion, nodoClick1.getValue());
                                    nodoSeleccionadoConexion = null;
                                    JOptionPane.showMessageDialog(MapPanel.this, "Conexión creada con éxito.");
                                    repaint();
                                }
                            }
                            break;

                        case ELIMINAR_CONEXION:
                            Node<MapPoint> nodoClick2 = buscarNodoCercano(mouseX, mouseY);
                            if (nodoClick2 != null) {
                                if (nodoSeleccionadoConexion == null) {
                                    nodoSeleccionadoConexion = nodoClick2.getValue();
                                    JOptionPane.showMessageDialog(MapPanel.this, "Nodo " + nodoSeleccionadoConexion.getId() + " seleccionado. Haz clic en el nodo con el que pierde conexión.");
                                } else {
                                    grafo.removeConnection(nodoSeleccionadoConexion, nodoClick2.getValue());
                                    nodoSeleccionadoConexion = null;
                                    JOptionPane.showMessageDialog(MapPanel.this, "Conexión eliminada.");
                                    repaint();
                                }
                            }
                            break;
                    }
                    return; 
                }

                // 2. SI ESTAMOS SELECCIONANDO NODOS PARA UNA BÚSQUEDA (ej. DFS/BFS)
                if (modoSeleccionBusqueda) {
                    Node<MapPoint> nodoCercano = buscarNodoCercano(mouseX, mouseY);
                    if (nodoCercano != null) {
                        if (nodoInicioBusqueda == null) {
                            nodoInicioBusqueda = nodoCercano.getValue();
                            JOptionPane.showMessageDialog(MapPanel.this, "Inicio seleccionado: " + nodoInicioBusqueda.getId() + "\nHaz clic en el nodo de destino.");
                            repaint();
                        } else if (nodoFinBusqueda == null) {
                            nodoFinBusqueda = nodoCercano.getValue();
                            JOptionPane.showMessageDialog(MapPanel.this, "Destino seleccionado: " + nodoFinBusqueda.getId());
                            
                            ejecutarBusquedaSeleccionada();
                            modoSeleccionBusqueda = false;
                            repaint();
                        }
                    }
                    return;
                }
                        
                // 3. MODO NORMAL: Seleccionar nodo individual
                Node<MapPoint> nodoCercano = buscarNodoCercano(mouseX, mouseY);
                if (nodoCercano != null) {
                    nodoSeleccionado = nodoCercano.getValue();
                    repaint();
                } else {
                    nodoSeleccionado = null;
                    repaint();
                }
            }
        });

        // LISTENER: Cambiar cursor a "Mano" cuando el ratón pase sobre un nodo o sobre el botón de limpiar
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (botonLimpiarBounds.contains(e.getX(), e.getY())) {
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    return;
                }

                if (grafo != null && grafo.getNodes() != null) {
                    boolean sobreNodo = false;
                    for (Node<MapPoint> n : grafo.getNodes()) {
                        MapPoint p = n.getValue();
                        double distancia = Math.sqrt(Math.pow(e.getX() - p.getX(), 2) + Math.pow(e.getY() - p.getY(), 2));
                        if (distancia <= RADIO_NODO + 8) {
                            sobreNodo = true;
                            break;
                        }
                    }
                    setCursor(sobreNodo ? new Cursor(Cursor.HAND_CURSOR) : new Cursor(Cursor.DEFAULT_CURSOR));
                } else {
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });
    }

    public void setSearchCallback(SearchCallback callback) {
        this.searchCallback = callback;
    }

    public void activarModoSeleccionBusqueda(String tipoBusqueda) {
        this.modoSeleccionBusqueda = true;
        this.tipoBusquedaActual = tipoBusqueda;
        this.nodoInicioBusqueda = null;
        this.nodoFinBusqueda = null;
        this.modoActual = null;
        JOptionPane.showMessageDialog(this, "Modo " + tipoBusqueda + ": Haz clic en el nodo de INICIO en el mapa.");
        repaint();
    }

    private void ejecutarBusquedaSeleccionada() {
        if (nodoInicioBusqueda != null && nodoFinBusqueda != null && searchCallback != null) {
            searchCallback.onSearch(tipoBusquedaActual, nodoInicioBusqueda, nodoFinBusqueda);
        }
    }

    public void setModoActual(ModoEdicion modo) {
        this.modoActual = modo;
        this.nodoSeleccionadoConexion = null; 
        this.modoSeleccionBusqueda = false;
    }

    public MapPoint getNodoSeleccionado() {
        return nodoSeleccionado;
    }

    private Node<MapPoint> buscarNodoCercano(int x, int y) {
        if (grafo == null || grafo.getNodes() == null) return null;
        for (Node<MapPoint> n : grafo.getNodes()) {
            MapPoint p = n.getValue();
            double distancia = Math.sqrt(Math.pow(x - p.getX(), 2) + Math.pow(y - p.getY(), 2));
            if (distancia <= RADIO_NODO + 8) {
                return n;
            }
        }
        return null;
    }

    public void setModoVisualizacion(TipoVisualizacion modoV) {
        this.modoVisualizacion = modoV;
        repaint();
    }

    public void limpiarVisualizacion() {
        if (timerExploration != null) {
            timerExploration.stop();
            timerExploration = null;
        }
        visitadosADibujar.clear();
        pathADibujar.clear();
        colaExploracion.clear();
        nodoSeleccionado = null;
        nodoInicioBusqueda = null;
        nodoFinBusqueda = null;
        modoSeleccionBusqueda = false;
        repaint();
    }

    public void toggleTemperaturas() {
        this.mostrarTemperaturas = !this.mostrarTemperaturas;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graficos) {
        super.paintComponent(graficos);
        Graphics2D g2 = (Graphics2D) graficos;
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (imagenMapa != null) {
            g2.drawImage(imagenMapa, 0, 0, getWidth(), getHeight(), this);
        }

        if (grafo == null) return;

        // DIBUJAR CONEXIONES RESPETANDO DIRECCIONALIDAD Y FLECHAS
        for (Node<MapPoint> nodoOrigenNode : grafo.getNodes()) {
            MapPoint origen = nodoOrigenNode.getValue();
            
            Set<Node<MapPoint>> vecinosSalientes = grafo.getVecinos(origen);
            if (vecinosSalientes == null) continue;

            for (Node<MapPoint> nodoDestinoNode : vecinosSalientes) {
                MapPoint destino = nodoDestinoNode.getValue();
                
                boolean inversoExiste = false;
                
                Set<Node<MapPoint>> vecinosDelDestino = grafo.getVecinos(destino);
                if (vecinosDelDestino != null) {
                    for (Node<MapPoint> vecinoDelDestino : vecinosDelDestino) {
                        if (vecinoDelDestino.getValue().equals(origen)) {
                            inversoExiste = true;
                            break;
                        }
                    }
                }

                dibujarFlechaConOffset(g2, origen.getX(), origen.getY(), destino.getX(), destino.getY(), inversoExiste);
            }
        }

        // Dibujar nodos
        for (Node<MapPoint> nodo : grafo.getNodes()) {
            MapPoint punto = nodo.getValue();
            int x = punto.getX();
            int y = punto.getY();

            if (nodoInicioBusqueda != null && nodoInicioBusqueda.equals(punto)) {
                g2.setColor(new Color(40, 180, 70));
                g2.fillOval(x - (RADIO_NODO + 4), y - (RADIO_NODO + 4), (RADIO_NODO + 4) * 2, (RADIO_NODO + 4) * 2);
            } else if (nodoFinBusqueda != null && nodoFinBusqueda.equals(punto)) {
                g2.setColor(new Color(255, 193, 7));
                g2.fillOval(x - (RADIO_NODO + 4), y - (RADIO_NODO + 4), (RADIO_NODO + 4) * 2, (RADIO_NODO + 4) * 2);
            } else if (nodoSeleccionado != null && nodoSeleccionado.equals(punto)) {
                g2.setColor(new Color(255, 193, 7));
                g2.fillOval(x - (RADIO_NODO + 3), y - (RADIO_NODO + 3), (RADIO_NODO + 3) * 2, (RADIO_NODO + 3) * 2);
            }

            g2.setColor(new Color(220, 53, 69));
            g2.fillOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

            g2.setColor(new Color(120, 20, 20));
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

            if (mostrarTemperaturas && nodo.getTemperatura() != null) {
                g2.setColor(new Color(240, 200, 0));
                g2.setFont(new Font("Arial", Font.BOLD, 11));
                g2.drawString(nodo.getTemperatura().toString(), x + 9, y + 12);
            }
        }

        for (MapPoint punto : visitadosADibujar) {
            g2.setColor(Color.ORANGE);
            g2.fillOval(punto.getX() - 6, punto.getY() - 6, 12, 12);
        }

        if (pathADibujar.size() > 1) {
            g2.setColor(new Color(40, 180, 70));
            g2.setStroke(new BasicStroke(4.5f));
            for (int i = 0; i < pathADibujar.size() - 1; i++) {
                MapPoint actual = pathADibujar.get(i);
                MapPoint siguiente = pathADibujar.get(i + 1);
                g2.drawLine(actual.getX(), actual.getY(), siguiente.getX(), siguiente.getY());
            }
        }

        for (MapPoint punto : pathADibujar) {
            g2.setColor(new Color(40, 180, 70));
            g2.fillOval(punto.getX() - 6, punto.getY() - 6, 12, 12);
        }

        if (!pathADibujar.isEmpty()) {
            MapPoint destino = pathADibujar.get(pathADibujar.size() - 1);
            int px = destino.getX();
            int py = destino.getY();

            int[] xPoints = {px - 7, px + 7, px};
            int[] yPoints = {py - 12, py - 12, py};
            g2.setColor(new Color(215, 40, 40));
            g2.fillPolygon(xPoints, yPoints, 3);
            g2.fillOval(px - 9, py - 24, 18, 18);

            g2.setColor(new Color(120, 10, 10));
            g2.drawOval(px - 9, py - 24, 18, 18);

            g2.setColor(Color.WHITE);
            g2.fillOval(px - 3, py - 18, 6, 6);
        }

        // DIBUJAR BOTÓN FLOTANTE "LIMPIAR BÚSQUEDA" EN LA ESQUINA SUPERIOR DERECHA
        if (!pathADibujar.isEmpty() || !visitadosADibujar.isEmpty() || nodoInicioBusqueda != null) {
            String textoBoton = "Limpiar Búsqueda";
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int anchoTexto = fm.stringWidth(textoBoton);
            
            int btnW = anchoTexto + 20;
            int btnH = 28;
            int btnX = getWidth() - btnW - 20;
            int btnY = 20;

            // Actualizar límites para detectar clics
            botonLimpiarBounds.setBounds(btnX, btnY, btnW, btnH);

            // Fondo del botón (rojo elegante o gris oscuro)
            g2.setColor(new Color(220, 53, 69));
            g2.fillRoundRect(btnX, btnY, btnW, btnH, 8, 8);

            // Borde del botón
            g2.setColor(new Color(160, 30, 40));
            g2.drawRoundRect(btnX, btnY, btnW, btnH, 8, 8);

            // Texto del botón
            g2.setColor(Color.WHITE);
            g2.drawString(textoBoton, btnX + 10, btnY + 19);
        } else {
            // Si no hay búsqueda activa, resetear las bounds para que no interfiera
            botonLimpiarBounds.setBounds(0, 0, 0, 0);
        }
    }

    private void dibujarFlechaConOffset(Graphics2D g2, int x1, int y1, int x2, int y2, boolean conOffset) {
        g2.setColor(new Color(30, 115, 190)); 
        g2.setStroke(new BasicStroke(2.0f)); 

        double dx = x2 - x1;
        double dy = y2 - y1;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        if (distancia == 0) return;

        double ux = dx / distancia;
        double uy = dy / distancia;

        double nx = -uy;
        double ny = ux;
        double offset = conOffset ? 5.5 : 0.0;

        double startX = x1 + nx * offset;
        double startY = y1 + ny * offset;
        double endX = x2 + nx * offset;
        double endY = y2 + ny * offset;

        double xA = startX + ux * (RADIO_NODO + 1);
        double yA = startY + uy * (RADIO_NODO + 1);
        double xB = endX - ux * (RADIO_NODO + 3);
        double yB = endY - uy * (RADIO_NODO + 3);

        g2.drawLine((int) xA, (int) yA, (int) xB, (int) yB);

        double angulo = Math.atan2(dy, dx);
        int tamFlecha = 8;
        AffineTransform tx = g2.getTransform();
        g2.translate(xB, yB);
        g2.rotate(angulo);
        
        Polygon cabezaFlecha = new Polygon();
        cabezaFlecha.addPoint(0, 0);
        cabezaFlecha.addPoint(-tamFlecha, -tamFlecha / 2);
        cabezaFlecha.addPoint(-tamFlecha, tamFlecha / 2);
        g2.fill(cabezaFlecha);
        
        g2.setTransform(tx);
    }

    @Override
    public void onNodeVisited(MapPoint nodoPunto) {
        colaExploracion.add(nodoPunto);
        if (timerExploration == null) {
            timerExploration = new Timer(250, e -> {
                if (!colaExploracion.isEmpty()) {
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

    public void mostrarRuta(List<MapPoint> rutaExploracion) {
        pathADibujar.clear();
        Timer timer = new Timer(250, null);
        final int[] indice = {0};
        timer.addActionListener(e -> {
            if (indice[0] < rutaExploracion.size()) {
                pathADibujar.add(rutaExploracion.get(indice[0]));
                indice[0]++;
                repaint();
            } else {
                timer.stop();
            }
        });
        timer.start();
    }

    public boolean exploracionTerminada() {
        return colaExploracion.isEmpty() && (timerExploration == null || !timerExploration.isRunning());
    }

    public void setGrafo(Graph<MapPoint> grafo) {
        this.grafo = grafo;
        repaint();
    }

    private boolean perteneceARuta(MapPoint punto) {
    for (MapPoint p : pathADibujar) {
        if (p.equals(punto)) {
            return true;
        }
    }
    return false;
}
}