package com.example.puntoencuentro.entidad;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.List;

public class Ruta {

  private Date horaPartida;
  private LatLng puntoInicio;
  private LatLng puntoFin;
  private List<Usuario> usuarios;

  public Ruta(LatLng puntoInicio, LatLng puntoFin, List<Usuario> usuarios, Date horaPartida) {

    this.puntoInicio = puntoInicio;
    this.puntoFin = puntoFin;
    this.usuarios= usuarios;
    this.horaPartida=horaPartida;

  }

  public List<Usuario> getUsuarios() {
    return usuarios;
  }

  public Date getHoraPartida() {
    return horaPartida;
  }

  public void agregarUsuario(Usuario usuario){

    this.usuarios.add(usuario);

  }

  public LatLng getPuntoFin() {
    return puntoFin;
  }

  public LatLng getPuntoInicio() {
    return puntoInicio;
  }
}
