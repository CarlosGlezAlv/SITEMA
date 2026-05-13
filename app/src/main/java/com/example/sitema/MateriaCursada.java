package com.example.sitema;

public class MateriaCursada {
    private String claveMateria;
    private String nombreMateria;
    private double calificacion;

    public MateriaCursada() {}

    public MateriaCursada(String claveMateria, String nombreMateria, double calificacion) {
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
        this.calificacion = calificacion;
    }

    public String getClaveMateria() { return claveMateria; }
    public void setClaveMateria(String claveMateria) { this.claveMateria = claveMateria; }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }

    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }
}
