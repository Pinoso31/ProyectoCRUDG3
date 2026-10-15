package com.example.proyectocrudg3.model;

import java.util.Date;

public class Producto {
    private int id_producto;
    private String codigoProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioProducto;

    private String categoria;
    private String marca;

    private int stockProducto;
    private Date Fecha_ingresoProducto;
    private int id_marca;
    private int id_categoria;
    private String nombreCategoria;
    private String nombreMarca;
    private boolean estadoProducto;

    public Producto() {
    }

    public Producto(int id_producto, String codigoProducto, String nombreProducto, String descripcionProducto,
                    String categoria, String marca,
                    double precioProducto, int stockProducto, Date fecha_ingresoProducto,
                    int id_marca, int id_categoria, String nombreCategoria, String nombreMarca,
                    boolean estadoProducto) {
        this.id_producto = id_producto;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.descripcionProducto = descripcionProducto;
        this.categoria = categoria;
        this.marca = marca;
        this.precioProducto = precioProducto;
        this.stockProducto = stockProducto;
        Fecha_ingresoProducto = fecha_ingresoProducto;
        this.id_marca = id_marca;
        this.id_categoria = id_categoria;
        this.nombreCategoria = nombreCategoria;
        this.nombreMarca = nombreMarca;
        this.estadoProducto = estadoProducto;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(double precioProducto) {
        this.precioProducto = precioProducto;
    }

    public int getStockProducto() {
        return stockProducto;
    }

    public void setStockProducto(int stockProducto) {
        this.stockProducto = stockProducto;
    }

    public Date getFecha_ingresoProducto() {
        return Fecha_ingresoProducto;
    }

    public void setFecha_ingresoProducto(Date fecha_ingresoProducto) {
        Fecha_ingresoProducto = fecha_ingresoProducto;
    }

    public int getId_marca() {
        return id_marca;
    }

    public void setId_marca(int id_marca) {
        this.id_marca = id_marca;
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

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public boolean isEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(boolean estadoProducto) {
        this.estadoProducto = estadoProducto;
    }
}


