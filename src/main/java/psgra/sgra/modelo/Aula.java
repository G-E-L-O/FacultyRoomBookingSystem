package psgra.sgra.modelo;

public class Aula {
    private String codigo;
    private int capacidad;

    public Aula(String codigo, int capacidad) {
        this.codigo = codigo;
        this.capacidad = capacidad;
    }

    public String getCodigo() { return codigo; }
    public int getCapacidad() { return capacidad; }

    @Override
    public String toString() {
        return codigo + " (" + capacidad + " personas)";
    }
}