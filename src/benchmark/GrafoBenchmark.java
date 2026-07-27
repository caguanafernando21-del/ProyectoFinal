package benchmark;

import models.MapPoint;
import structures.graphs.Graph;
import structures.graphs.PathFinder;
import structures.graphs.PathResult;
import structures.graphs.implementations.BFSPathFinder;
import structures.graphs.implementations.DFSPathFinder;

public class GrafoBenchmark { //comparar TANTO TIEMPOS como CANTIDADES
    public static void compararRendimiento(Graph<MapPoint> grafo, MapPoint inicio, MapPoint destino){
        PathFinder<MapPoint> breadthFirstSearch = new BFSPathFinder<>();
        PathFinder<MapPoint> depthFirstSearch = new DFSPathFinder<>();
        //algoritmo BFS rendimineto
        long inicioDelBfs = System.nanoTime();
        PathResult<MapPoint> resultadoDelBfs = breadthFirstSearch.find(grafo, inicio, destino, null);
        long finalDelBfs = System.nanoTime();

        //algoritmo DFS rendimiento
        long inicioDelDfs = System.nanoTime();
        PathResult<MapPoint> resultadoDelDfs = depthFirstSearch.find(grafo, inicio, destino, null);
        long finalDelDfs = System.nanoTime();
        //benchmarking
        System.out.println("===== Algoritmo BFS =====");
        System.out.println("Nodos visitados:" + resultadoDelBfs.getVisitados().size());
        System.out.println("Longitud de la ruta:" + resultadoDelBfs.getPath().size());
        System.out.println("Orden de exploración:" + resultadoDelBfs.getVisitados());
        System.out.println("Timpo empleado: "+ (finalDelBfs - inicioDelBfs) + "ns");

        System.out.println("===== Algoritmo DFS =====");
        System.out.println("Nodos visitados:" + resultadoDelDfs.getVisitados().size());
        System.out.println("Longitud de la ruta:" + resultadoDelDfs.getPath().size());
        System.out.println("Orden de exploración:" + resultadoDelDfs.getVisitados());
        System.out.println("Timpo empleado: "+ (finalDelDfs - inicioDelDfs) + "ns");

        System.out.println("Diferencias entre algoritmos");
        //nueva FUNCION que retorna un VALOR ABSOLUTO (POSITIVO) en los resultados
        System.out.println("En nodos visitados: " + Math.abs(resultadoDelBfs.getVisitados().size() - resultadoDelDfs.getVisitados().size()));
        System.out.println("En longitud de ruta: " + Math.abs(resultadoDelBfs.getPath().size() - resultadoDelDfs.getPath().size())); 


    }    
}
