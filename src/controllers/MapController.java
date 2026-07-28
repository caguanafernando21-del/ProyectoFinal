package controllers;

import listeners.PathListener;
import models.MapPoint;
import persistance.GraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import structures.node.Node;
import structures.graphs.implementations.Temperatura;

public class MapController {
    private Graph<MapPoint> grafoAUsar;
    private GraphRepository repositorioGrafo;

    public MapController(GraphRepository repositorioGrafo){
        this.repositorioGrafo = repositorioGrafo;
        this.grafoAUsar = repositorioGrafo.cargarArchivo("src/resources/mapa.json");
        if (this.grafoAUsar != null && this.grafoAUsar.getNodes() != null) {
            for (Node<MapPoint> nodo : this.grafoAUsar.getNodes()) {
                // Genera valores de prueba entre 18.0 °C y 25.0 °C con 1 decimal
                double tempVal = 18.0 + (Math.random() * 7.0);
                tempVal = Math.round(tempVal * 10.0) / 10.0;
            
                nodo.setTemperatura(new Temperatura(tempVal));
            }
        }
    }
    public Graph<MapPoint> getGrafo(){
        return grafoAUsar;
    }

    public PathResult<MapPoint> buscarRuta(MapPoint puntoInicio, MapPoint puntoFinal, PathListener<MapPoint> listener, PathFinder<MapPoint> algoritmo){
        System.out.println("Buscando desde: " + puntoInicio);
        System.out.println("Hasta: " + puntoFinal);
   
        System.out.println("Nodos en controlador: " + grafoAUsar.getNodes().size());
        return algoritmo.find(grafoAUsar, puntoInicio, puntoFinal, listener);
    }
    
}
