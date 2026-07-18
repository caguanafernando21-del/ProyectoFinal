import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class Ventana extends JFrame {
    public Ventana(){
        setTitle("Interfaz DFS/BFS");
        setSize(900, 600);

        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15));
        JPanel pnlLateral = new JPanel(new BorderLayout());

        pnlLateral.setPreferredSize(new Dimension(200, 0));

        JButton btnSuperior  = new JButton("Inicio");
        JButton btnInferior = new JButton("Salir");
        JPanel contenido = new JPanel();
        contenido.setBackground(Color.DARK_GRAY);

        pnlPrincipal.add(pnlLateral, BorderLayout.WEST);
        pnlPrincipal.add(contenido, BorderLayout.CENTER);

        add(pnlPrincipal);

        setVisible(true);

    }
}