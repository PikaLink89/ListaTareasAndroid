package com.example.listatareas;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.listatareas.db.controladorDB;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {
    controladorDB cDB2;
    Toast toast;
    Toast toast2;
    Toast toast3;
    MediaPlayer mp;
    MediaPlayer mpAlert;

    private String usuarioActual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mp = MediaPlayer.create(this, R.raw.click); //Importamos el sonido click
        mpAlert = MediaPlayer.create(this, R.raw.alert); //Importamos el sonido alert
        usuarioActual = null;


        cDB2 = new controladorDB(this);
    }
    //Método para que llama el botón de crear una cuenta, hacemos un alertDialgo metiendo dentro el layout de nuevo_user
    //Extrae el contenido introducido y se lo envía al método .crearUserDB pasándole el usuario y contraseña, el cual se almacenará el la BD.
    //Creamos 2 toasts por si deja en blanco user y pass al crear una cuenta o por si es correcto y da de alta el usuario
    public void crearCuenta(View view){
        mp.start();// Sonido Click
        AlertDialog.Builder dialogUser = new AlertDialog.Builder(this);
        View mview = getLayoutInflater().inflate(R.layout.nuevo_user, null);
        dialogUser.setView(mview);
        final TextInputEditText user = (TextInputEditText) mview.findViewById(R.id.crearuser);
        final TextInputEditText pass = (TextInputEditText) mview.findViewById(R.id.crearpass);
        toast = Toast.makeText(this, "Usuario y contraseña no pueden estar en blanco", Toast.LENGTH_LONG);
        toast2 = Toast.makeText(this, "Usuario "+user.getText().toString()+" dado de alta", Toast.LENGTH_LONG);
        toast3 = Toast.makeText(this, "Usuario "+user.getText().toString()+" ya existente", Toast.LENGTH_LONG);
        AlertDialog dialog = dialogUser.setPositiveButton("Crear", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Si alguno de los dos campos está en blanco muestra el toast incorrecto, de lo contrario, da de alta el user y pass en la bd y toast correcto
                if(user.getText().toString().equals("") || pass.getText().toString().equals("")){
                    mpAlert.start(); //Sonido alerta
                    toast.show();
                }
                else{
                    boolean comprobarUser = false;
                    comprobarUser = cDB2.consultaUser(user.getText().toString());
                    //Comprobamos si existe el usuario, en caso de que exista, alertamos y cancelamos
                    //Si no existe, entonces crea el usuario con los datos pasados
                    if(comprobarUser){
                        mpAlert.start();
                        toast3.show();
                    }
                     else{
                        toast2.show();
                        //Le pasamos al objeto cDB2 el metodo añadir tarea el texto que ha introducido el usuario
                        cDB2.crearUserDB(user.getText().toString(),pass.getText().toString());
                        //Creamos una tabla con el usuario nuevo
                        cDB2.crearTablaUserDB(user.getText().toString());
                        usuarioActual = (user.getText().toString());

                        mp.start();//Click

                    }


                }


            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mp.start();

                    }

                } )
                .create();
        dialog.show();


    }
    //Lee los 2 campos de usuario y pass y se los manda al método iniciar sesión, devuelve true o false, en caso de correcto pasa a la siguiente pantalla
    //Si es incorrecto, muestra un toast con credenciales inválidas.
    public void iniciarSesion(View view){

        TextInputEditText usuarioSes = (TextInputEditText) findViewById(R.id.cajauser);
        TextInputEditText passSes = (TextInputEditText) findViewById(R.id.cajapass);
        boolean status = false;
        status = cDB2.iniciarSesionDB(usuarioSes.getText().toString(), passSes.getText().toString());
        if(status)
        {
            mp.start();// Sonido Click
            usuarioActual = (usuarioSes.getText().toString()); //Metemos en una variable el usuario con el que estamos.
            //Intent permite saltar a otra pantalla.
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            toast = Toast.makeText(this, "Credenciales correctas", Toast.LENGTH_LONG);
            toast.show();
            //finish();
        }
       else{
            mpAlert.start(); //Sonido alerta
        toast = Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_LONG);
        toast.show();
      }
    }
    public String getUsuarioActual() {
        return usuarioActual;
    }

}
