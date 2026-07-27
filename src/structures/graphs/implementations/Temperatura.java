package structures.graphs.implementations;

public class Temperatura {
    private double valor;
    private String unidad;

    // Constructor completo
    public Temperatura(double valor, String unidad) {
        this.valor = valor;
        this.unidad = unidad;
    }

    // Constructor rápido (por defecto usa °C)
    public Temperatura(double valor) {
        this(valor, "°C");
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

    // Para mostrarlo bonito como Texto
    @Override
    public String toString() {
        return valor + " " + unidad;
    }
}