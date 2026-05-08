package com.example.sitema;

public class Alumno {
    private String numControl;
    private String nombre;
    private String telefono;

    public Alumno() {}

    public Alumno(String numControl, String nombre, String telefono) {
        this.numControl = numControl;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNumControl() { return numControl; }
    public void setNumControl(String numControl) { this.numControl = numControl; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
