package com.example.proyectocrudg3.model;

public class Categoria {

    private int id_categoria;
    private String nombreCategoria;
    private String descripcionCategoria;
    private boolean estadoCategoria;

    public Categoria() {
    }

    public Categoria(int id_categoria, String nombreCategoria, String descripcionCategoria,
                     boolean estadoCategoria) {

        this.id_categoria = id_categoria;
        this.nombreCategoria = nombreCategoria;
        this.descripcionCategoria = descripcionCategoria;
        this.estadoCategoria = estadoCategoria;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getDescripcionCategoria() {
        return descripcionCategoria;
    }

    public void setDescripcionCategoria(String descripcionCategoria) {
        this.descripcionCategoria = descripcionCategoria;
    }

    public boolean isEstadoCategoria() {
        return estadoCategoria;
    }

    public void setEstadoCategoria(boolean estadoCategoria) {
        this.estadoCategoria = estadoCategoria;
    }
}
