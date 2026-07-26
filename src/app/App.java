package app;
import java.awt.*;
import javax.swing.*;

import listeners.PathListener;
import models.MapPoint;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import view.MainFrame;

public class App {
    public static void main(String[] args) {
        System.out.println("ESTOY EN APP");
        MainFrame ventana = new MainFrame();
        ventana.setVisible(true);

        //PRUEBAS DFS, BFS
        PathListener<MapPoint> listener = punto -> {
            System.out.println("Nodo siendo visitado: " + punto);
        };

        Graph<MapPoint> mapGraph = new Graph<>();
        MapPoint puntoA = new MapPoint("NA", 100, 200);
        MapPoint puntoB = new MapPoint("NB", 300, 200);
        MapPoint puntoC = new MapPoint("NC", 300, 400);
        MapPoint puntoD = new MapPoint("Nd", 500, 400);

        mapGraph.add(puntoA);
        mapGraph.add(puntoB);
        mapGraph.add(puntoC);
        mapGraph.add(puntoD);
        mapGraph.addConection(puntoA, puntoB);
        mapGraph.addConection(puntoA, puntoC);
        mapGraph.addConection(puntoB, puntoD);
        mapGraph.addConection(puntoC, puntoD);

        PathListener<MapPoint> listenerDos = nodo -> {
            System.out.println("Visitando: " + nodo.getId());
        };

        PathFinder<MapPoint> bfs = new BFSPathFinder<>();

        PathResult<MapPoint> resultado = bfs.find(mapGraph, puntoA, puntoD, listener);


        System.out.println("---- VISITADOS ----");
        System.out.println(resultado.getVisitados());

        System.out.println("---- RUTA ----");
        System.out.println(resultado.getPath());
    }
}