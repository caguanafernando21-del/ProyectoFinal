package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controllers.MapController;
import models.MapPoint;
import models.VisualizationMode.ModoEdicion;
import models.VisualizationMode.TipoVisualizacion;
import persistance.FileGraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder;
import structures.node.Node; 

public class MainFrame extends JFrame {

    // Paleta de colores ajustada a la imagen
    private static final Color COLOR_FONDO_MENU = new Color(38, 50, 66);      // Azul oscuro lateral
    private static final Color COLOR_BOTON = new Color(50, 68, 90);           // Fondo del botón
    private static final Color COLOR_BOTON_HOVER = new Color(65, 88, 115);    // Hover
    private static final Color COLOR_TEXTO = new Color(205, 218, 230);        // Texto claro
    private static final Color COLOR_SALIR = new Color(217, 83, 79);          // Rojo para "Salir"
    private static final Color COLOR_TITULO_SECCION = new Color(130, 150, 175);// Título de categorías

    // Fuentes
    private static final Font FUENTE_SECCION = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.PLAIN, 13);

    // Atributos del sistema
    private MapPanel mapa;
    private Graph<MapPoint> grafoResultado;
    private FileGraphRepository repositorio = new FileGraphRepository();
    private MapController controladorMapa;

    // Botones del menú lateral
    private JButton btnDfs;
    private JButton btnBfs;
    private JButton btnAgregarNodo;
    private JButton btnEliminarNodo;
    private JButton btnAgregarConexion;
    private JButton btnEliminarConexion;
    private JButton btnTemperatura;
    private JButton btnSalir;

    public MainFrame(MapController controladorMapa) {
        this.controladorMapa = controladorMapa;
        
        // Configuración de la ventana
        setTitle("Google Maps");
        setSize(1000, 680);
        setResizable(false);    
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Grafo y panel de mapa
        grafoResultado = controladorMapa.getGrafo();
        mapa = new MapPanel(grafoResultado);
        mapa.setPreferredSize(new Dimension(670, 520));

        // Configurar el callback para ejecutar búsquedas al hacer clic en los nodos del mapa
        mapa.setSearchCallback((tipo, inicio, fin) -> {
            mapa.limpiarVisualizacion();
            mapa.setModoVisualizacion(TipoVisualizacion.EXPLORATION);

            structures.graphs.PathFinder<MapPoint> finder = tipo.equals("DFS") ? new DFSPathFinder<>() : new BFSPathFinder<>();
            PathResult<MapPoint> resultado = controladorMapa.buscarRuta(inicio, fin, mapa, finder);
            
            if (resultado != null && resultado.getPath() != null) {
                Timer esperar = new Timer(100, null);
                esperar.addActionListener(ev -> {
                    if (mapa.exploracionTerminada()) {
                        mapa.mostrarRuta(new ArrayList<>(resultado.getPath()));
                        esperar.stop();
                    }
                });
                esperar.start();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró una ruta entre los nodos seleccionados.", "Sin ruta", JOptionPane.WARNING_MESSAGE);
            }
        });

        // PANEL LATERAL (Menú)
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(240, 680));
        menuPanel.setBackground(COLOR_FONDO_MENU);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(25, 15, 25, 15));

        // 1. MÉTODOS DE BÚSQUEDA
        agregarCategoria(menuPanel, "MÉTODOS DE BÚSQUEDA");
        btnDfs = crearBotonMenu("Busqueda a profundidad", COLOR_TEXTO);
        btnBfs = crearBotonMenu("Busqueda por niveles", COLOR_TEXTO);

        menuPanel.add(btnDfs);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnBfs);
        menuPanel.add(Box.createVerticalStrut(25));

        // 2. NODOS
        agregarCategoria(menuPanel, "NODOS");
        btnAgregarNodo = crearBotonMenu("Agregar Nodo", COLOR_TEXTO);
        btnEliminarNodo = crearBotonMenu("Eliminar Nodo", COLOR_TEXTO);
        btnAgregarConexion = crearBotonMenu("Conectar Nodos", COLOR_TEXTO);
        btnEliminarConexion = crearBotonMenu("Eliminar Conexion", COLOR_TEXTO);

        menuPanel.add(btnAgregarNodo);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnEliminarNodo);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnAgregarConexion);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnEliminarConexion);
        menuPanel.add(Box.createVerticalStrut(25));

        // 3. EXTRAS
        agregarCategoria(menuPanel, "Extras");
        btnTemperatura = crearBotonMenu("Temperatura", COLOR_TEXTO);
        btnSalir = crearBotonMenu("Salir", COLOR_SALIR);

        menuPanel.add(btnTemperatura);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnSalir);
        menuPanel.add(Box.createVerticalGlue());

        // CONTENEDOR CENTRAL DEL MAPA
        JPanel contenedorMapa = new JPanel(new BorderLayout());
        contenedorMapa.setBorder(new EmptyBorder(15, 20, 20, 20));
        contenedorMapa.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Mapa de Italia");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(40, 50, 60));
        titulo.setBorder(new EmptyBorder(0, 0, 15, 0));

        contenedorMapa.add(titulo, BorderLayout.NORTH);
        contenedorMapa.add(mapa, BorderLayout.CENTER);

        // Ensamblado general
        add(menuPanel, BorderLayout.WEST);
        add(contenedorMapa, BorderLayout.CENTER);

        crearBarraMenu();
        configurarEventos();
    }

    private void configurarEventos() {
        // ALGORITMO BFS (Selección directa por clics en el mapa)
        btnBfs.addActionListener(e -> {
            mapa.activarModoSeleccionBusqueda("BFS");
        });
        
        // ALGORITMO DFS (Selección directa por clics en el mapa)
        btnDfs.addActionListener(e -> {
            mapa.activarModoSeleccionBusqueda("DFS");
        });

        // BOTONES Y SUS EVENTOS DIRECTOS EN EL MAPA
        btnAgregarNodo.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.AGREGAR_NODO);
            JOptionPane.showMessageDialog(this, "Modo Agregar Nodo activado.\nHaga clic en el mapa para colocar el nodo.", "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
        });

        btnEliminarNodo.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.ELIMINAR_NODO);
            JOptionPane.showMessageDialog(this, "Modo Eliminar Nodo activado.\nHaga clic sobre el nodo que desea eliminar.", "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
        });

        btnAgregarConexion.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.CONECTAR_NODOS);
            JOptionPane.showMessageDialog(this, "Modo Conectar Nodos activado.\nHaga clic en dos nodos sucesivamente para enlazarlos.", "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
        });

        btnEliminarConexion.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.ELIMINAR_CONEXION);
            JOptionPane.showMessageDialog(this, "Modo Eliminar Conexión activado.\nHaga clic en los dos nodos conectados para romper el enlace.", "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
        });

        getContentPane().setBackground(Color.WHITE);

        // TOGGLE TEMPERATURA
        btnTemperatura.addActionListener(e -> {
            mapa.toggleTemperaturas();
            mapa.repaint();
        });

        // BOTÓN SALIR
        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void crearBarraMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem guardar = new JMenuItem("Guardar mapa");
        JMenuItem cargar = new JMenuItem("Cargar mapa");

        guardar.addActionListener(e -> {
            repositorio.guardarArchivo(grafoResultado, "src/resources/mapa.json");
            JOptionPane.showMessageDialog(this, "Mapa guardado correctamente.");
        });

        cargar.addActionListener(e -> {
            grafoResultado = repositorio.cargarArchivo("src/resources/mapa.json");
            mapa.setGrafo(grafoResultado);
            actualizarMapa();
            JOptionPane.showMessageDialog(this, "Mapa cargado correctamente.");
        });

        archivo.add(guardar);
        archivo.add(cargar);
        barra.add(archivo);
        setJMenuBar(barra);
    }

    private void actualizarMapa() {
        mapa.setGrafo(grafoResultado);
        mapa.repaint();
    }

    private void agregarCategoria(JPanel panel, String titulo) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(FUENTE_SECCION);
        lbl.setForeground(COLOR_TITULO_SECCION);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(10));
    }

    private JButton crearBotonMenu(String texto, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(colorTexto);
        boton.setBackground(COLOR_BOTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 36));
        boton.setPreferredSize(new Dimension(200, 36));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(COLOR_BOTON_HOVER);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_BOTON);
            }
        });

        return boton;
    }
}