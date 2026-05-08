package com.example.sitema;

public class MateriaCursada {
    private String nombreMateria;
    private double calificacion;

    public MateriaCursada() {}

    public MateriaCursada(String nombreMateria, double calificacion) {
        this.nombreMateria = nombreMateria;
        this.calificacion = calificacion;
    }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }

    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }
}
