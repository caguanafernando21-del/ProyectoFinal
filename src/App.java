import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import models.VisualizationMode.ModoEdicion; 
import view.MapPanel;                     

public class App {

    // Paleta de colores moderna
    private static final Color COLOR_FONDO_MENU = new Color(44, 62, 80);      // Azul oscuro grisáceo
    private static final Color COLOR_BOTON = new Color(52, 73, 94);           // Fondo de botón normal
    private static final Color COLOR_BOTON_HOVER = new Color(41, 128, 185);   // Azul brillante al pasar el ratón
    private static final Color COLOR_TEXTO = new Color(236, 240, 241);        // Blanco humo

    // Fuentes
    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FUENTE_SECCION = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font FUENTE_BOTON = new Font("Segoe UI", Font.PLAIN, 14);

    public static void main(String[] args) {
        // Activar el diseño nativo del sistema operativo para ventanas y diálogos
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> crearYMostrarGUI());
    }

    private static void crearYMostrarGUI() {
        JFrame ventana = new JFrame("Google Maps");
        ventana.setSize(1100, 700);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());
        ventana.getContentPane().setBackground(Color.WHITE);

 
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(240, 700));
        menu.setBackground(COLOR_FONDO_MENU);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 15, 20, 15)); 

        // Título del Menu
      
        menu.add(Box.createVerticalStrut(30));

        // Instanciamos el panel interactivo del mapa para poder pasarlo a las acciones
        MapPanel panelMapa = new MapPanel();

        // Categoria: Métodos
        agregarCategoria(menu, "METODOS DE BUSQUEDA");
        JButton btnBfs = crearBotonMenu("Busqueda a profundidad");
        JButton btnDfs = crearBotonMenu("Busqueda por niveles");
        menu.add(btnBfs);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnDfs);
        menu.add(Box.createVerticalStrut(20));

        // Categoría: Edición
        agregarCategoria(menu, "NODOS");
        JButton btnAgregar = crearBotonMenu("Agregar Nodo");
        JButton btnEliminar = crearBotonMenu("Eliminar Nodo");
        JButton btnConectar = crearBotonMenu("Conectar Nodos");
        JButton btnEliminarConexion = crearBotonMenu("Eliminar Conexión");
        
        menu.add(btnAgregar);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnEliminar);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnConectar);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnEliminarConexion);
        menu.add(Box.createVerticalStrut(20));

        // Categoría: Sistema
        agregarCategoria(menu, "Extras");
        JButton btnTemperatura = crearBotonMenu("Temperatura");
        JButton btnSalir = crearBotonMenu("Salir");
        
        // Darle un color al boton salir para que resalte
        btnSalir.setForeground(new Color(231, 76, 60)); 

        menu.add(btnTemperatura);
        menu.add(Box.createVerticalStrut(5));
        menu.add(btnSalir);
        menu.add(Box.createVerticalGlue()); // Empuja todo hacia arriba

        JPanel contenedorMapa = new JPanel(new BorderLayout());
        contenedorMapa.setBackground(new Color(245, 246, 250)); // Fondo gris 
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

        // Decoración del panel del mapa (sombra ligera / borde)
        panelMapa.setBackground(Color.WHITE);
        panelMapa.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230), 2));
        contenedorMapa.add(panelMapa, BorderLayout.CENTER);

        // 3. EVENTOS 
        btnAgregar.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.AGREGAR_NODO);
            mostrarMensaje(ventana, "Modo Agregar Nodo activado.\nHaga clic en el mapa.");
        });

        btnEliminar.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.ELIMINAR_NODO);
            mostrarMensaje(ventana, "Modo Eliminar Nodo activado.\nSeleccione el nodo a eliminar.");
        });

        btnConectar.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.CONECTAR_NODOS);
            mostrarMensaje(ventana, "Modo Conectar Nodos activado.\nSeleccione dos nodos.");
        });

        btnEliminarConexion.addActionListener(e -> {
            panelMapa.setModoActual(ModoEdicion.ELIMINAR_CONEXION);
            mostrarMensaje(ventana, "Modo Eliminar Conexión activado.\nSeleccione los dos nodos.");
        });

        btnTemperatura.addActionListener(e -> {
            JOptionPane.showMessageDialog(ventana, "Selecciones temperatura", "Temperatura", JOptionPane.INFORMATION_MESSAGE);
        });

        btnSalir.addActionListener(e -> System.exit(0));

        // Agregar paneles principales a la ventana
        ventana.add(menu, BorderLayout.WEST);
        ventana.add(contenedorMapa, BorderLayout.CENTER);
        ventana.setVisible(true);
    }


    private static void agregarCategoria(JPanel panel, String titulo) {
        JLabel lblCategoria = new JLabel(titulo);
        lblCategoria.setFont(FUENTE_SECCION);
        lblCategoria.setForeground(new Color(149, 165, 166)); 
        lblCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblCategoria);
        panel.add(Box.createVerticalStrut(10));
    }

    private static JButton crearBotonMenu(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setForeground(COLOR_TEXTO);
        boton.setBackground(COLOR_BOTON);
        boton.setFocusPainted(false); // Quita el recuadro feo al hacer clic
        boton.setBorderPainted(false);
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setMaximumSize(new Dimension(200, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efecto Hover (cambio de color al pasar el ratón)
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

    //Metodo auxiliar para mostrar una ventana emergenteal usuario
    // de forma rapida y limpia evitando repetir el codigo de JOptionPane

    private static void mostrarMensaje(Component padre, String mensaje) {
        
        JOptionPane.showMessageDialog(padre, mensaje, "Modo Edición", JOptionPane.INFORMATION_MESSAGE);
    }
}