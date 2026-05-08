package com.example.sitema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etIdentificacion;
    private Button btnIngresar;

    private DocenteManager docenteManager;
    private AlumnoManager alumnoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Usar la vista de login como pantalla inicial
        setContentView(R.layout.activity_login);

        etIdentificacion = findViewById(R.id.et_identificacion);
        btnIngresar = findViewById(R.id.btn_ingresar);

        docenteManager = new DocenteManager(this);
        alumnoManager = new AlumnoManager(this);

        btnIngresar.setOnClickListener(v -> {
            String idIngresado = etIdentificacion.getText().toString().trim();

            if (idIngresado.isEmpty()) {
                Toast.makeText(MainActivity.this, "Por favor ingrese su identificación", Toast.LENGTH_SHORT).show();
                return;
            }

            // 1. Validar si es el Administrador
            if (idIngresado.equalsIgnoreCase("admin")) {
                abrirActividad(AdministradorActivity.class, "admin");
                return;
            }

            // 2. Validar si es un Docente
            docenteManager.open();
            Docente docente = docenteManager.obtenerDocentePorEmpleado(idIngresado);
            docenteManager.close();

            if (docente != null) {
                abrirActividad(DocentesActivity.class, idIngresado);
                return;
            }

            // 3. Validar si es un Alumno
            alumnoManager.open();
            Alumno alumno = alumnoManager.obtenerAlumnoPorControl(idIngresado);
            alumnoManager.close();

            if (alumno != null) {
                abrirActividad(AlumnosActivity.class, idIngresado);
                return;
            }

            // Si no se encuentra en ningún lado
            Toast.makeText(MainActivity.this, "Identificación no encontrada", Toast.LENGTH_SHORT).show();
        });
    }

    private void abrirActividad(Class<?> actividadDestino, String idUsuario) {
        Intent intent = new Intent(MainActivity.this, actividadDestino);
        // Pasamos el ID por si la siguiente pantalla lo necesita para consultar sus datos
        intent.putExtra("ID_USUARIO", idUsuario);
        startActivity(intent);
    }
}