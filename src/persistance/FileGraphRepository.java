package persistance;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import models.MapPoint;
import models.MapaJson;
import models.NodoJson;
import structures.graphs.Graph;
import structures.node.Node;

public class FileGraphRepository implements GraphRepository{

    @Override 
    public Graph<MapPoint> cargarArchivo(String archivo){
        System.out.println("Cargando archivo:" + archivo);
        Graph<MapPoint> grafo = new Graph<>();
        try(FileReader lectorArchivos = new FileReader(archivo)){
            Gson gson = new Gson();
            MapaJson mapaJson = gson.fromJson(lectorArchivos, MapaJson.class);
            HashMap<String, MapPoint> puntos = new HashMap<>();
            for(NodoJson nodo : mapaJson.getNodos()){
                MapPoint punto = new MapPoint(nodo.getId(), nodo.getX(), nodo.getY());
                grafo.add(punto);
                puntos.put(nodo.getId(), punto);
            }
            for(NodoJson nodo : mapaJson.getNodos()){
                MapPoint puntoOrigen = puntos.get(nodo.getId());
                for(String vecino : nodo.getVecinos()){
                    MapPoint puntoDestino = puntos.get(vecino);
                    grafo.addConection(puntoOrigen, puntoDestino);
                }
            } 
            System.out.println("Nodos cargados: " + grafo.getNodes().size());
            grafo.printGraph();
            MapPoint n1 = puntos.get("N1");
            System.out.println("N1 cargado:" + n1);
            System.out.println("Vecinos N1:" + grafo.getVecinos(n1));

        } catch(Exception exc){
            exc.printStackTrace();
        }
        return grafo;
    }
    @Override
    public void guardarArchivo(Graph<MapPoint> grafo, String archivo){
        try(FileWriter escritor = new FileWriter(archivo)){
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<NodoJson> nodosJson = new ArrayList<>();
            for(Node<MapPoint> nodo : grafo.getNodes()){
                MapPoint punto = nodo.getValue();
                List<String> vecinos = new ArrayList<>();
                for(Node<MapPoint> vecino : grafo.getVecinos(punto)){
                    vecinos.add(vecino.getValue().getId());
                }
                NodoJson nodoJson = new NodoJson(punto.getId(), punto.getX(), punto.getY(), vecinos);
                nodosJson.add(nodoJson);
            }
            MapaJson mapaJson = new MapaJson();
            mapaJson.setNodos(nodosJson);
            gson.toJson(mapaJson, escritor);
        } catch(Exception exc1){
            exc1.printStackTrace();
        }
    } 
}

