package com.example.listatareas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.listatareas.db.controladorDB;

import static androidx.core.os.LocaleListCompat.create;

public class MainActivity extends AppCompatActivity {

    controladorDB cDB;
    ArrayAdapter<String> mAdapter;
    ListView listViewTareas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cDB = new controladorDB(this);
        listViewTareas = (ListView) findViewById(R.id.ListTask);
        actualizadorTareas();
    }
    //Cargamos la barra y le damos funcionalidad

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Cargar la barra
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Si se pulsa el item salir, entonces vamos a la pantalla de Login
        if (item.getItemId() == R.id.itemsalir) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }

        //Dar funcionalidad al item añadir tarea
        //Creamos una caja donde el usuario pueda escribir un texto
        if (item.getItemId() == R.id.nuevaTarea) {
            //Cuando se pulse el item, mostramos un toad de añadir tarea
            Toast toast = Toast.makeText(this, "Añadir tarea", Toast.LENGTH_LONG);
            toast.show();
            final EditText cajaTexto = new EditText(this);
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Agregar tarea")
                    .setMessage("Nombre de la tarea")
                    //Mostramos la caja de texto creada
                    .setView(cajaTexto)
                    //Si pulsa el botón positivo (Añadir) le hacemos un listener
                    .setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Le pasamos al objeto cDB el metodo añadir tarea el texto que ha introducido el usuario
                            cDB.addTarea(cajaTexto.getText().toString());
                            actualizadorTareas();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .create();
            dialog.show();

        }
        return super.onOptionsItemSelected(item);

    }



    //Este método permite actualizar la pantalla de tareas para que se muestren todas
    private void actualizadorTareas(){
        if(cDB.numRegistros()==0){
            listViewTareas.setAdapter(null);
        }
        else{
        //Localizamos el xml de las tareas (item_tareas)
        //Recoge las tareas de item_tareas (titulo_tarea)
        //rellenamos las tareas (.obtenerTareas)
        mAdapter = new ArrayAdapter<>(this, R.layout.item_tareas,R.id.titulo_tarea, cDB.obtenerTareas());
        //Metemos en el ListTask del xml lo que recoge en mAdapter
        listViewTareas.setAdapter(mAdapter);
       }
    }

    public void borrarTarea(View view){
        //Obtenemos el padre del botón (El recuadro que tiene el botón)
        View padreBoton = (View) view.getParent();
        //Obtenemos la tarea que está dentro de ese view (La tarea que tiene a la izq del botón)
        TextView tareaIzqBoton = (TextView) padreBoton.findViewById(R.id.titulo_tarea);
        //Llamamamos al método de borrar la tarea pasándole el String del texto que contiene
        cDB.borrarTareaBD(tareaIzqBoton.getText().toString());
        //Actualizamos las tareas para que desaparezca del listado
        actualizadorTareas();
        //Agregamos un Toast de tarea realizada
        Toast toast = Toast.makeText(this, "Tarea Realizada", Toast.LENGTH_LONG);
        toast.show();
    }

    //Cuando el usuario pulse el botón editar...
    public void editarTareaMain(View view){

        View padreBoton = (View) view.getParent();
        TextView tareaIzqBoton = (TextView) padreBoton.findViewById(R.id.titulo_tarea);
        //Guardamos en una variable el contenido antiguo.
        final String tareaAntigua = tareaIzqBoton.getText().toString();

        final EditText cajaTexto = new EditText(this);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Editar tarea")
                .setMessage("Nuevo nombre de la tarea")
                //Mostramos la caja de texto creada
                .setView(cajaTexto)
                //Si pulsa el botón positivo (Editar) le hacemos un listener
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Le pasamos al objeto cDB el metodo editar tarea el texto que ha introducido el usuario, también el que había anteriormente
                        String tareaNueva = cajaTexto.getText().toString();
                        cDB.editarTareaBD(tareaAntigua, tareaNueva);
                        //Actualizamos para que se visualicen los cambios
                        actualizadorTareas();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
        dialog.show();

    }

}
