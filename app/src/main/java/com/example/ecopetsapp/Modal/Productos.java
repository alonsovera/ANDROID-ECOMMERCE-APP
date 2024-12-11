package com.example.ecopetsapp.Modal;

public class Productos {
    private String pid, cantidad, descripcion, imagen, nombre, precio;

    public Productos(){}

    public Productos(String pid, String descripcion, String imagen, String cantidad, String nombre, String precio) {
        this.pid = pid;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.cantidad = cantidad;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }
}
