package com.example.sitema;

public class Docente {
    private String numEmpleado;
    private String nombre;
    private String direccion;

    public Docente() {}

    public Docente(String numEmpleado, String nombre, String direccion) {
        this.numEmpleado = numEmpleado;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public String getNumEmpleado() { return numEmpleado; }
    public void setNumEmpleado(String numEmpleado) { this.numEmpleado = numEmpleado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}
