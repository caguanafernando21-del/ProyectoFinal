package models;


public class VisualizationMode {


    // Controlar el estado del raton, resumen para poder hacer varias cosas al hacer click aun así
    // saber que debe de hacer el programa
    public enum ModoEdicion {
    NINGUNO, 
    AGREGAR_NODO, 
    ELIMINAR_NODO, 
    CONECTAR_NODOS, 
    ELIMINAR_CONEXION
    }
    public enum TipoVisualizacion {
        EXPLORATION, 
        FINAL_PATH

    }
}
