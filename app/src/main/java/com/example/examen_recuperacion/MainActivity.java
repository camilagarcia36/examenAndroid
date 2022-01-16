package com.example.examen_recuperacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText puesto;
    private Button boton;
    private Spinner departamento;

    private ListView listaDepartamentos;
    private ArrayList departamentosArray;
    private ArrayAdapter adapter;

    private BaseDatos baseDatos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Encontramos las instancias con el id.
        nombre = findViewById(R.id.nombre);
        puesto = findViewById(R.id.puesto);
        departamento = findViewById(R.id.departamento);
        boton = findViewById(R.id.boton);
        listaDepartamentos = findViewById(R.id.lista_departamento);

        baseDatos = new BaseDatos(this);

        departamentosArray = new ArrayList();
        adapter = new ArrayAdapter(this, android.R.layout.simple_selectable_list_item,departamentosArray );
        listaDepartamentos.setAdapter(adapter);


        //Cogemos los departamentos del BBDD y los creamos.
        obtenerDepartamentos ();
        añadirDepartamentos ();
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validamos que los campos no esten vacios
                if (validacionNombre()) {
                    if (validacionPuesto() ) {
                        String departmentSelected = departamento.getSelectedItem().toString();
                        saveInDatabase(nombre.getText().toString(), puesto.getText().toString(), departmentSelected);
                    }else {
                        Toast.makeText(MainActivity.this, "Debes rellenar el campo puesto.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this, "Debes rellenar el campo nombre", Toast.LENGTH_SHORT).show();
                }
            }
        });


        listaDepartamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String departmento = adapterView.getItemAtPosition(i).toString();
                Intent intent = new Intent(getApplicationContext(), Lista_final.class);
                intent.putExtra("departamento", departmento);
                startActivity(intent);

            }
        });


    }
    //Método para almacenar en la base de datos
    private void saveInDatabase(String nombre, String puesto, String departamento) {
        if(baseDatos.añadirTrabajador(nombre, puesto, departamento)) {
            Toast.makeText(this, "Trabajador añadido. ", Toast.LENGTH_SHORT).show();
            clearFields();
            adapter.clear();
            obtenerDepartamentos();
        }else {
            Toast.makeText(this, "Los datos no se pudieron guardar.", Toast.LENGTH_SHORT).show();
        }

    }

    //Validamos que el nombre no esté vacío
    private boolean validacionNombre() {
        return !nombre.getText().toString().isEmpty() && !nombre.getText().toString().equals("");
    }

    //Validamos que el puesto no esté vacío
    private boolean validacionPuesto() {
        return !puesto.getText().toString().isEmpty();
    }

    //Creamos un arreglo con los tipos de departamentos permitidos
    //Creamos un adaptador que se va a encargar de manipular la lista
    //Le asignamos el adaptador al spinner
    private void añadirDepartamentos() {
        String [] departamentosArray = new String[] {"Ventas", "Desarrollo", "Marketing", "Dirección"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departamentosArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departamento.setAdapter(adapter);
    }



    private void clearFields() {
        nombre.setText("");
        puesto.setText("");
    }

    //Obtener departamentos de BBDD.
    private void obtenerDepartamentos() {
        //Obtenemos el objeto cursor que contiene toda la información de los departamentos
        Cursor c = baseDatos.obtenerDepartamentos();
        try {
            if(c != null) {
                do {
                    departamentosArray.add(c.getString(0));
                    adapter.notifyDataSetChanged();
                }while(c.moveToNext());
            }
        }catch (Exception e) {
        }
    }
}