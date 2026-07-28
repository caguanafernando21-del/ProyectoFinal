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

public class MainFrame extends JFrame {

    private static final Color COLOR_FONDO_MENU = new Color(30, 39, 46);      
    private static final Color COLOR_BOTON = new Color(44, 62, 80);           
    private static final Color COLOR_BOTON_HOVER = new Color(52, 73, 94);     
    private static final Color COLOR_TEXTO = new Color(236, 240, 241);        
    private static final Color COLOR_SALIR = new Color(231, 76, 60);          
    private static final Color COLOR_TITULO_SECCION = new Color(149, 165, 166);

    private static final Font FUENTE_SECCION = new Font("Segoe UI", Font.BOLD, 11);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.PLAIN, 13);

    private MapPanel mapa;
    private Graph<MapPoint> grafoResultado;
    private FileGraphRepository repositorio = new FileGraphRepository();
    private MapController controladorMapa;

    private JButton btnDfs, btnBfs, btnAgregarNodo, btnEliminarNodo, btnAgregarConexion, btnEliminarConexion, btnTemperatura, btnSalir;
    private JLabel lblEstado;

    public MainFrame(MapController controladorMapa) {
        this.controladorMapa = controladorMapa;
        
        setTitle("Google Maps - Editor de Rutas");
        setSize(1020, 690);
        setResizable(true); // Permitir redimensionar la ventana (Prueba 8)
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        grafoResultado = controladorMapa.getGrafo();
        mapa = new MapPanel(grafoResultado);
        mapa.setPreferredSize(new Dimension(680, 520));

        mapa.setSearchCallback((tipo, inicio, fin) -> {
            // Validar si el nodo de inicio es igual al nodo de destino (Prueba 12)
            if (inicio.equals(fin)) {
                JOptionPane.showMessageDialog(this, "El nodo de inicio y el nodo destino no pueden ser el mismo.", "Aviso de ruta", JOptionPane.WARNING_MESSAGE);
                lblEstado.setText("Operación cancelada: El inicio y el destino son idénticos.");
                return;
            }

            mapa.limpiarVisualizacion();
            mapa.setModoVisualizacion(TipoVisualizacion.EXPLORATION);
            lblEstado.setText("Calculando ruta con " + tipo + "...");

            structures.graphs.PathFinder<MapPoint> finder = tipo.equals("DFS") ? new DFSPathFinder<>() : new BFSPathFinder<>();
            PathResult<MapPoint> resultado = controladorMapa.buscarRuta(inicio, fin, mapa, finder);
            
            if (resultado != null && resultado.getPath() != null) {
                Timer esperar = new Timer(100, null);
                esperar.addActionListener(ev -> {
                    if (mapa.exploracionTerminada()) {
                        mapa.mostrarRuta(new ArrayList<>(resultado.getPath()));
                        lblEstado.setText("Ruta encontrada exitosamente.");
                        esperar.stop();
                    }
                });
                esperar.start();
            } else {
                lblEstado.setText("No se encontró una ruta disponible (Nodo destino sin conexión).");
                JOptionPane.showMessageDialog(this, "No se encontró una ruta entre los nodos seleccionados.", "Sin ruta", JOptionPane.WARNING_MESSAGE);
            }
        });

        // PANEL LATERAL (Menú)
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(250, 690));
        menuPanel.setBackground(COLOR_FONDO_MENU);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(25, 15, 25, 15));

        // 1. MÉTODOS DE BÚSQUEDA
        agregarCategoria(menuPanel, "MÉTODOS DE BÚSQUEDA");
        btnDfs = crearBotonMenu("Búsqueda en Profundidad", COLOR_TEXTO);
        btnBfs = crearBotonMenu("Búsqueda por Niveles", COLOR_TEXTO);

        menuPanel.add(btnDfs);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnBfs);
        menuPanel.add(Box.createVerticalStrut(25));

        // 2. NODOS
        agregarCategoria(menuPanel, "EDICIÓN DE NODOS");
        btnAgregarNodo = crearBotonMenu("Agregar Nodo", COLOR_TEXTO);
        btnEliminarNodo = crearBotonMenu("Eliminar Nodo", COLOR_TEXTO);
        btnAgregarConexion = crearBotonMenu("Conectar Nodos", COLOR_TEXTO);
        btnEliminarConexion = crearBotonMenu("Eliminar Conexión", COLOR_TEXTO);

        menuPanel.add(btnAgregarNodo);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnEliminarNodo);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnAgregarConexion);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnEliminarConexion);
        menuPanel.add(Box.createVerticalStrut(25));

        // 3. EXTRAS
        agregarCategoria(menuPanel, "OPCIONES");
        btnTemperatura = crearBotonMenu("Alternar Temperaturas", COLOR_TEXTO);
        btnSalir = crearBotonMenu("Salir", COLOR_TEXTO);

        menuPanel.add(btnTemperatura);
        menuPanel.add(Box.createVerticalStrut(8));
        menuPanel.add(btnSalir);
        menuPanel.add(Box.createVerticalGlue());

        // CONTENEDOR CENTRAL DEL MAPA
        JPanel contenedorMapa = new JPanel(new BorderLayout());
        contenedorMapa.setBorder(new EmptyBorder(15, 20, 15, 20));
        contenedorMapa.setBackground(Color.WHITE);

        JLabel titulo = new JLabel("Mapa de Italia");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(44, 62, 80));
        titulo.setBorder(new EmptyBorder(0, 0, 10, 0));

        // BARRA DE ESTADO INFERIOR
        lblEstado = new JLabel(" Estado: Listo. Selecciona una opción o haz clic en un nodo.");
        lblEstado.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblEstado.setForeground(new Color(127, 140, 141));
        lblEstado.setBorder(new EmptyBorder(8, 0, 0, 0));

        contenedorMapa.add(titulo, BorderLayout.NORTH);
        contenedorMapa.add(mapa, BorderLayout.CENTER);
        contenedorMapa.add(lblEstado, BorderLayout.SOUTH);

        add(menuPanel, BorderLayout.WEST);
        add(contenedorMapa, BorderLayout.CENTER);

        crearBarraMenu();
        configurarEventos();
    }

    private void configurarEventos() {
        btnBfs.addActionListener(e -> {
            mapa.activarModoSeleccionBusqueda("BFS");
            lblEstado.setText("Modo BFS: Haz clic en el nodo de inicio.");
        });
        
        btnDfs.addActionListener(e -> {
            mapa.activarModoSeleccionBusqueda("DFS");
            lblEstado.setText("Modo DFS: Haz clic en el nodo de inicio.");
        });

        btnAgregarNodo.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.AGREGAR_NODO);
            lblEstado.setText("Modo Edición: Haz clic en el mapa para agregar un nodo.");
        });

        btnEliminarNodo.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.ELIMINAR_NODO);
            lblEstado.setText("Modo Edición: Haz clic en un nodo para eliminarlo.");
        });

        btnAgregarConexion.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.CONECTAR_NODOS);
            lblEstado.setText("Modo Edición: Selecciona dos nodos para conectarlos.");
        });

        btnEliminarConexion.addActionListener(e -> {
            mapa.setModoActual(ModoEdicion.ELIMINAR_CONEXION);
            lblEstado.setText("Modo Edición: Selecciona dos nodos para desconectarlos.");
        });

        btnTemperatura.addActionListener(e -> {
            mapa.toggleTemperaturas();
            lblEstado.setText("Visualización de temperaturas actualizada.");
        });

        btnSalir.addActionListener(e -> System.exit(0));
    }

    private void crearBarraMenu() {
        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem guardar = new JMenuItem("Guardar mapa");
        JMenuItem cargar = new JMenuItem("Cargar mapa");

        guardar.addActionListener(e -> {
            try {
                repositorio.guardarArchivo(grafoResultado, "src/resources/mapa.json");
                lblEstado.setText("Mapa guardado con éxito.");
                JOptionPane.showMessageDialog(this, "Configuración guardada correctamente.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cargar.addActionListener(e -> {
            try {
                Graph<MapPoint> grafoCargado = repositorio.cargarArchivo("src/resources/mapa.json");
                if (grafoCargado != null && grafoCargado.getNodes() != null) {
                    grafoResultado = grafoCargado;
                    mapa.setGrafo(grafoResultado);
                    mapa.repaint();
                    lblEstado.setText("Configuración válida cargada con éxito.");
                    JOptionPane.showMessageDialog(this, "Mapa cargado correctamente.", "Carga exitosa", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Control de carga con errores / estructura corrupta (Prueba 18)
                    JOptionPane.showMessageDialog(this, "El archivo contiene errores, formato inválido o está vacío.", "Error de Carga", JOptionPane.ERROR_MESSAGE);
                    lblEstado.setText("Error: Configuración inválida.");
                }
            } catch (Exception ex) {
                // Captura excepciones de sintaxis JSON o archivos inexistentes (Prueba 14 y 18)
                JOptionPane.showMessageDialog(this, "Error crítico al procesar la configuración: " + ex.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
                lblEstado.setText("Error al cargar la configuración.");
            }
        });

        archivo.add(guardar);
        archivo.add(cargar);
        barra.add(archivo);
        setJMenuBar(barra);
    }

    private void agregarCategoria(JPanel panel, String titulo) {
        JLabel lbl = new JLabel(titulo);
        lbl.setFont(FUENTE_SECCION);
        lbl.setForeground(COLOR_TITULO_SECCION);
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(6));
    }

    private JButton crearBotonMenu(String texto, Color colorTexto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(colorTexto);
        boton.setBackground(COLOR_BOTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(220, 38));
        boton.setPreferredSize(new Dimension(220, 38));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (texto.equals("Salir")) {
                    boton.setBackground(COLOR_SALIR);
                } else {
                    boton.setBackground(COLOR_BOTON_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(COLOR_BOTON);
            }
        });

        return boton;
    }
}