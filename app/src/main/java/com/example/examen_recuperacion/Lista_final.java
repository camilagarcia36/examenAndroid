package com.example.examen_recuperacion;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Lista_final extends AppCompatActivity {

    //Creamos las instancias correspondientes.
    private BaseDatos baseDatos;

    private TableLayout tableLayout;
    private TextView TV1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_final);


        String departamento = getIntent().getExtras().get("departamento").toString();

        //Inicializamos la instancia la BD
        baseDatos = new BaseDatos(this);


        tableLayout = findViewById(R.id.TableLayout);
        TV1 = findViewById(R.id.TV1);
        TV1.setText(departamento);

        obtenerTrabajadores(departamento);
    }

    private void obtenerTrabajadores(String departamento) {
        //Obtenemos el objeto, en este caso el cursor, que contiene toda la informacion introducida de los trabajadores.

        Cursor c = baseDatos.trabajadorPorDepartamento(departamento);
        try {
            if (c != null) {
                do {
                    //Empezamos añadiendo una fila a la tabla, despues crearemos un elemento para la seccion nombre.
                    TableRow row = new TableRow(this);
                    TextView name = new TextView(this);
                    name.setText(c.getString(1));
                    name.setTextSize((float) 17);
                    row.addView(name, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

                    //Fila puesto
                    TextView puesto = new TextView(this);
                    puesto.setText(c.getString(2));
                    puesto.setTextSize((float) 17);
                    row.addView(puesto, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

                    tableLayout.addView(row);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
        }
    }
}