package com.example.listatareas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
//implementamos Animation.AnimationListener para que poder hacer el intent de cuando acabe la animación
public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        //Obtenemos el titulo
        TextView titulosplash = (TextView) findViewById(R.id.titulosplash);
        //Le damos la animacion
        Animation animacionTitulo = AnimationUtils.loadAnimation(this, R.anim.titulo);
        //Al titulo, le pasamos que comience la animacion
        titulosplash.startAnimation(animacionTitulo);
        //Hay que establecer el listener para cuando acabe salte al metodo
        animacionTitulo.setAnimationListener(this);

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        Intent intentsplash = new Intent(Splash.this, Login.class);
        startActivity(intentsplash);
        finish();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
