package app;
import java.awt.*;
import javax.swing.*;

import benchmark.GrafoBenchmark;
import controllers.MapController;
import listeners.PathListener;
import models.MapPoint;
import persistance.FileGraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import view.MainFrame;

public class App {
    public static void main(String[] args) {
        System.out.println("ESTOY EN APP");
               // Arranca la aplicación
        
        MapController controladorMapa = new MapController(new FileGraphRepository());
        Graph<MapPoint> grafoUsar = controladorMapa.getGrafo(); //llama al CONTROLADOR
        grafoUsar.printGraph();
        MapPoint inicio = new MapPoint("N1", 204, 19);
        MapPoint destino = new MapPoint("N33", 557, 274);
        GrafoBenchmark.compararRendimiento(grafoUsar, inicio, destino);
        SwingUtilities.invokeLater(() -> {
            MainFrame ventana = new MainFrame(controladorMapa);
            ventana.setVisible(true);
        });
}
}
