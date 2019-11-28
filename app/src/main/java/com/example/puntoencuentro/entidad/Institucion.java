package com.example.puntoencuentro.entidad;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Institucion implements Serializable {

    private int id;
    private String nombre;
    private String cuit;
    private LatLng ubicacion;
    private List<Ruta> rutas;

    public Institucion(int id, String nombre, String cuit, LatLng ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.cuit = cuit;
        this.ubicacion = ubicacion;
        this.rutas = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCuit() {
        return cuit;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void agregarRuta (Ruta ruta){

        this.rutas.add(ruta);

    }

    public List<Ruta> getRutas() {
        return rutas;
    }
}
