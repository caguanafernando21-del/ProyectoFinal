// Ubicacion: src/view/MainFrame.java
package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controllers.MapController;
import models.MapPoint;
import models.VisualizationMode.TipoVisualizacion;
import persistance.FileGraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder;
import structures.node.Node; 

public class MainFrame extends JFrame {
    

    public MainFrame() {
    }

    public MainFrame(GraphicsConfiguration gc, MapPanel mapa, Graph<MapPoint> grafoResultado,
            FileGraphRepository repositorio, MapController controladorMapa) {
        super(gc);
        this.mapa = mapa;
        this.grafoResultado = grafoResultado;
        this.repositorio = repositorio;
        this.controladorMapa = controladorMapa;
    }

    public MainFrame(String title, MapPanel mapa, Graph<MapPoint> grafoResultado, FileGraphRepository repositorio,
            MapController controladorMapa) throws HeadlessException {
        super(title);
        this.mapa = mapa;
        this.grafoResultado = grafoResultado;
        this.repositorio = repositorio;
        this.controladorMapa = controladorMapa;
    }

    public MainFrame(String title, GraphicsConfiguration gc, MapPanel mapa, Graph<MapPoint> grafoResultado,
            FileGraphRepository repositorio, MapController controladorMapa) {
        super(title, gc);
        this.mapa = mapa;
        this.grafoResultado = grafoResultado;
        this.repositorio = repositorio;
        this.controladorMapa = controladorMapa;
    }

    // Paleta de colores moderna
    private static final Color COLOR_FONDO_MENU = new Color(44, 62, 80);      // Azul oscuro grisaceo
    private static final Color COLOR_BOTON = new Color(52, 73, 94);             // Fondo de boton normal
    private static final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);   // Azul brillante al pasar el raton
    private static final Color COLOR_TEXTO = new Color(236, 240, 241);        // Blanco humo
    // Fuentes
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FUENTE_SECCION = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.PLAIN, 14);

    //MOTOR del proyecto
    private MapPanel mapa;
    private Graph<MapPoint> grafoResultado;
    private FileGraphRepository repositorio = new FileGraphRepository();
    private MapController controladorMapa;
    
    public MainFrame(MapController controladorMapa) {
        System.out.println("CREANDO MAINFRAME");
        this.controladorMapa = controladorMapa;
        setTitle("Explorador de rutas");
        // Configuracion de la ventana principal
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        grafoResultado = controladorMapa.getGrafo(); //el controlador OBTIENE LOS DATOS
        mapa = new MapPanel(grafoResultado);
        mapa.setPreferredSize(new Dimension(670, 520));

        //parte del MENU LATERAL
        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(270, 700));
        menuPanel.setBackground(COLOR_FONDO_MENU);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(20, 13, 20, 15));
        menuPanel.add(Box.createVerticalStrut(20));
        //GRAFO
        agregarCategoria(menuPanel, "GRAFO");
        JButton btnAgregarNodo = crearBotonMenu("+ Agregar nodo");
        JButton btnEliminarNodo = crearBotonMenu("- Eliminar nodo");
        JButton btnAgregarConexion = crearBotonMenu("+ Agregar conexion");
        JButton btnEliminarConexion = crearBotonMenu("- Eliminar conexion");

        menuPanel.add(btnAgregarNodo);
        // agregar un ESPACIO después de CADA BOTON
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnEliminarNodo);
        menuPanel.add(Box.createHorizontalStrut(5));
        menuPanel.add(btnAgregarConexion);
        menuPanel.add(Box.createHorizontalStrut(5));
        menuPanel.add(btnEliminarConexion);
        menuPanel.add(Box.createHorizontalStrut(25));

        //ALGORITMOS
        agregarCategoria(menuPanel, "ALGORITMOS");
        JButton btnBfs = crearBotonMenu("Breadth-First-Search (BFS)");
        JButton btnDfs = crearBotonMenu("Depth-First-Search (DFS)");
        menuPanel.add(btnBfs);
        menuPanel.add(Box.createVerticalStrut(5));
        menuPanel.add(btnDfs);
        menuPanel.add(Box.createVerticalGlue());
        
        //CENTRO DEL PANEL
        JPanel contenedorMapa = new JPanel(new BorderLayout());
        contenedorMapa.setBorder(new EmptyBorder(20, 20, 20, 20));
        JLabel titulo = new JLabel("Venecia, Italia");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        contenedorMapa.add(titulo, BorderLayout. NORTH);
        contenedorMapa.add(mapa, BorderLayout.CENTER); //le CENTRA al MAPA
        add(menuPanel, BorderLayout.WEST);
        add(contenedorMapa, BorderLayout.CENTER);
        crearBarraMenu(); //corregir


        //BOTONES Y sus EVENTOS
        btnAgregarNodo.addActionListener(e -> {
            JTextField txtId = new JTextField();
            JTextField txtX = new JTextField();
            JTextField txtY = new JTextField();

            Object[] campos = {
                "ID del nodo:", txtId,
                "Coordenada x:", txtX,
                "Coordenada y:", txtY
            };
            int opcion = JOptionPane.showConfirmDialog(this, campos, "Agregar nodo", JOptionPane.OK_CANCEL_OPTION);
            if(opcion == JOptionPane.OK_OPTION){
                try {
                    String id = txtId.getText().trim();
                    int x = Integer.parseInt(txtX.getText());
                    int y = Integer.parseInt(txtY.getText());
                    boolean existe = false; //no EXISTE
                    for(Node<MapPoint> nodo : grafoResultado.getNodes()){
                        if(nodo.getValue().getId().equalsIgnoreCase(id)){
                            existe = true;
                            break;
                        }
                    }
                    if(existe){
                        JOptionPane.showMessageDialog(this, "El ID ya existe.", "Error.", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    MapPoint nuevo = new MapPoint(id, x, y);
                    grafoResultado.add(nuevo);
                    actualizarMapa();
                    JOptionPane.showMessageDialog(this, "Nodo agregado exitosamente.");
                } catch (NumberFormatException exc1){
                    JOptionPane.showMessageDialog(this, "Las coordenadas deben ser números.");
                }
            }
        });

        btnEliminarNodo.addActionListener(e -> {
            String id = JOptionPane.showInputDialog(this, "ID del nodo a ser eliminado");
            if(id == null){
                return;
            }
            MapPoint eliminar = new MapPoint(id, 0, 0);
            grafoResultado.remove(eliminar);
            actualizarMapa();
        });
        //agregar UNA CONEXION
        btnAgregarConexion.addActionListener(e -> {
            String origenId = JOptionPane.showInputDialog(this, "Nodo origen");
            String destinoId = JOptionPane.showInputDialog(this, "Nodo destino");
            if(origenId == null || destinoId == null){
                return;
            }
            MapPoint puntoOrigenVacio = null;
            MapPoint puntoDestinoVacio = null;
            for(Node<MapPoint> nodo : grafoResultado.getNodes()){
                if(nodo.getValue().getId().equalsIgnoreCase(origenId)){
                    puntoOrigenVacio = nodo.getValue(); //ahora YA NO ES VACIO
                }
                if(nodo.getValue().getId().equalsIgnoreCase(destinoId)){
                    puntoDestinoVacio = nodo.getValue(); //tampoco es VACIO
                }
            }
            if(puntoOrigenVacio == null || puntoDestinoVacio == null){
                JOptionPane.showMessageDialog(this, "Nodo inexistente");
                return;
            }
            grafoResultado.addConection(puntoOrigenVacio, puntoDestinoVacio);
            actualizarMapa();
        });
        //boton ELIMINAR CONEXION
        btnEliminarConexion.addActionListener(e -> {
            String origenId = JOptionPane.showInputDialog(this, "Nodo origen");
            String destinoId = JOptionPane.showInputDialog(this, "Nodo destino");
            if(origenId == null || destinoId == null){
                return;
            }
            MapPoint puntoOrigenVacio = null;
            MapPoint puntoDestinoVacio = null;
            for(Node<MapPoint> nodo : grafoResultado.getNodes()){
                if(nodo.getValue().getId().equalsIgnoreCase(origenId)){
                    puntoOrigenVacio = nodo.getValue(); //ahora YA NO ES VACIO
                }
                if(nodo.getValue().getId().equalsIgnoreCase(destinoId)){
                    puntoDestinoVacio = nodo.getValue(); //tampoco es VACIO
                }
            }
            if(puntoOrigenVacio != null || puntoDestinoVacio != null){
                grafoResultado.removeConnection(puntoOrigenVacio, puntoDestinoVacio);
                actualizarMapa();
            }
        } );
        getContentPane().setBackground(Color.WHITE);

        //--------------------------------- OTRA PARTE ---------------------------------------------
        btnBfs.addActionListener(e -> {
            System.out.println("PRESIONE BFS");
            mapa.limpiarVisualizacion();
            mapa.setModoVisualizacion(TipoVisualizacion.EXPLORATION);
            MapPoint puntoInicio = obtenerNodo("N1");
            MapPoint puntoFinal = obtenerNodo("N33");
            if(puntoInicio == null || puntoFinal == null){
                JOptionPane.showMessageDialog(this, "No se encontró el nodo inicio o destino");
                return;
            }
            //PathResult<MapPoint> resultado = bfs.find(grafoResultado, puntoInicio, puntoFinal, mapa); //en lugar de esta linea en MainFrame
            PathResult<MapPoint> resultado = controladorMapa.buscarRuta(puntoInicio, puntoFinal, mapa, new BFSPathFinder<>());
            Timer esperar = new Timer(100, null);
            esperar.addActionListener(ev -> {
                if(mapa.exploracionTerminada()){
                    mapa.mostrarRuta(new ArrayList<>(resultado.getPath()));
                    esperar.stop();
                }
            });
            esperar.start();
        });
        
        btnDfs.addActionListener(e -> {
            mapa.limpiarVisualizacion();
            mapa.setModoVisualizacion(TipoVisualizacion.EXPLORATION);
            MapPoint puntoInicio = obtenerNodo("N1");
            MapPoint puntoFinal = obtenerNodo("N33");
            if(puntoInicio == null || puntoFinal == null){
                JOptionPane.showMessageDialog(this, "No se encontró el nodo inicio o destino");
                return;
            }
            PathResult<MapPoint> resultado = controladorMapa.buscarRuta(puntoInicio, puntoFinal, mapa, new DFSPathFinder<>());
            Timer esperar = new Timer(100, null);
             esperar.addActionListener(ev -> {
                if(mapa.exploracionTerminada()){
                    mapa.mostrarRuta(new ArrayList<>(resultado.getPath()));
                    esperar.stop();
                }
            });
            esperar.start();
        });

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        JPanel pnlLateral = new JPanel(new BorderLayout());
        pnlLateral.setPreferredSize(new Dimension(240, 700));
        pnlLateral.setBackground(COLOR_FONDO_MENU);
        JPanel pnlOpcionesPrincipales = new JPanel();
        pnlOpcionesPrincipales.setLayout(new BoxLayout(pnlOpcionesPrincipales, BoxLayout.Y_AXIS));
        pnlOpcionesPrincipales.setBackground(COLOR_FONDO_MENU);

        estilizarBoton(btnAgregarNodo);
        estilizarBoton(btnEliminarNodo);
        estilizarBoton(btnAgregarConexion);
        estilizarBoton(btnEliminarConexion);
        estilizarBoton(btnBfs);
        estilizarBoton(btnDfs);

        // Menu lateral

        // Agregar paneles principales a la ventana
        add(menuPanel, BorderLayout.WEST);
        add(contenedorMapa, BorderLayout.CENTER);
    }
    private void crearBarraMenu(){
        JMenuBar barra = new JMenuBar();
        JMenu archivo = new JMenu("Archivo");
        JMenuItem guardar = new JMenuItem("Guardar mapa");
        JMenuItem cargar = new JMenuItem("Cargar mapa");
        guardar.addActionListener(e -> {
        repositorio.guardarArchivo(grafoResultado, "src/resources/mapa.json");
            JOptionPane.showMessageDialog(this, "Mapa guardado correctamente");});
        cargar.addActionListener(e -> {
            grafoResultado = repositorio.cargarArchivo("src/resources/mapa.json");
            mapa.setGrafo(grafoResultado);
            actualizarMapa();
            JOptionPane.showMessageDialog(this, "Mapa cargado correctamente");});
        JMenu menuModoVisualizacion = new JMenu("Modo de visualizacion");
        JMenuItem itmExploracion = new JMenuItem("Exploracion");
        JMenuItem itmRutaFinal = new JMenuItem("Ruta final");

        itmExploracion.addActionListener(e -> {
            mapa.setModoVisualizacion(TipoVisualizacion.EXPLORATION);
            mapa.limpiarVisualizacion();
            JOptionPane.showMessageDialog(this, "Modo exploración seleccionado. Ejecutar BFS/DFS desde los botones", "Modo Visualizacion", JOptionPane.INFORMATION_MESSAGE);
        });
        itmRutaFinal.addActionListener(e -> {
            mapa.setModoVisualizacion(TipoVisualizacion.FINAL_PATH);
            mapa.limpiarVisualizacion();
            JOptionPane.showMessageDialog(this, "Modo rutaFinal seleccionada. Ejecutar BFS/DFS desde los botones", "Modo Visualizacion", JOptionPane.INFORMATION_MESSAGE);
        });
        menuModoVisualizacion.add(itmExploracion);
        menuModoVisualizacion.add(itmRutaFinal);
        barra.add(menuModoVisualizacion);
        setJMenuBar(barra);
            archivo.add(guardar);
            archivo.add(cargar);
            JMenu ayuda = new JMenu("Ayuda");
            JMenuItem acerca = new JMenuItem("Acerca de");
            acerca.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Proyecto");
            });
            ayuda.add(acerca);
            barra.add(archivo);
            barra.add(ayuda);
            setJMenuBar(barra);
    }
    private void actualizarMapa() {
        mapa.setGrafo(grafoResultado);
        mapa.repaint();
    }
    private void estilizarBoton(JButton boton){
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setForeground(Color.WHITE);
        boton.setForeground(new Color(52, 73, 94));
        boton.setFocusPainted(false);            
        boton.setBorderPainted(false);
    }
    private void agregarCategoria(JPanel panel, String titulo){

    JLabel lbl = new JLabel(titulo);
    lbl.setFont(FUENTE_SECCION);
    lbl.setForeground(new Color(149,165,166));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);


    panel.add(lbl);


    panel.add(Box.createVerticalStrut(10));

}

    private JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setBackground(COLOR_BOTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto Hover
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

    private MapPoint obtenerNodo(String id){
        for(Node<MapPoint> nodo : grafoResultado.getNodes()){
            if(nodo.getValue().getId().equalsIgnoreCase(id)){
                return nodo.getValue();
            }
        }
        return null;
    }

    private void mostrarMensaje(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Modo Edicion", JOptionPane.INFORMATION_MESSAGE);
    }
}