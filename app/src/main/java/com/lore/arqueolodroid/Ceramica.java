package com.lore.arqueolodroid;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by loree on 01/06/2018.
 */

public class Ceramica {//esta funcion es usada como contenedor para un elemento de la base de datos

  @SerializedName("idDato")
  @Expose
  private int id;

  @SerializedName("padre")
  @Expose
  private int idPadre;

  @SerializedName("datos")
  @Expose
  private String valor;


  public Ceramica(int idDato, int idPadre, String valor) {
    this.id = idDato;
    this.idPadre = idPadre;
    this.valor = valor;
  }

  public int getId() {
    return id;
  }


  public int getIdPadre() {
    return idPadre;
  }

  public String getValor() {
    return valor;
  }
}
