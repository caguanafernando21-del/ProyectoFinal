package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
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
    
    private TipoVisualizacion modoVisualizacion = null; 
    private ModoEdicion modoActual;
    
    private List<MapPoint> visitadosADibujar = new ArrayList<>();
    private List<MapPoint> pathADibujar = new ArrayList<>();
    private Queue<MapPoint> colaExploracion = new LinkedList<>();

    private Timer timerExploration;
    private Timer timerPath;

    private Set<String> aristasUnidireccionales = new HashSet<>();

    private final int RADIO_NODO = 5;
    private boolean mostrarTemperaturas = false;

    private MapPoint nodoSeleccionadoConexion = null;
    private MapPoint nodoSeleccionado = null; 

    private boolean modoSeleccionBusqueda = false;
    private String tipoBusquedaActual = null;
    private MapPoint nodoInicioBusqueda = null;
    private MapPoint nodoFinBusqueda = null;

    private SearchCallback searchCallback;

    private Rectangle botonLimpiarBounds = new Rectangle(0, 0, 0, 0);
    private Rectangle tarjetaInfoBounds = new Rectangle(0, 0, 0, 0);

    private JScrollPane scrollRegistro;
    private JTextArea textRegistroRuta;

    public interface SearchCallback {
        void onSearch(String tipo, MapPoint inicio, MapPoint fin);
    }

    public MapPanel(Graph<MapPoint> grafo) {
        this.grafo = grafo;
        setBackground(new Color(30, 39, 46));
        setLayout(null);

        textRegistroRuta = new JTextArea();
        textRegistroRuta.setEditable(false);
        textRegistroRuta.setLineWrap(true);
        textRegistroRuta.setWrapStyleWord(true);
        textRegistroRuta.setBackground(new Color(24, 32, 38, 245)); 
        textRegistroRuta.setForeground(new Color(236, 240, 241));
        textRegistroRuta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textRegistroRuta.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        textRegistroRuta.setOpaque(true);

        scrollRegistro = new JScrollPane(textRegistroRuta);
        scrollRegistro.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219, 150), 1, true),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        scrollRegistro.setOpaque(false);
        scrollRegistro.getViewport().setOpaque(false);
        scrollRegistro.setVisible(false);
        add(scrollRegistro);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int padding = 20;
                int logHeight = 110;
                scrollRegistro.setBounds(padding, getHeight() - logHeight - padding, getWidth() - (padding * 2), logHeight);
            }
        });

        java.net.URL ruta = getClass().getResource("/mapas.png");
        if (ruta != null) {
            imagenMapa = new ImageIcon(ruta).getImage();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                if (botonLimpiarBounds.contains(mouseX, mouseY)) {
                    limpiarVisualizacion();
                    return;
                }

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
                                            if (numero > maxId) maxId = numero;
                                        }
                                    } catch (NumberFormatException ignored) {}
                                }
                            }
                            String idNuevo = "N" + (maxId + 1);
                            grafo.add(new MapPoint(idNuevo, mouseX, mouseY)); 
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
                                    repaint();
                                } else {
                                    MapPoint origen = nodoSeleccionadoConexion;
                                    MapPoint destino = nodoClick1.getValue();
                                    
                                    if (!origen.equals(destino)) {
                                        int eleccion = mostrarDialogoElegante(origen, destino);

                                        if (eleccion == 0) { 
                                            grafo.addConection(origen, destino);
                                            aristasUnidireccionales.add(origen.getId() + "->" + destino.getId());
                                        } else if (eleccion == 1) { 
                                            grafo.addConection(origen, destino);
                                            grafo.addConection(destino, origen); 
                                            aristasUnidireccionales.remove(origen.getId() + "->" + destino.getId());
                                            aristasUnidireccionales.remove(destino.getId() + "->" + origen.getId());
                                        }
                                    }
                                    nodoSeleccionadoConexion = null;
                                    repaint();
                                }
                            }
                            break;

                        case ELIMINAR_CONEXION:
                            Node<MapPoint> nodoClick2 = buscarNodoCercano(mouseX, mouseY);
                            if (nodoClick2 != null) {
                                if (nodoSeleccionadoConexion == null) {
                                    nodoSeleccionadoConexion = nodoClick2.getValue();
                                    repaint();
                                } else {
                                    MapPoint origen = nodoSeleccionadoConexion;
                                    MapPoint destino = nodoClick2.getValue();
                                    grafo.removeConnection(origen, destino);
                                    aristasUnidireccionales.remove(origen.getId() + "->" + destino.getId());
                                    aristasUnidireccionales.remove(destino.getId() + "->" + origen.getId());
                                    nodoSeleccionadoConexion = null;
                                    repaint();
                                }
                            }
                            break;
                    }
                    return; 
                }

                if (modoSeleccionBusqueda) {
                    Node<MapPoint> nodoCercano = buscarNodoCercano(mouseX, mouseY);
                    if (nodoCercano != null) {
                        if (nodoInicioBusqueda == null) {
                            nodoInicioBusqueda = nodoCercano.getValue();
                            repaint();
                        } else if (nodoFinBusqueda == null) {
                            nodoFinBusqueda = nodoCercano.getValue();
                            ejecutarBusquedaSeleccionada();
                            modoSeleccionBusqueda = false;
                            repaint();
                        }
                    }
                    return;
                }
                        
                Node<MapPoint> nodoCercano = buscarNodoCercano(mouseX, mouseY);
                if (nodoCercano != null) {
                    nodoSeleccionado = nodoCercano.getValue();
                } else {
                    if (!tarjetaInfoBounds.contains(mouseX, mouseY)) {
                        nodoSeleccionado = null;
                    }
                }
                repaint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (botonLimpiarBounds.contains(e.getX(), e.getY()) || tarjetaInfoBounds.contains(e.getX(), e.getY())) {
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

    // --- NUEVO DIÁLOGO MODERNO PARA NOTIFICACIONES DE MODO ---
    public void mostrarNotificacionModo(String titulo, String mensaje) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Modo Cambiado", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0)); 
        
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(33, 43, 54)); // Fondo oscuro moderno
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.setColor(new Color(52, 152, 219, 200)); // Borde sutil azulado
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
                g2.dispose();
            }
        };
        panelPrincipal.setLayout(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(22, 26, 22, 26));
        panelPrincipal.setOpaque(false);

        // Icono circular elegante
        JLabel lblIcono = new JLabel("i");
        lblIcono.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblIcono.setForeground(Color.WHITE);
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel iconContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(52, 152, 219));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        iconContainer.setPreferredSize(new Dimension(38, 38));
        iconContainer.setOpaque(false);
        iconContainer.setLayout(new BorderLayout());
        iconContainer.add(lblIcono, BorderLayout.CENTER);

        // Textos
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setOpaque(false);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblMensaje = new JLabel("<html><body style='width: 270px;'>" + mensaje + "</body></html>");
        lblMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblMensaje.setForeground(new Color(189, 195, 199));

        textPanel.add(lblTitulo);
        textPanel.add(lblMensaje);

        JPanel topContent = new JPanel(new BorderLayout(16, 0));
        topContent.setOpaque(false);
        topContent.add(iconContainer, BorderLayout.WEST);
        topContent.add(textPanel, BorderLayout.CENTER);

        panelPrincipal.add(topContent, BorderLayout.CENTER);

        // Botón Aceptar Moderno
        JButton btnAceptar = crearBotonElegante("Aceptar", new Color(52, 152, 219));
        btnAceptar.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(btnAceptar);

        panelPrincipal.add(btnPanel, BorderLayout.SOUTH);

        dialog.add(panelPrincipal);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private int mostrarDialogoElegante(MapPoint origen, MapPoint destino) {
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Tipo de Conexión", Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setUndecorated(true); 
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(44, 62, 80), 2));
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 15));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel titulo = new JLabel("Configurar Conexión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JLabel mensaje = new JLabel("¿Qué tipo de ruta deseas crear entre " + origen.getId() + " y " + destino.getId() + "?");
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        mensaje.setForeground(new Color(52, 73, 94));
        mensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(mensaje, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnUni = crearBotonElegante("Unidireccional", new Color(52, 152, 219));
        JButton btnBi = crearBotonElegante("Bidireccional", new Color(46, 204, 113));
        JButton btnCancelar = crearBotonElegante("Cancelar", new Color(231, 76, 60));

        final int[] eleccion = {-1};

        btnUni.addActionListener(e -> { eleccion[0] = 0; dialog.dispose(); });
        btnBi.addActionListener(e -> { eleccion[0] = 1; dialog.dispose(); });
        btnCancelar.addActionListener(e -> { eleccion[0] = -1; dialog.dispose(); });

        panelBotones.add(btnUni);
        panelBotones.add(btnBi);
        panelBotones.add(btnCancelar);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        dialog.add(panelPrincipal);
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return eleccion[0];
    }

    private JButton crearBotonElegante(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(color);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(color.darker());
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(color);
            }
        });
        return btn;
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
        repaint();
    }

    private void ejecutarBusquedaSeleccionada() {
        if (nodoInicioBusqueda != null && nodoFinBusqueda != null && searchCallback != null) {
            if (modoVisualizacion != null) {
                scrollRegistro.setVisible(true);
                actualizarTextoRegistro();
            } else {
                scrollRegistro.setVisible(false);
            }
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
        if (timerPath != null) {
            timerPath.stop();
            timerPath = null;
        }
        visitadosADibujar.clear();
        pathADibujar.clear();
        colaExploracion.clear();
        nodoSeleccionado = null;
        nodoInicioBusqueda = null;
        nodoFinBusqueda = null;
        modoSeleccionBusqueda = false;
        
        scrollRegistro.setVisible(false);
        textRegistroRuta.setText("");
        
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
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (imagenMapa != null) {
            g2.drawImage(imagenMapa, 0, 0, getWidth(), getHeight(), this);
        }

        if (grafo == null) return;

        Set<String> aristasDibujadas = new HashSet<>();

        for (Node<MapPoint> nodoOrigenNode : grafo.getNodes()) {
            MapPoint origen = nodoOrigenNode.getValue();
            Set<Node<MapPoint>> vecinosSalientes = grafo.getVecinos(origen);
            if (vecinosSalientes == null) continue;

            for (Node<MapPoint> nodoDestinoNode : vecinosSalientes) {
                MapPoint destino = nodoDestinoNode.getValue();
                
                String idArista = origen.getId().compareTo(destino.getId()) < 0 ? 
                                  origen.getId() + "-" + destino.getId() : 
                                  destino.getId() + "-" + origen.getId();

                if (aristasDibujadas.contains(idArista)) {
                    continue; 
                }
                aristasDibujadas.add(idArista);
                
                dibujarAristaUnica(g2, origen, destino);
            }
        }

        for (Node<MapPoint> nodo : grafo.getNodes()) {
            MapPoint punto = nodo.getValue();
            int x = punto.getX();
            int y = punto.getY();

            if (nodoInicioBusqueda != null && nodoInicioBusqueda.equals(punto)) {
                g2.setColor(new Color(46, 204, 113));
                g2.fillOval(x - (RADIO_NODO + 5), y - (RADIO_NODO + 5), (RADIO_NODO + 5) * 2, (RADIO_NODO + 5) * 2);
            } else if (nodoFinBusqueda != null && nodoFinBusqueda.equals(punto)) {
                g2.setColor(new Color(241, 196, 15));
                g2.fillOval(x - (RADIO_NODO + 5), y - (RADIO_NODO + 5), (RADIO_NODO + 5) * 2, (RADIO_NODO + 5) * 2);
            } else if (nodoSeleccionado != null && nodoSeleccionado.equals(punto)) {
                g2.setColor(new Color(52, 152, 219));
                g2.fillOval(x - (RADIO_NODO + 5), y - (RADIO_NODO + 5), (RADIO_NODO + 5) * 2, (RADIO_NODO + 5) * 2);
            }

            g2.setColor(new Color(231, 76, 60));
            g2.fillOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

            g2.setColor(new Color(192, 57, 43));
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawOval(x - RADIO_NODO, y - RADIO_NODO, RADIO_NODO * 2, RADIO_NODO * 2);

            if (mostrarTemperaturas && nodo.getTemperatura() != null) {
                String tempText = nodo.getTemperatura().toString() + "°C";
                g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                
                FontMetrics fmTemp = g2.getFontMetrics();
                int tWidth = fmTemp.stringWidth(tempText) + 8;
                int tHeight = 16;
                
                int tX = x + 12;
                int tY = y - 10;

                g2.setColor(new Color(255, 255, 255, 240));
                g2.fillRoundRect(tX, tY, tWidth, tHeight, 6, 6);
                
                g2.setColor(new Color(189, 195, 199));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(tX, tY, tWidth, tHeight, 6, 6);

                g2.setColor(new Color(44, 62, 80));
                g2.drawString(tempText, tX + 4, tY + 12);
            }
        }

        if (modoVisualizacion == null || modoVisualizacion == TipoVisualizacion.EXPLORATION) {
            for (MapPoint punto : visitadosADibujar) {
                g2.setColor(new Color(230, 126, 34));
                g2.fillOval(punto.getX() - 6, punto.getY() - 6, 12, 12);
            }
        }

        if (modoVisualizacion == null || modoVisualizacion == TipoVisualizacion.FINAL_PATH) {
            if (pathADibujar.size() > 1) {
                g2.setColor(new Color(46, 204, 113));
                g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < pathADibujar.size() - 1; i++) {
                    MapPoint actual = pathADibujar.get(i);
                    MapPoint siguiente = pathADibujar.get(i + 1);
                    g2.drawLine(actual.getX(), actual.getY(), siguiente.getX(), siguiente.getY());
                }
            }

            for (MapPoint punto : pathADibujar) {
                g2.setColor(new Color(46, 204, 113));
                g2.fillOval(punto.getX() - 6, punto.getY() - 6, 12, 12);
            }

            if (!pathADibujar.isEmpty()) {
                MapPoint destino = pathADibujar.get(pathADibujar.size() - 1);
                int px = destino.getX();
                int py = destino.getY();

                int[] xPoints = {px - 7, px + 7, px};
                int[] yPoints = {py - 12, py - 12, py};
                g2.setColor(new Color(231, 76, 60));
                g2.fillPolygon(xPoints, yPoints, 3);
                g2.fillOval(px - 9, py - 24, 18, 18);
                g2.setColor(Color.WHITE);
                g2.fillOval(px - 3, py - 18, 6, 6);
            }
        }

        if (nodoSeleccionado != null) {
            int cardW = 220;
            int cardH = 95;
            int cardX = 20;
            int cardY = getHeight() - cardH - 20;

            if (scrollRegistro.isVisible()) {
                cardY -= (scrollRegistro.getHeight() + 10);
            }

            tarjetaInfoBounds.setBounds(cardX, cardY, cardW, cardH);

            g2.setColor(new Color(0, 0, 0, 60));
            g2.fillRoundRect(cardX + 3, cardY + 3, cardW, cardH, 12, 12);

            g2.setColor(new Color(255, 255, 255, 245));
            g2.fillRoundRect(cardX, cardY, cardW, cardH, 12, 12);

            g2.setColor(new Color(189, 195, 199));
            g2.setStroke(new BasicStroke(1f));
            g2.drawRoundRect(cardX, cardY, cardW, cardH, 12, 12);

            g2.setColor(new Color(44, 62, 80));
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            g2.drawString("Información del Nodo", cardX + 15, cardY + 22);

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            g2.drawString("ID: " + nodoSeleccionado.getId(), cardX + 15, cardY + 42);
            g2.drawString("Coordenadas: (" + nodoSeleccionado.getX() + ", " + nodoSeleccionado.getY() + ")", cardX + 15, cardY + 60);
            
            String tempStr = "N/A";
            for (Node<MapPoint> n : grafo.getNodes()) {
                if (n.getValue().equals(nodoSeleccionado)) {
                    if (n.getTemperatura() != null) {
                        tempStr = n.getTemperatura().toString();
                    }
                    break;
                }
            }
            g2.drawString("Temperatura: " + tempStr, cardX + 15, cardY + 78);
        } else {
            tarjetaInfoBounds.setBounds(0, 0, 0, 0);
        }

        if (!pathADibujar.isEmpty() || !visitadosADibujar.isEmpty() || nodoInicioBusqueda != null) {
            String textoBoton = "Limpiar Búsqueda";
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int btnW = fm.stringWidth(textoBoton) + 24;
            int btnH = 32;
            int btnX = getWidth() - btnW - 20;
            int btnY = 20;

            botonLimpiarBounds.setBounds(btnX, btnY, btnW, btnH);

            g2.setColor(new Color(231, 76, 60));
            g2.fillRoundRect(btnX, btnY, btnW, btnH, 8, 8);
            g2.setColor(Color.WHITE);
            g2.drawString(textoBoton, btnX + 12, btnY + 21);
        } else {
            botonLimpiarBounds.setBounds(0, 0, 0, 0);
        }
    }

    private void dibujarAristaUnica(Graphics2D g2, MapPoint origen, MapPoint destino) {
        g2.setColor(new Color(52, 152, 219, 200)); 
        g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); 

        int x1 = origen.getX();
        int y1 = origen.getY();
        int x2 = destino.getX();
        int y2 = destino.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;
        double distancia = Math.sqrt(dx * dx + dy * dy);
        if (distancia == 0) return;

        double ux = dx / distancia;
        double uy = dy / distancia;

        int startX = (int) (x1 + ux * (RADIO_NODO + 2));
        int startY = (int) (y1 + uy * (RADIO_NODO + 2));
        int endX = (int) (x2 - ux * (RADIO_NODO + 2));
        int endY = (int) (y2 - uy * (RADIO_NODO + 2));

        g2.drawLine(startX, startY, endX, endY);

        boolean uni1 = aristasUnidireccionales.contains(origen.getId() + "->" + destino.getId());
        boolean uni2 = aristasUnidireccionales.contains(destino.getId() + "->" + origen.getId());

        if (uni1) {
            dibujarPuntaFlecha(g2, endX, endY, dx, dy);
        } else if (uni2) {
            dibujarPuntaFlecha(g2, startX, startY, -dx, -dy);
        } else {
            dibujarPuntaFlecha(g2, endX, endY, dx, dy);
            dibujarPuntaFlecha(g2, startX, startY, -dx, -dy);
        }
    }

    private void dibujarPuntaFlecha(Graphics2D g2, int x, int y, double dx, double dy) {
        double angulo = Math.atan2(dy, dx);
        int tamFlecha = 10; 
        AffineTransform tx = g2.getTransform();
        g2.translate(x, y);
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
            if (modoVisualizacion == TipoVisualizacion.EXPLORATION) {
                scrollRegistro.setVisible(true);
            } else {
                scrollRegistro.setVisible(false);
            }
            
            timerExploration = new Timer(200, e -> {
                if (!colaExploracion.isEmpty()) {
                    visitadosADibujar.add(colaExploracion.poll());
                    if (modoVisualizacion == TipoVisualizacion.EXPLORATION) {
                        actualizarTextoRegistro();
                    }
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
        if (modoVisualizacion == TipoVisualizacion.FINAL_PATH) {
            scrollRegistro.setVisible(true);
            visitadosADibujar.clear(); 
            iniciarAnimacionRutaVerde(rutaExploracion);
        } else if (modoVisualizacion == TipoVisualizacion.EXPLORATION) {
            scrollRegistro.setVisible(true);
            pathADibujar.clear();
            actualizarTextoRegistro();
        } else {
            scrollRegistro.setVisible(false);
            iniciarAnimacionRutaVerde(rutaExploracion);
        }
    }

    private void iniciarAnimacionRutaVerde(List<MapPoint> ruta) {
        pathADibujar.clear();
        if (timerPath != null && timerPath.isRunning()) {
            timerPath.stop();
        }
        final int[] indice = {0};
        timerPath = new Timer(250, e -> {
            if (indice[0] < ruta.size()) {
                pathADibujar.add(ruta.get(indice[0]));
                indice[0]++;
                if (modoVisualizacion == TipoVisualizacion.FINAL_PATH) {
                    actualizarTextoRegistro();
                }
                repaint();
            } else {
                timerPath.stop();
                timerPath = null;
            }
        });
        timerPath.start();
    }

    private void actualizarTextoRegistro() {
        StringBuilder sb = new StringBuilder();
        
        if (modoVisualizacion == TipoVisualizacion.EXPLORATION) {
            sb.append("⚡ MODO EXPLORACIÓN — Nodos Evaluados:\n");
            if (visitadosADibujar.isEmpty()) {
                sb.append("Buscando nodos visitados...");
            } else {
                for (int i = 0; i < visitadosADibujar.size(); i++) {
                    sb.append(visitadosADibujar.get(i).getId());
                    if (i < visitadosADibujar.size() - 1) sb.append(" -> ");
                }
            }
        } else if (modoVisualizacion == TipoVisualizacion.FINAL_PATH) {
            sb.append("🎯 RUTA FINAL — Camino Óptimo Encontrado:\n");
            if (pathADibujar.isEmpty()) {
                sb.append("Calculando ruta óptima...");
            } else {
                for (int i = 0; i < pathADibujar.size(); i++) {
                    sb.append(pathADibujar.get(i).getId());
                    if (i < pathADibujar.size() - 1) sb.append(" -> ");
                }
            }
        }
        
        textRegistroRuta.setText(sb.toString());
        textRegistroRuta.setCaretPosition(textRegistroRuta.getDocument().getLength());
    }

    public boolean exploracionTerminada() {
        return colaExploracion.isEmpty() && (timerExploration == null || !timerExploration.isRunning());
    }

    public void setGrafo(Graph<MapPoint> grafo) {
        this.grafo = grafo;
        repaint();
    }
}