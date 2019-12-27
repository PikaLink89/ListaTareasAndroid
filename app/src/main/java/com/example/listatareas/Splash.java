package com.example.listatareas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

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
    }
}
