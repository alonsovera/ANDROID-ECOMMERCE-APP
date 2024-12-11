package com.example.ecopetsapp;

public class User {
    public String nombre;
    public String apellido;
    public String telefono;
    public String direccion;
    public String email;

    // Constructor

    public User(String nombre, String apellido, String telefono, String direccion, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    // Constructor vacío necesario para Firebase
    public User() {}
}
