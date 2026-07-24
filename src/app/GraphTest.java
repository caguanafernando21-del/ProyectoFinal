package app;


import models.MapPoint;
import structures.graphs.Graph;
import structures.graphs.PathResult;
import structures.graphs.implementations.DFSPathFinder;
import structures.graphs.implementations.BFSPathFinder;

public class GraphTest {
    public static void main(String[] args) {
        //OBJETIVO --> verificar que Graph acepta los nodos y los DFS/BFS SI USAN MapPoint
        Graph<MapPoint> mapaPrueba = new Graph<>(); //instancia
        MapPoint nodoA = new MapPoint("NA", 100, 100);
        MapPoint nodoB = new MapPoint("NB", 200, 100);
        MapPoint nodoC = new MapPoint("NC", 300, 100);
        mapaPrueba.addConection(nodoA, nodoB);
        mapaPrueba.addConection(nodoB, nodoC);
        
        //verificar COMO SALE
        mapaPrueba.printGraph();
        BFSPathFinder<MapPoint> bfSearch = new BFSPathFinder<>();
        PathResult<MapPoint> resultadoBfSearch = bfSearch.find(mapaPrueba, nodoA, nodoC);
        System.out.println(resultadoBfSearch); //imprime
        //imprime visitados=[MapPoint [id=NA, x=100, y=100], MapPoint [id=NB, x=200, y=100], MapPoint [id=NC, x=300, y=100]] SI USA
        
        //probar con DFS
        System.out.println("=== PRUEBA CoN DFS ===");
        DFSPathFinder<MapPoint> dfSearch = new DFSPathFinder<>();
        PathResult<MapPoint> resultadoDfSearch = dfSearch.find(mapaPrueba, nodoA, nodoC);
        System.out.println(resultadoDfSearch); //imprime DFS
    }
}
