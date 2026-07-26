package persistance;

import java.io.FileReader;

import com.google.gson.Gson;

import models.MapPoint;
import models.MapaJson;
import structures.graphs.Graph;

public class FileGraphRepository implements GraphRepository{

    @Override 
    public Graph<MapPoint> cargarArchivo(String archivo){
        Graph<MapPoint> grafo = new Graph<>();
        try(FileReader lectorArchivos = new FileReader(archivo)){
            Gson gson = new Gson();
            MapaJson mapaJson = gson.fromJson(lectorArchivos, MapaJson.class);
            System.out.println("MapaJson:" + mapaJson);
            System.out.println("Nodos: " + mapaJson.getNodos());
        } catch(Exception exc){
            exc.printStackTrace();
        }
        return grafo;
     }
}
