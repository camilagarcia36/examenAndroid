package com.example.examen_recuperacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatos extends SQLiteOpenHelper {


    private static final int BASEDATOS_VERSION = 1;
    private static final String BASEDATOS_NOMBRE = "datos.db";

    private static final String TABLA_TRABAJADOR = "trabajadores";
    private static final String COLUMNA_ID = "id";
    private static final String COLUMNA_NOMBRE = "nombre";
    private static final String COLUMNA_PUESTO = "puesto";
    private static final String COLUMNA_DEPARTAMENTO = "departmento";


    //SQL para crear una tabla.
    private static final String SQL_CREAR_TRABAJADOR = "CREATE TABLE IF NOT EXISTS " + TABLA_TRABAJADOR + " ("
            + COLUMNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_NOMBRE + " CHAR, "
            + COLUMNA_PUESTO + " CHAR, "
            + COLUMNA_DEPARTAMENTO + " CHAR);";


    public BaseDatos(Context context) {
        super(context, BASEDATOS_NOMBRE, null, BASEDATOS_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase databasedb) {
        databasedb.execSQL(SQL_CREAR_TRABAJADOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase databasedb, int i, int i1) {
    }

    //Añadimos un trabajador con su nombre, puesto y departamento.Con los datos que indica el usuario.
    public boolean añadirTrabajador(String nombre, String puesto, String departamento) {
        try {
            SQLiteDatabase databasedb = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(COLUMNA_NOMBRE, nombre);
            cv.put(COLUMNA_PUESTO, puesto);
            cv.put(COLUMNA_DEPARTAMENTO, departamento);

            databasedb.insert(TABLA_TRABAJADOR, null, cv);
            databasedb.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //información de los trabajadores en cada departamento.
    public Cursor trabajadorPorDepartamento(String departmento) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLA_TRABAJADOR + " WHERE " + COLUMNA_DEPARTAMENTO + "='" + departmento + "'", null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    //Obtenemos los departamentos registrados en BBDD, unicamente.
    public Cursor obtenerDepartamentos() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMNA_DEPARTAMENTO + " FROM " + TABLA_TRABAJADOR + " GROUP BY " + COLUMNA_DEPARTAMENTO, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
