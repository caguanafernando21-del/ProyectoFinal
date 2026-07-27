package controllers;

import listeners.PathListener;
import models.MapPoint;
import persistance.GraphRepository;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;

public class MapController {
    private Graph<MapPoint> grafoAUsar;
    private GraphRepository repositorioGrafo;

    public MapController(GraphRepository repositorioGrafo){
        this.repositorioGrafo = repositorioGrafo;
        this.grafoAUsar = repositorioGrafo.cargarArchivo("src/resources/mapa.json");
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
