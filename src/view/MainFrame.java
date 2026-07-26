package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import models.MapPoint;
import persistance.FileGraphRepository;

import java.awt.color.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class MainFrame extends JFrame {
    public MainFrame(){
        System.out.println("CREANDO MAINFRAME");
        setTitle("Interfaz DFS/BFS");
        //setSize(900, 600);
        //el modelo original ocupa 4 PANELES 
        JPanel pnlPrincipal = new JPanel(new BorderLayout(15, 15)); //panel derecho
        JPanel pnlLateral = new JPanel(new BorderLayout()); //panel izquierdo

        pnlLateral.setPreferredSize(new Dimension(200, 0));

        JButton btnSuperior  = new JButton("Inicio");
        JButton btnInferior = new JButton("Salir");
        FileGraphRepository repositorio = new FileGraphRepository();
        repositorio.cargarArchivo("src/resources/mapa.json");



        //clase CREADA
        MapPanel mapa = new MapPanel();
        mapa.setPreferredSize(new Dimension(670, 520));
        mapa.setBackground(Color.DARK_GRAY);

        List<MapPoint> listaNodos = new ArrayList<>();
        listaNodos.add(new MapPoint("N1", 207, 22));
        listaNodos.add(new MapPoint("N2", 500, 300));
        listaNodos.add(new MapPoint("N3", 700, 400));
        mapa.setNodos(listaNodos); //se usa la lista CREADA


        pnlLateral.add(btnSuperior, BorderLayout.NORTH);
        pnlLateral.add(btnInferior, BorderLayout.SOUTH);
        pnlPrincipal.add(pnlLateral, BorderLayout.WEST);
        pnlPrincipal.add(mapa, BorderLayout.CENTER);

        add(pnlPrincipal);
        pack();
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
