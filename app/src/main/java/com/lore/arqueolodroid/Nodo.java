package com.lore.arqueolodroid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loree on 01/06/2018.
 */

public class Nodo {//esta clase es una estructura de datos que permite crear el arbol mediante la especificacion de hijos en cada nodo
  private Ceramica valor;
  private List<Nodo> hijos;

  public Nodo(Ceramica valor) {
    this.valor = valor;
    hijos = new ArrayList<>();
  }

  public Ceramica getValor() {
    return valor;
  }

  public void setValor(Ceramica valor) {
    this.valor = valor;
  }

  public List<Nodo> getHijos() {
    return hijos;
  }

  public void setHijos(List<Nodo> hijos) {
    this.hijos = hijos;
  }

  public Nodo getHijo(int indice) {
    return hijos.get(indice);
  }

  public void addHijo(Nodo hijo) {
    hijos.add(hijo);
  }

  @Override
  public String toString() {
    return valor.getValor() + (hijos.size() > 0 ? ":" + hijos : "");
  }
}
