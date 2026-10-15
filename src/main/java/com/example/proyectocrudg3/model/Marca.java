package com.example.proyectocrudg3.model;

public class Marca {
    private int id_marca;
    private String nombreMarca;
    private String paisOrigen;
    private boolean estadoMarca;

    public Marca() {
    }

    public Marca(int id_marca, String nombreMarca, String paisOrigen, boolean estadoMarca) {
        this.id_marca = id_marca;
        this.nombreMarca = nombreMarca;
        this.paisOrigen = paisOrigen;
        this.estadoMarca = estadoMarca;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public boolean isEstadoMarca() {
        return estadoMarca;
    }

    public void setEstadoMarca(boolean estadoMarca) {
        this.estadoMarca = estadoMarca;
    }
}
