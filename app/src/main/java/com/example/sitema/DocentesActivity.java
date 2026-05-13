package com.example.sitema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class DocentesActivity extends AppCompatActivity {

    // Vistas de datos del docente
    private TextInputEditText etNoEmpleado, etNombreDocente, etDireccion;

    // Selector de alumno
    private AutoCompleteTextView acvSelectorAlumno;

    // Items de materia-calificación (máximo 5 fijos en el layout)
    private View[] itemsMateria;
    private static final int[] ITEM_IDS = {
            R.id.item_doc_1, R.id.item_doc_2, R.id.item_doc_3,
            R.id.item_doc_4, R.id.item_doc_5
    };

    private DocenteManager docenteManager;
    private AlumnoManager alumnoManager;

    private String numEmpleadoActual;
    private ArrayList<Alumno> alumnosDelDocente = new ArrayList<>();
    private Alumno alumnoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docentes);

        // Toolbar con botón de regreso
        MaterialToolbar toolbar = findViewById(R.id.toolbar_docentes);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> cerrarSesion());

        // Referencias a campos del docente
        etNoEmpleado   = findViewById(R.id.et_no_empleado);
        etNombreDocente = findViewById(R.id.et_nombre_docente);
        etDireccion    = findViewById(R.id.et_direccion);

        // Selector de alumno
        acvSelectorAlumno = findViewById(R.id.acv_selector_alumno);

        // Guardar referencias a los 5 items de materia del layout
        itemsMateria = new View[ITEM_IDS.length];
        for (int i = 0; i < ITEM_IDS.length; i++) {
            itemsMateria[i] = findViewById(ITEM_IDS[i]);
            itemsMateria[i].setVisibility(View.GONE); // ocultamos por defecto
        }

        // Managers
        docenteManager = new DocenteManager(this);
        alumnoManager  = new AlumnoManager(this);

        // Cargar datos del docente que hizo login
        numEmpleadoActual = getIntent().getStringExtra("ID_USUARIO");
        if (numEmpleadoActual != null && !numEmpleadoActual.isEmpty()) {
            cargarDatosDocente(numEmpleadoActual);
        }

        // Cuando el docente selecciona un alumno del dropdown
        acvSelectorAlumno.setOnItemClickListener((parent, view, position, id) -> {
            alumnoSeleccionado = alumnosDelDocente.get(position);
            cargarMateriasDelAlumno(alumnoSeleccionado.getNumControl());
        });
    }

    /**
     * Carga y muestra los datos del docente en los campos de texto.
     * Luego carga la lista de alumnos que tiene asignados.
     */
    private void cargarDatosDocente(String numEmpleado) {
        docenteManager.open();
        Docente docente = docenteManager.obtenerDocentePorEmpleado(numEmpleado);
        docenteManager.close();

        if (docente == null) {
            Toast.makeText(this, "Error al cargar datos del docente", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostrar datos (solo lectura)
        etNoEmpleado.setText(docente.getNumEmpleado());
        etNoEmpleado.setFocusable(false);
        etNoEmpleado.setClickable(false);

        etNombreDocente.setText(docente.getNombre());
        etNombreDocente.setFocusable(false);
        etNombreDocente.setClickable(false);

        etDireccion.setText(docente.getDireccion());
        etDireccion.setFocusable(false);
        etDireccion.setClickable(false);

        // Cargar alumnos asignados a este docente
        cargarAlumnosDelDocente(numEmpleado);
    }

    /**
     * Obtiene los alumnos asignados al docente y puebla el dropdown.
     */
    private void cargarAlumnosDelDocente(String numEmpleado) {
        docenteManager.open();
        alumnosDelDocente = docenteManager.obtenerAlumnosPorDocente(numEmpleado);
        docenteManager.close();

        if (alumnosDelDocente.isEmpty()) {
            acvSelectorAlumno.setHint("Sin alumnos asignados");
            acvSelectorAlumno.setEnabled(false);
            return;
        }

        String[] nombres = new String[alumnosDelDocente.size()];
        for (int i = 0; i < alumnosDelDocente.size(); i++) {
            Alumno a = alumnosDelDocente.get(i);
            nombres[i] = a.getNumControl() + " - " + a.getNombre();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, nombres);
        acvSelectorAlumno.setAdapter(adapter);
    }

    /**
     * Carga las materias que el docente imparte al alumno seleccionado
     * y las muestra en los items fijos del layout con sus calificaciones.
     */
    private void cargarMateriasDelAlumno(String numControl) {
        docenteManager.open();
        ArrayList<MateriaCursada> materias = docenteManager.obtenerMateriasDeAlumnoPorDocente(numEmpleadoActual, numControl);
        docenteManager.close();

        // Ocultar todos los items primero
        for (View item : itemsMateria) {
            item.setVisibility(View.GONE);
        }

        if (materias == null || materias.isEmpty()) {
            Toast.makeText(this, "Este alumno no tiene materias asignadas a tu clase", Toast.LENGTH_SHORT).show();
            return;
        }

        int maxItems = Math.min(materias.size(), itemsMateria.length);
        for (int i = 0; i < maxItems; i++) {
            View item = itemsMateria[i];
            item.setVisibility(View.VISIBLE);

            TextView tvNombre = item.findViewById(R.id.tv_materia_nombre);
            EditText etCalif  = item.findViewById(R.id.et_calificacion);
            MaterialButton btnAsignar = item.findViewById(R.id.btn_asignar_calif);

            MateriaCursada mc = materias.get(i);
            tvNombre.setText(mc.getNombreMateria());
            etCalif.setText(String.valueOf((int) mc.getCalificacion()));

            // Referencia final para el listener
            final String claveMateria = mc.getClaveMateria();
            final String numCtrl = numControl;

            btnAsignar.setOnClickListener(v -> {
                String califStr = etCalif.getText().toString().trim();
                if (califStr.isEmpty()) {
                    Toast.makeText(this, "Ingrese la calificación", Toast.LENGTH_SHORT).show();
                    return;
                }
                double nuevaCalif;
                try {
                    nuevaCalif = Double.parseDouble(califStr);
                    if (nuevaCalif < 0 || nuevaCalif > 100) {
                        Toast.makeText(this, "La calificación debe estar entre 0 y 100", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Calificación inválida", Toast.LENGTH_SHORT).show();
                    return;
                }

                alumnoManager.open();
                boolean ok = alumnoManager.actualizarCalificacion(numCtrl, claveMateria, nuevaCalif);
                alumnoManager.close();

                if (ok) {
                    Toast.makeText(this, "Calificación actualizada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Error al actualizar calificación", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void cerrarSesion() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
