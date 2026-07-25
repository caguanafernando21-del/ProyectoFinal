import java.awt.*;
import javax.swing.*;

public class App {

    public static void main(String[] args) {

        // Ventana
        JFrame ventana = new JFrame("Proyecto");
        ventana.setSize(1000, 600);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(new BorderLayout());

        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(180, 600));
        menu.setBackground(new Color(35, 55, 85));
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));

        // Barra 
        JMenuBar barraMetodos = new JMenuBar();
        barraMetodos.setMaximumSize(new Dimension(140, 30));
        barraMetodos.setPreferredSize(new Dimension(140, 30));
        barraMetodos.setBackground(new Color(52, 152, 219));
        barraMetodos.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JMenu metodos = new JMenu("Métodos");

        JMenuItem bfs = new JMenuItem("BFS");
        JMenuItem dfs = new JMenuItem("DFS");

        metodos.add(bfs);
        metodos.add(dfs);

        barraMetodos.add(metodos);
        barraMetodos.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Barra Opciones
        JMenuBar barraOpciones = new JMenuBar();
        barraOpciones.setMaximumSize(new Dimension(140, 30));
        barraOpciones.setPreferredSize(new Dimension(140, 30));
        barraOpciones.setBackground(new Color(52, 152, 219));
        barraOpciones.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JMenu opciones = new JMenu("Opciones");

        JMenuItem temperatura = new JMenuItem("Temperatura");
        JMenuItem salir = new JMenuItem("Salir");

        opciones.add(temperatura);
        opciones.addSeparator();
        opciones.add(salir);

        barraOpciones.add(opciones);
        barraOpciones.setAlignmentX(Component.CENTER_ALIGNMENT);

        menu.add(Box.createVerticalStrut(30));
        menu.add(barraMetodos);
        menu.add(Box.createVerticalStrut(20));
        menu.add(barraOpciones);
        menu.add(Box.createVerticalGlue());

    
        JPanel mapa = new JPanel(new BorderLayout());
        mapa.setBackground(new Color(240, 240, 240));

        JLabel titulo = new JLabel("MAPA", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        mapa.add(titulo, BorderLayout.NORTH);

        // Cargar imagen
        java.net.URL ruta = App.class.getResource("/mapas.png");

        if (ruta != null) {

            ImageIcon icono = new ImageIcon(ruta);

            Image img = icono.getImage().getScaledInstance(
                    760,
                    500,
                    Image.SCALE_SMOOTH);

            JLabel imagen = new JLabel(new ImageIcon(img));
            imagen.setHorizontalAlignment(SwingConstants.CENTER);

            mapa.add(imagen, BorderLayout.CENTER);

        } else {

            JLabel error = new JLabel("No se encontró mapas.png");
            error.setHorizontalAlignment(SwingConstants.CENTER);
            error.setFont(new Font("Arial", Font.BOLD, 20));

            mapa.add(error, BorderLayout.CENTER);
        }

        ventana.add(menu, BorderLayout.WEST);
        ventana.add(mapa, BorderLayout.CENTER);

        salir.addActionListener(e -> System.exit(0));

        ventana.setVisible(true);
    }
}