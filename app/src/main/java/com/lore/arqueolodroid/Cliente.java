package com.lore.arqueolodroid;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import android.support.annotation.NonNull;
import rx.Observable;

/**
 * Created by loree on 01/06/2018.
 */

public class Cliente {
//  URL de la base de datos
  private static final String BASE_URL = "https://ceramicas-api.herokuapp.com";

  private static Cliente instance;
  private static DataBaseService dataBaseService;


  public static Cliente getInstance() {
    if (instance == null) {
      instance = new Cliente();
    }
    return instance;
  }
//Configuracion:
  private Cliente() {
    final Gson gson =
        new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    final Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build();
    dataBaseService = retrofit.create(DataBaseService.class);
  }

  public Observable<Token> login(@NonNull String credentials, @NonNull String masterToken) {
    return dataBaseService.login(credentials, masterToken);
  }

  public Observable<List<Ceramica>> getCeramicas(){
    return dataBaseService.getCeramicas();
  }

  public Observable<Ubicacion> getUbicacion(String nombre){
    return dataBaseService.getUbicacion(nombre);
  }
}
