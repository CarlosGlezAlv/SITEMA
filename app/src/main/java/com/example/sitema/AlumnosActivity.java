package com.example.sitema;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class AlumnosActivity extends AppCompatActivity {
    
    private TextInputEditText etNoControl, etNombre, etTelefono;
    private RecyclerView rvMaterias;
    private TextView tvSinMaterias, tvPromedio;
    
    private AlumnoManager alumnoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumnos);

        etNoControl = findViewById(R.id.et_no_control);
        etNombre = findViewById(R.id.et_nombre);
        etTelefono = findViewById(R.id.et_telefono);
        rvMaterias = findViewById(R.id.rv_materias);
        tvSinMaterias = findViewById(R.id.tv_sin_materias);
        tvPromedio = findViewById(R.id.tv_promedio);
        
        alumnoManager = new AlumnoManager(this);

        String idUsuario = getIntent().getStringExtra("ID_USUARIO");
        if (idUsuario != null && !idUsuario.isEmpty()) {
            cargarDatosAlumno(idUsuario);
        }
    }

    private void cargarDatosAlumno(String numControl) {
        alumnoManager.open();
        Alumno alumno = alumnoManager.obtenerAlumnoPorControl(numControl);
        
        if (alumno != null) {
            // Deshabilitar la edición ya que es solo vista para el alumno
            etNoControl.setText(alumno.getNumControl());
            etNoControl.setFocusable(false);
            etNoControl.setClickable(false);
            
            etNombre.setText(alumno.getNombre());
            etNombre.setFocusable(false);
            etNombre.setClickable(false);
            
            etTelefono.setText(alumno.getTelefono());
            etTelefono.setFocusable(false);
            etTelefono.setClickable(false);
            
            // Cargar materias
            ArrayList<MateriaCursada> materias = alumnoManager.obtenerMateriasYCalificaciones(numControl);
            
            if (materias == null || materias.isEmpty()) {
                rvMaterias.setVisibility(View.GONE);
                tvSinMaterias.setVisibility(View.VISIBLE);
                tvPromedio.setText("—");
            } else {
                rvMaterias.setVisibility(View.VISIBLE);
                tvSinMaterias.setVisibility(View.GONE);
                
                rvMaterias.setLayoutManager(new LinearLayoutManager(this));
                MateriaAdapter adapter = new MateriaAdapter(materias);
                rvMaterias.setAdapter(adapter);
                
                double suma = 0;
                for (MateriaCursada m : materias) {
                    suma += m.getCalificacion();
                }
                double promedio = suma / materias.size();
                tvPromedio.setText(String.format("%.2f", promedio));
            }
        }
        
        alumnoManager.close();
    }
}
