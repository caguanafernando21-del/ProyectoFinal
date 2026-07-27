// Ubicacion: src/view/MainFrame.java
package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.VisualizationMode.ModoEdicion; 

public class MainFrame extends JFrame {

    // Paleta de colores moderna
    private static final Color COLOR_FONDO_MENU = new Color(44, 62, 80);      // Azul oscuro grisaceo
    private static final Color COLOR_BOTON = new Color(52, 73, 94);             // Fondo de boton normal
    private static final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);   // Azul brillante al pasar el raton
    private static final Color COLOR_TEXTO = new Color(236, 240, 241);        // Blanco humo

    // Fuentes
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FUENTE_SECCION = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.PLAIN, 14);

    public MainFrame() {
        // Configuracion de la ventana principal
        setTitle("Google Maps");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Menu lateral
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(240, 700));
        menu.setBackground(COLOR_FONDO_MENU);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 15, 20, 15)); 

        menu.add(Box.createVerticalStrut(30));

        // Se instancia el panel interactivo del mapa para poder pasarlo a las acciones
        MapPanel panelMapa = new MapPanel();

        // Categoria: Metodos de busqueda
        agregarCategoria(menu, "METODOS DE BUSQUEDA");
        JButton btnBfs = crearBotonMenu("Busqueda a profundidad");
        JButton btnDfs = crearBotonMenu("Busqueda por niveles");
        menu.add(btnBfs);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnDfs);
        menu.add(Box.createVerticalStrut(20));

        // Categoria: Edicion de nodos
        agregarCategoria(menu, "NODOS");
        JButton btnAgregar = crearBotonMenu("Agregar Nodo");
        JButton btnEliminar = crearBotonMenu("Eliminar Nodo");
        JButton btnConectar = crearBotonMenu("Conectar Nodos");
        JButton btnEliminarConexion = crearBotonMenu("Eliminar Conexion");
        
        menu.add(btnAgregar);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnEliminar);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnConectar);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnEliminarConexion);
        menu.add(Box.createVerticalStrut(20));

        // Categoria: Sistema / Extras
        agregarCategoria(menu, "Extras");
        JButton btnTemperatura = crearBotonMenu("Temperatura");
        JButton btnSalir = crearBotonMenu("Salir");
        
        btnSalir.setForeground(new Color(231, 76, 60)); // Resalta el boton salir en rojo

        menu.add(btnTemperatura);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnSalir);
        menu.add(Box.createVerticalGlue()); // Empuja todo hacia arriba

        // Contenedor principal del mapa
        JPanel contenedorMapa = new JPanel(new BorderLayout());
        contenedorMapa.setBackground(new Color(245, 246, 250)); 
        contenedorMapa.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Cabecera del mapa
        JPanel headerMapa = new JPanel(new BorderLayout());
        headerMapa.setOpaque(false);
        headerMapa.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel tituloMapa = new JLabel("Mapa de Italia");
        tituloMapa.setFont(FUENTE_TITULO);
        tituloMapa.setForeground(new Color(44, 62, 80));
        headerMapa.add(tituloMapa, BorderLayout.WEST);

        contenedorMapa.add(headerMapa, BorderLayout.NORTH);

        // Decoracion del panel del mapa
        panelMapa.setBackground(Color.WHITE);
        panelMapa.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 2));
        contenedorMapa.add(panelMapa, BorderLayout.CENTER);

        // EVENTOS DE LOS BOTONES
        btnAgregar.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.AGREGAR_NODO);
            mostrarMensaje(this, "Modo Agregar Nodo activado.\nHaga clic en el mapa.");
        });

        btnEliminar.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.ELIMINAR_NODO);
            mostrarMensaje(this, "Modo Eliminar Nodo activado.\nSeleccione el nodo a eliminar.");
        });

        btnConectar.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.CONECTAR_NODOS);
            mostrarMensaje(this, "Modo Conectar Nodos activado.\nSeleccione dos nodos.");
        });

        btnEliminarConexion.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.ELIMINAR_CONEXION);
            mostrarMensaje(this, "Modo Eliminar Conexion activado.\nSeleccione los dos nodos.");
        });

        btnTemperatura.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Selecciones temperatura", "Temperatura", JOptionPane.INFORMATION_MESSAGE);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        // Agregar paneles principales a la ventana
        add(menu, BorderLayout.WEST);
        add(contenedorMapa, BorderLayout.CENTER);
    }

    private void agregarCategoria(JPanel panel, String titulo) {
        JLabel lblCategoria = new JLabel(titulo);
        lblCategoria.setFont(FUENTE_SECCION);
        lblCategoria.setForeground(new Color(149, 165, 166)); 
        lblCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblCategoria);
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

    private void mostrarMensaje(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, "Modo Edicion", JOptionPane.INFORMATION_MESSAGE);
    }
}