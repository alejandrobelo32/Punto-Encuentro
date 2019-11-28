package com.example.puntoencuentro.entidad;

import java.io.Serializable;

public class Usuario implements Serializable {

    private int id;
    private String mail;
    private long dni;
    private String nombre;
    private String apellido;
    private String contrasena;
    private boolean habilidato;

    public Usuario(int id, String mail, String nombre, String apellido, String contrasena, boolean habilidato) {
        this.id=id;
        this.mail = mail;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contrasena = contrasena;
        this.habilidato = habilidato;
    }

    public Usuario(int id, String mail, long dni, String nombre, String apellido,String contrasena , boolean habilidato) {
        this.id = id;
        this.mail = mail;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.habilidato = habilidato;
        this.contrasena = contrasena;
    }

    public Usuario() {
    }

    public String getMail() {
        return mail;
    }

    public boolean isHabilidato() {
        return habilidato;
    }

    public int getId() {
        return id;
    }
}
