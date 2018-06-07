package com.lore.arqueolodroid;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap mMap;
  private TextView tRes;
  private String titulo;
  private double lat;
  private double lng;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    tRes = (TextView) findViewById(R.id.tRes);
    Intent intent = getIntent();
    titulo = intent.getStringExtra("titulo");
    getUbicacion(titulo);
    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
  }

  private void getUbicacion(String nombre) {
    Subscription subscription = Cliente.getInstance()
        .getUbicacion(nombre)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Ubicacion>() {
          @Override
          public void onCompleted() {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(MapsActivity.this);
          }

          @Override
          public void onError(Throwable e) {
          }

          @Override
          public void onNext(Ubicacion ubicacion) {
            Toast.makeText(MapsActivity.this, "" + ubicacion.getText(), Toast.LENGTH_SHORT).show();
            lng = ubicacion.getLongitud();
            lat = ubicacion.getLatitud();
          }

        });
  }

  /**
   * Manipulates the map once available.
   * This callback is triggered when the map is ready to be used.
   * This is where we can add markers or lines, add listeners or move the camera. In this case,
   * we just add a marker near Sydney, Australia.
   * If Google Play services is not installed on the device, the user will be prompted to install
   * it inside the SupportMapFragment. This method will only be triggered once the user has
   * installed Google Play services and returned to the app.
   */
  @Override
  public void onMapReady(GoogleMap googleMap) {//metodo que se ejecuta cuando se carga el mapa
    mMap = googleMap;
    mMap.setMinZoomPreference(10.0f);
//    Intent intent = getIntent();
//    BaseDatos datos = new BaseDatos(this, "arqueolodroid");//inicializamos la base de datos
//    titulo = intent
//        .getStringExtra("titulo");//pedimos el titulo que fue pasado como parametro
//    Cursor c = datos.consultar("select * from ubicaciones where nombre='" + titulo
//        + "'");//consultamos la ubicacion con ese titulo para obtener la latitud y longitud
//    if (c.moveToNext()) {//aqui agregamos al mapa un marcador con la ubicacion

    tRes.setText(titulo);
    LatLng pos = new LatLng(lat, lng);
    mMap.addMarker(new MarkerOptions().position(pos).title(titulo));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));

  }
}
