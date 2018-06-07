package com.lore.arqueolodroid;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface DataBaseService {
// las consultas a la base de datos

//  consulta de usuario para el login
  @FormUrlEncoded
  @POST("/auth")
  Observable<Token> login(
      @Header("Authorization") String credentials,
      @Field("access_token") String access);

//  para obtener las ceramicas
  @GET("/ceramicas")
  Observable<List<Ceramica>> getCeramicas();

//  para obtener la ubicacion de una ceramica
  @GET("/ubicaciones/{nombre}")
  Observable<Ubicacion> getUbicacion(@Path("nombre") String nombre);
}
