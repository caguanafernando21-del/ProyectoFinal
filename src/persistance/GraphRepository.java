package persistance;

import models.MapPoint;
import structures.graphs.Graph;

public interface GraphRepository {
    Graph<MapPoint> cargarArchivo(String archivo);
}
