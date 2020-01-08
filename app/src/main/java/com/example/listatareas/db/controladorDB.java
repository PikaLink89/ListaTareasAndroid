package com.example.listatareas.db;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

//Extendemos de SQLiteOpenHelper para que funcione, implementando sus métodos
//También es obligatorio un constructor
public class controladorDB extends SQLiteOpenHelper {
    public controladorDB(@Nullable Context context) {
        //El nombre de la db será el nombre de la clase completa, versión 1.
        super(context, "com.example.listatareas.db", null, 1);
    }

    @Override
    //Aquí se crea la tabla
    public void onCreate(SQLiteDatabase db) {
        //Con db.execSQL damos la instruccion para crear la tabla de tareas
    //db.execSQL("CREATE TABLE TASKS (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL);");
        //Con db.execSQL damos la instruccion para crear la tabla de usuarios
    db.execSQL("CREATE TABLE USERS (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER TEXT NOT NULL, PASS TEXT NOT NULL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //Metodo que recoge la tarea de la ventana de crear tarea y lo guarda en la BD
    public void addTarea(String tarea){

        ContentValues registro = new ContentValues();
        //Creamos el registro con el campo y el valor (el que le pasamos)
        registro.put("NAME", tarea);
        //Guardamos en una variable db la bd abierta para leer y escribir
        SQLiteDatabase db = this.getWritableDatabase();
        //Insertamos los datos
        db.insert("TASKS", null, registro);
        //Cerramos la bd
        db.close();
    }

    public String[] obtenerTareas(){

        SQLiteDatabase db = this.getReadableDatabase();
        //Hacemos la consulta en formato SQL, lo guardamos en un cursor.
        //Cursor resultadoCursor = db.rawQuery("SELECT* FROM TASKS",null);
        Cursor resultadoCursor = db.rawQuery("SELECT* FROM TASKS",null);
        int regs = resultadoCursor.getCount(); //Vemos cuantos registros hemos obtenido
        if(regs==0){
            db.close();
            return null;
        }
        else{
            String[] resultados = new String[regs];
            resultadoCursor.moveToFirst();
            for(int i=0; i<regs; i++){
                resultados[i] = resultadoCursor.getString(1); //Posición 1 por que en el cursor hemos guardado todo.
                                                                          //Si en el cursor hubieramos guardado solo el campo NAME entonces sería posicion 0
                resultadoCursor.moveToNext();//Nos movemos al siguiente registro
            }
            db.close();
            return resultados;

        }

    }
    //Consultamos el número de registros que tenemos.
    public int numRegistros(){
        SQLiteDatabase db = this.getReadableDatabase();
        //Hacemos la consulta en formato SQL, lo guardamos en un cursor.
        Cursor resultadoCursor = db.rawQuery("SELECT* FROM TASKS",null);
        int regs = resultadoCursor.getCount();
        return regs;

    }

    //Metodo que borrará la tarea que le pasemos
    public void borrarTareaBD(String tarea){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete("TASKS","NAME=?",new String[]{tarea});
        db.close();

    }

    //Para editar la tarea, recogemos lo que nos envía en el Main, la tarea antigua y la tarea nueva.
    public void editarTareaBD(String tareaAntigua, String tareaNueva){
        SQLiteDatabase db = this.getWritableDatabase();
        //Realizamos un udpate  haciendo un set con la nueva tarea y un where de la tarea antigua.
        db.execSQL("UPDATE TASKS SET NAME ='"+ tareaNueva +"' WHERE NAME = '"+ tareaAntigua +"'");
        db.close();
    }

    //Metodo que agrega el user y pass que le pasamos a la tabla
    public void crearUserDB(String usuarioBD, String passBD){

        ContentValues registro = new ContentValues();
        //Creamos el registro con el campo y el valor (los que le pasamos)
        registro.put("USER", usuarioBD);
        registro.put("PASS", passBD);
        //Guardamos en una variable db la bd abierta para leer y escribir
        SQLiteDatabase db = this.getWritableDatabase();
        //Insertamos los datos
        db.insert("USERS", null, registro);
        db.insert("PASS", null, registro);
        //Cerramos la bd
        db.close();
    }

    //Metodo para crear tabla con el usuario que le pasamos que serán sus tareas

    public void crearTablaUserDB(String userTabla){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("CREATE TABLE '"+ userTabla +"' (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT NOT NULL);");
        db.close();

    }

    //Metodo para consultar si el usuario existe en la tabla
    public boolean consultaUser(String usuarioBD){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor resultadoCursorLogin = db.rawQuery("SELECT PASS FROM USERS WHERE (USER =?) ", new String[]{usuarioBD});
        //Cursor cursore = db.rawQuery("SELECT * FROM USERS", null);
        //db.execSQL("SELECT PASS FROM USERS WHERE NAME = '"+ user +"'");
        int regs = resultadoCursorLogin.getCount(); //Vemos cuantos registros hemos obtenido
        db.close();
        if(regs==0){
            return false;
        }
        else{
            return true;
        }

    }
    //Comprueba que user y pass coinciden para poder iniciar sesion
    public boolean iniciarSesionDB(String user, String pass){

        SQLiteDatabase db = this.getReadableDatabase();
       Cursor resultadoCursorLogin = db.rawQuery("SELECT PASS FROM USERS WHERE (USER =?) AND (PASS=?) ", new String[]{user,pass});
        //Cursor cursore = db.rawQuery("SELECT * FROM USERS", null);
        //db.execSQL("SELECT PASS FROM USERS WHERE NAME = '"+ user +"'");
        int regs = resultadoCursorLogin.getCount(); //Vemos cuantos registros hemos obtenido
        db.close();
        if(regs==0){
            return false;
        }
        else{
            return true;
        }


    }

}
