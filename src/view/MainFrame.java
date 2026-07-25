package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.awt.color.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    public MainFrame(){
        setTitle("Interfaz DFS/BFS");
        setSize(900, 600);
        //el modelo original ocupa 4 PANELES 
        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15)); //panel derecho
        JPanel pnlLateral = new JPanel(new BorderLayout()); //panel izquierdo

        pnlLateral.setPreferredSize(new Dimension(200, 0));

        JButton btnSuperior  = new JButton("Inicio");
        JButton btnInferior = new JButton("Salir");
        //clase CREADA
        MapPanel mapa = new MapPanel();
        mapa.setBackground(Color.DARK_GRAY);

        pnlLateral.add(btnSuperior, BorderLayout.NORTH);
        pnlLateral.add(btnInferior, BorderLayout.SOUTH);
        pnlPrincipal.add(pnlLateral, BorderLayout.WEST);
        pnlPrincipal.add(mapa, BorderLayout.CENTER);

        add(pnlPrincipal);
        btnInferior.addActionListener(e -> {
            System.exit(0);
        });
        JMenuItem bfs = new JMenuItem("BFS");
        JMenuItem dfs = new JMenuItem("DFS");

        JMenu menuMetodos = new JMenu("Métodos");
        menuMetodos.add(bfs);
        menuMetodos.add(dfs);

        JMenuBar barraMenu = new JMenuBar();
        barraMenu.add(menuMetodos);
        setJMenuBar(barraMenu);

        JMenu opcionesAdicionales = new JMenu("Opciones");
        JMenuItem itmTemperatura = new JMenuItem("Temperatura");
        JMenuItem itmSalir = new JMenuItem("Salir");
        opcionesAdicionales.add(itmTemperatura);
        opcionesAdicionales.addSeparator();
        opcionesAdicionales.add(itmSalir);
    }
}
