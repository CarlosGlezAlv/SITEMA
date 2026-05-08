package com.example.sitema;

public class Materia {
    private String claveMateria;
    private String nombreMateria;

    public Materia() {}

    public Materia(String claveMateria, String nombreMateria) {
        this.claveMateria = claveMateria;
        this.nombreMateria = nombreMateria;
    }

    public String getClaveMateria() { return claveMateria; }
    public void setClaveMateria(String claveMateria) { this.claveMateria = claveMateria; }

    public String getNombreMateria() { return nombreMateria; }
    public void setNombreMateria(String nombreMateria) { this.nombreMateria = nombreMateria; }
}
