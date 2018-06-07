package com.lore.arqueolodroid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Hugo on 06/06/18.
 */

public class Ubicacion {
//(nombre text, lat real, lng real)

  @SerializedName("nombre")
  @Expose
  private String nombre;

  @SerializedName("lat")
  @Expose
  private double latitud;

  @SerializedName("long")
  @Expose
  private double longitud;

  public Ubicacion(String nombre, double latitud, double longitud) {
    this.nombre = nombre;
    this.latitud = latitud;
    this.longitud = longitud;
  }

  public String getText() {
    return nombre;
  }

  public double getLatitud() {
    return latitud;
  }

  public double getLongitud() {
    return longitud;
  }
}
