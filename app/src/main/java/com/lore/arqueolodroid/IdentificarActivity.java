package com.lore.arqueolodroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IdentificarActivity extends AppCompatActivity implements
    RadioGroup.OnCheckedChangeListener {

  private RadioGroup rgPreguntas;

  private Nodo raiz;
  private Nodo preguntaActual;

  private final static String TAG = "TAG";
  List<Ceramica> ceramicas;

  private int hijoActual;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_identificar);
    rgPreguntas = findViewById(R.id.rg_preguntas);
    rgPreguntas.setOnCheckedChangeListener(this);

//    obtiene los datos de la base de datos
    getCeramicas();
  }

  @Override
  public void onCheckedChanged(RadioGroup radioGroup, int id) {
    //se crea un radiobutton con el id del radiobutton presionado
    RadioButton radioButton = findViewById(id);
    //se obtiene el texto del radiogrupo
    String radioText = radioButton.getText().toString();

    //se busca el indice del hijo seleccionado en el radiogroup
    int index = getHijoSeleccionado(radioText);
    //se cambia la pregunta actual de acuerdo al radiobutton seleccionado
    preguntaActual = preguntaActual.getHijo(index);
    //se reconstruye el radiogroup con las nuevas preguntas
    recorrerHorizontal();
    hijoActual = 0;
    revisarFinDeArbol();
  }


  private int getHijoSeleccionado(String texto) {
    int index = 0;
//    se recorre el la lista de preguntas hasta que los valores coincidan
    while (index < preguntaActual.getHijos().size()) {
      if (preguntaActual.getHijo(index).getValor().getValor().equals(texto)) {
        return index;
      }
      index++;
    }
    return index;
  }

  private void recorrerHorizontal() {
    rgPreguntas.removeAllViews();
    //avanzamos en horizontal en el arbol
    int index = 0;
    while (index < preguntaActual.getHijos().size()) {
      RadioButton radioButton = new RadioButton(this);
      radioButton.setText(preguntaActual.getHijo(index).getValor().getValor());
      rgPreguntas.addView(radioButton);
      index++;
    }
  }

  private void getCeramicas() {
    Subscription subscription = Cliente.getInstance()
        .getCeramicas()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<List<Ceramica>>() {
          @Override
          public void onCompleted() {
            //una vez que se obtienen las ceramicas:
            int i;
            for (i = 0; i < ceramicas.size(); i++) {
              if (ceramicas.get(i).getIdPadre() == -1) {
                break;
              }
            }
            //aqui buscamos el primer elemento de la base de datos el cual es el que tiene el padre con -1
            raiz = new Nodo(ceramicas.get(i));//creamos el nodo raiz atravez de ese primer elemento
            //llamamos a la funcion llenar para crear el arbol de decision
            raiz = llenar(ceramicas, raiz);
            Log.e(TAG,
                ceramicas.get(i).getValor() + ": idpadre = " + ceramicas.get(i).getIdPadre() + " id"
                    + ceramicas.get(i).getId());
            preguntaActual = raiz;//ponemos el primer elemento
            hijoActual = 0;//y el hijo a cero
            revisarFinDeArbol();//y entonces preguntamos al usuario
            recorrerHorizontal();
          }

          @Override
          public void onError(Throwable e) {
          }

          @Override
          public void onNext(List<Ceramica> _ceramicas) {
            //las ceramicas de la base de datos se le asignan a la lista local
            ceramicas = _ceramicas;
          }
        });
  }

  //con esta funcion recursiva llenamos el arbol
  private Nodo llenar(List<Ceramica> ceramicas, Nodo nodo) {
    List<Nodo> hijos = getHijos(ceramicas, nodo);//obtenermos los hijos del nodo actual
    for (Nodo hijo : hijos) {
      llenar(ceramicas, hijo);//llamamos a esta misma funcion para cada hijo
    }
    nodo.setHijos(hijos);//le asignamos esos hijos al elemento actual
    return nodo;
  }

  //funcion para obtener los hijos de un nodo
  private List<Nodo> getHijos(List<Ceramica> ceramicas, Nodo nodo) {
    List<Nodo> hijos = new ArrayList<>();
    //recorremos todos los elemento
    for (Ceramica c : ceramicas) {
      //si el elemento tiene un padre que coincide con el nodo n entonces lo agregamos
      if (c.getIdPadre() == nodo.getValor().getId()) {
        hijos.add(new Nodo(c));
      }
    }
    return hijos;
  }

  private void revisarFinDeArbol() {//funcion para revisarFinDeArbol al usuario
    //si el siguiente nodo no es un nodo terminal del arbol
    if (!(preguntaActual.getHijos().size() > 0
        && preguntaActual.getHijo(hijoActual).getHijos().size() > 0)) {
      Intent intent = new Intent(IdentificarActivity.this, MapsActivity.class);
      if (preguntaActual.getHijos().size() > 0) {
        intent.putExtra("titulo", preguntaActual.getHijo(hijoActual).getValor().getValor());
      } else {
        intent.putExtra("titulo", preguntaActual.getValor().getValor());
      }
      startActivity(intent);
    }
  }


  public void reiniciar(View view) {//metodo que se ejecuta cuando los botones son presionados
    preguntaActual = raiz;
    hijoActual = 0;
    revisarFinDeArbol();
    recorrerHorizontal();
  }
}
