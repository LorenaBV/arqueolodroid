package com.lore.arqueolodroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

  private EditText eUsuario, ePass;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    eUsuario = findViewById(R.id.eUsuario);
    ePass = findViewById(R.id.ePass);
  }

  public void login(View view) {//metodo que se ejecuta al presionar el boton login
    String usuario = eUsuario.getText().toString();
    String contrasena = ePass.getText().toString();
//    codigo para autentificar en la bas de datos
    final String MASTER_TOKEN = "cyqkpElAVO4YpJxvpAHjoMCKHiypXe45";

    // se prepara el usuario y contrasena para enviarlos
    String datos = "Basic " + Base64
        .encodeToString(String.format("%s:%s", usuario, contrasena).getBytes(), Base64.NO_WRAP);

    Subscription subscription = Cliente.getInstance()
        .login(datos, MASTER_TOKEN)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Token>() {
          @Override
          public void onCompleted() {
          }

          @Override
          public void onError(Throwable e) {
            e.printStackTrace();
            if (e.getMessage().contains("401")) {
              Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta",
                  Toast.LENGTH_SHORT).show();
            }
          }

          @Override
          public void onNext(Token token) {
            if (!token.getToken().isEmpty()) {
              //si se recibio un token los datos son correctos
              startActivity(new Intent(LoginActivity.this, IdentificarActivity.class));
            }
          }
        });

  }
}
