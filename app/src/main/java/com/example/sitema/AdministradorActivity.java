package com.example.sitema;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class AdministradorActivity extends AppCompatActivity {

    private AlumnoManager alumnoManager;
    private DocenteManager docenteManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        alumnoManager = new AlumnoManager(this);
        docenteManager = new DocenteManager(this);

        // Referencias a los botones de la sección ALUMNOS
        MaterialButton btnAltaAlumno = findViewById(R.id.btn_alta_alumno);
        MaterialButton btnVerAlumno = findViewById(R.id.btn_ver_alumno);
        MaterialButton btnEditarAlumno = findViewById(R.id.btn_editar_alumno);
        MaterialButton btnEliminarAlumno = findViewById(R.id.btn_eliminar_alumno);

        // Eventos clic ALUMNOS
        btnAltaAlumno.setOnClickListener(v -> mostrarDialogoAltaAlumno());
        btnVerAlumno.setOnClickListener(v -> mostrarDialogoVerAlumnos());
        btnEditarAlumno.setOnClickListener(v -> mostrarDialogoBuscarParaEditar());
        btnEliminarAlumno.setOnClickListener(v -> mostrarDialogoEliminar());

        // Referencias a los botones de la sección DOCENTES
        MaterialButton btnAltaDocente = findViewById(R.id.btn_alta_docente);
        MaterialButton btnVerDocente = findViewById(R.id.btn_ver_docente);
        MaterialButton btnEditarDocente = findViewById(R.id.btn_editar_docente);
        MaterialButton btnEliminarDocente = findViewById(R.id.btn_eliminar_docente);

        // Eventos clic DOCENTES
        btnAltaDocente.setOnClickListener(v -> mostrarDialogoAltaDocente());
        btnVerDocente.setOnClickListener(v -> mostrarDialogoVerDocentes());
        btnEditarDocente.setOnClickListener(v -> mostrarDialogoBuscarParaEditarDocente());
        btnEliminarDocente.setOnClickListener(v -> mostrarDialogoEliminarDocente());
    }

    // ==========================================
    // ALTA ALUMNO
    // ==========================================
    private void mostrarDialogoAltaAlumno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alta de Alumno");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumControl = new EditText(this);
        etNumControl.setHint("Número de Control");
        layout.addView(etNumControl);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre completo");
        layout.addView(etNombre);

        final EditText etTelefono = new EditText(this);
        etTelefono.setHint("Teléfono");
        layout.addView(etTelefono);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String numControl = etNumControl.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();

            if (numControl.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(this, "El número de control y nombre son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            alumnoManager.open();
            boolean exito = alumnoManager.insertarAlumno(new Alumno(numControl, nombre, telefono));
            alumnoManager.close();

            if (exito) {
                Toast.makeText(this, "Alumno guardado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar (El ID ya existe)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // ==========================================
    // VISUALIZAR ALUMNOS
    // ==========================================
    private void mostrarDialogoVerAlumnos() {
        alumnoManager.open();
        ArrayList<Alumno> lista = alumnoManager.obtenerTodosLosAlumnos();
        alumnoManager.close();

        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay alumnos registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] alumnosArray = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Alumno a = lista.get(i);
            alumnosArray[i] = a.getNumControl() + " - " + a.getNombre() + "\nTel: " + a.getTelefono();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lista de Alumnos");
        builder.setItems(alumnosArray, null);
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    // ==========================================
    // EDITAR ALUMNO
    // ==========================================
    private void mostrarDialogoBuscarParaEditar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Alumno");
        builder.setMessage("Ingrese el Número de Control:");

        final EditText input = new EditText(this);
        input.setHint("Número de control");
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Buscar", (dialog, which) -> {
            String numControl = input.getText().toString().trim();
            alumnoManager.open();
            Alumno alumno = alumnoManager.obtenerAlumnoPorControl(numControl);
            alumnoManager.close();

            if (alumno != null) {
                mostrarDialogoEdicion(alumno);
            } else {
                Toast.makeText(this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEdicion(Alumno alumno) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificando a: " + alumno.getNumControl());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(this);
        etNombre.setText(alumno.getNombre());
        layout.addView(etNombre);

        final EditText etTelefono = new EditText(this);
        etTelefono.setText(alumno.getTelefono());
        layout.addView(etTelefono);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            alumno.setNombre(etNombre.getText().toString().trim());
            alumno.setTelefono(etTelefono.getText().toString().trim());

            alumnoManager.open();
            boolean exito = alumnoManager.actualizarAlumno(alumno);
            alumnoManager.close();

            if (exito) {
                Toast.makeText(this, "Alumno actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ELIMINAR ALUMNO
    // ==========================================
    private void mostrarDialogoEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Alumno");
        builder.setMessage("Ingrese el Número de Control a eliminar:");

        final EditText input = new EditText(this);
        input.setHint("Número de control");
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String numControl = input.getText().toString().trim();
            alumnoManager.open();
            boolean exito = alumnoManager.eliminarAlumno(numControl);
            alumnoManager.close();

            if (exito) {
                Toast.makeText(this, "Alumno eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró el alumno o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ALTA DOCENTE
    // ==========================================
    private void mostrarDialogoAltaDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alta de Docente");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumEmpleado = new EditText(this);
        etNumEmpleado.setHint("Número de Empleado");
        layout.addView(etNumEmpleado);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre completo");
        layout.addView(etNombre);

        final EditText etDireccion = new EditText(this);
        etDireccion.setHint("Dirección");
        layout.addView(etDireccion);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String numEmpleado = etNumEmpleado.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();

            if (numEmpleado.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(this, "El número de empleado y nombre son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            docenteManager.open();
            boolean exito = docenteManager.insertarDocente(new Docente(numEmpleado, nombre, direccion));
            docenteManager.close();

            if (exito) {
                Toast.makeText(this, "Docente guardado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar (El ID ya existe)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // ==========================================
    // VISUALIZAR DOCENTES
    // ==========================================
    private void mostrarDialogoVerDocentes() {
        docenteManager.open();
        ArrayList<Docente> lista = docenteManager.obtenerTodosLosDocentes();
        docenteManager.close();

        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay docentes registrados", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] docentesArray = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Docente d = lista.get(i);
            docentesArray[i] = d.getNumEmpleado() + " - " + d.getNombre() + "\nDir: " + d.getDireccion();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lista de Docentes");
        builder.setItems(docentesArray, null);
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    // ==========================================
    // EDITAR DOCENTE
    // ==========================================
    private void mostrarDialogoBuscarParaEditarDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Docente");
        builder.setMessage("Ingrese el Número de Empleado a editar:");

        final EditText input = new EditText(this);
        input.setHint("Número de empleado");
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Buscar", (dialog, which) -> {
            String numEmpleado = input.getText().toString().trim();
            docenteManager.open();
            Docente docente = docenteManager.obtenerDocentePorEmpleado(numEmpleado);
            docenteManager.close();

            if (docente != null) {
                mostrarDialogoEdicionDocente(docente);
            } else {
                Toast.makeText(this, "Docente no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEdicionDocente(Docente docente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificando a: " + docente.getNumEmpleado());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(this);
        etNombre.setText(docente.getNombre());
        layout.addView(etNombre);

        final EditText etDireccion = new EditText(this);
        etDireccion.setText(docente.getDireccion());
        layout.addView(etDireccion);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            docente.setNombre(etNombre.getText().toString().trim());
            docente.setDireccion(etDireccion.getText().toString().trim());

            docenteManager.open();
            boolean exito = docenteManager.actualizarDocente(docente);
            docenteManager.close();

            if (exito) {
                Toast.makeText(this, "Docente actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ELIMINAR DOCENTE
    // ==========================================
    private void mostrarDialogoEliminarDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Docente");
        builder.setMessage("Ingrese el Número de Empleado a eliminar:");

        final EditText input = new EditText(this);
        input.setHint("Número de empleado");
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String numEmpleado = input.getText().toString().trim();
            docenteManager.open();
            boolean exito = docenteManager.eliminarDocente(numEmpleado);
            docenteManager.close();

            if (exito) {
                Toast.makeText(this, "Docente eliminado correctamente", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No se encontró el docente o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
