package structures.graphs.implementations;

public class Temperatura {
    private double valor;
    private String unidad;

    // Constructor principal
    public Temperatura(double valor, String unidad) {
        this.valor = valor;
        this.unidad = unidad;
    }

    // Constructor simplificado (asume grados Centígrados por defecto)
    public Temperatura(double valor) {
        this.valor = valor;
        this.unidad = "°C";
    }

    // Getters y Setters
    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    // Método útil para mostrar la temperatura en la interfaz gráfica
    @Override
    public String toString() {
        return valor + " " + unidad;
    }
}
