package com.example.sitema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class AdministradorActivity extends AppCompatActivity {

    private AlumnoManager alumnoManager;
    private DocenteManager docenteManager;
    private MateriaManager materiaManager;

    // Adapters para las tablas
    private AdminAlumnoAdapter adapterAlumnos;
    private AdminDocenteAdapter adapterDocentes;
    private AdminMateriaAdapter adapterMaterias;

    // Views de estado vacío
    private TextView tvSinAlumnos;
    private TextView tvSinDocentes;
    private TextView tvSinMaterias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> cerrarSesion());

        alumnoManager = new AlumnoManager(this);
        docenteManager = new DocenteManager(this);
        materiaManager = new MateriaManager(this);

        // --- Configurar RecyclerViews de las tablas ---
        tvSinAlumnos  = findViewById(R.id.tv_admin_sin_alumnos);
        tvSinDocentes = findViewById(R.id.tv_admin_sin_docentes);
        tvSinMaterias = findViewById(R.id.tv_admin_sin_materias);

        adapterAlumnos  = new AdminAlumnoAdapter(new ArrayList<>());
        adapterDocentes = new AdminDocenteAdapter(new ArrayList<>());
        adapterMaterias = new AdminMateriaAdapter(new ArrayList<>());

        RecyclerView rvAlumnos  = findViewById(R.id.rv_admin_alumnos);
        RecyclerView rvDocentes = findViewById(R.id.rv_admin_docentes);
        RecyclerView rvMaterias = findViewById(R.id.rv_admin_materias);

        rvAlumnos.setAdapter(adapterAlumnos);
        rvDocentes.setAdapter(adapterDocentes);
        rvMaterias.setAdapter(adapterMaterias);

        // Carga inicial de tablas
        cargarTablas();

        // --- Botones ALUMNOS ---
        MaterialButton btnAltaAlumno    = findViewById(R.id.btn_alta_alumno);
        MaterialButton btnEditarAlumno  = findViewById(R.id.btn_editar_alumno);
        MaterialButton btnEliminarAlumno = findViewById(R.id.btn_eliminar_alumno);

        btnAltaAlumno.setOnClickListener(v -> mostrarDialogoAltaAlumno());
        btnEditarAlumno.setOnClickListener(v -> mostrarDialogoBuscarParaEditar());
        btnEliminarAlumno.setOnClickListener(v -> mostrarDialogoEliminar());

        MaterialButton btnAsignarMateriaAlumno = findViewById(R.id.btn_asignar_materia_alumno);
        btnAsignarMateriaAlumno.setOnClickListener(v -> mostrarDialogoAsignarMateriaAlumno());

        // --- Botones DOCENTES ---
        MaterialButton btnAltaDocente    = findViewById(R.id.btn_alta_docente);
        MaterialButton btnEditarDocente  = findViewById(R.id.btn_editar_docente);
        MaterialButton btnEliminarDocente = findViewById(R.id.btn_eliminar_docente);

        btnAltaDocente.setOnClickListener(v -> mostrarDialogoAltaDocente());
        btnEditarDocente.setOnClickListener(v -> mostrarDialogoBuscarParaEditarDocente());
        btnEliminarDocente.setOnClickListener(v -> mostrarDialogoEliminarDocente());

        MaterialButton btnAsignarMateriaDocente = findViewById(R.id.btn_asignar_materia_docente);
        btnAsignarMateriaDocente.setOnClickListener(v -> mostrarDialogoAsignarMateriaDocente());

        MaterialButton btnAsignarAlumnoDocente = findViewById(R.id.btn_asignar_alumno_docente);
        btnAsignarAlumnoDocente.setOnClickListener(v -> mostrarDialogoAsignarAlumnoDocente());

        // --- Botones MATERIAS ---
        MaterialButton btnAltaMateria    = findViewById(R.id.btn_alta_materia);
        MaterialButton btnEditarMateria  = findViewById(R.id.btn_editar_materia);
        MaterialButton btnEliminarMateria = findViewById(R.id.btn_eliminar_materia);

        btnAltaMateria.setOnClickListener(v -> mostrarDialogoAltaMateria());
        btnEditarMateria.setOnClickListener(v -> mostrarDialogoBuscarParaEditarMateria());
        btnEliminarMateria.setOnClickListener(v -> mostrarDialogoEliminarMateria());
    }

    // ==========================================
    // CARGAR TABLAS
    // ==========================================
    private void cargarTablas() {
        // Alumnos
        alumnoManager.open();
        ArrayList<Alumno> listaA = alumnoManager.obtenerTodosLosAlumnos();
        alumnoManager.close();
        adapterAlumnos.actualizar(listaA);
        tvSinAlumnos.setVisibility(listaA.isEmpty() ? View.VISIBLE : View.GONE);

        // Docentes
        docenteManager.open();
        ArrayList<Docente> listaD = docenteManager.obtenerTodosLosDocentes();
        docenteManager.close();
        adapterDocentes.actualizar(listaD);
        tvSinDocentes.setVisibility(listaD.isEmpty() ? View.VISIBLE : View.GONE);

        // Materias
        materiaManager.open();
        ArrayList<Materia> listaM = materiaManager.obtenerTodasLasMaterias();
        materiaManager.close();
        adapterMaterias.actualizar(listaM);
        tvSinMaterias.setVisibility(listaM.isEmpty() ? View.VISIBLE : View.GONE);
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
                cargarTablas();
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
                cargarTablas();
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
                cargarTablas();
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
                cargarTablas();
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
                cargarTablas();
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
                cargarTablas();
            } else {
                Toast.makeText(this, "No se encontró el docente o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ALTA MATERIA
    // ==========================================
    private void mostrarDialogoAltaMateria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alta de Materia");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etClave = new EditText(this);
        etClave.setHint("Clave de Materia");
        layout.addView(etClave);

        final EditText etNombre = new EditText(this);
        etNombre.setHint("Nombre de la Materia");
        layout.addView(etNombre);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String clave = etClave.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();

            if (clave.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(this, "La clave y nombre son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            materiaManager.open();
            boolean exito = materiaManager.insertarMateria(new Materia(clave, nombre));
            materiaManager.close();

            if (exito) {
                Toast.makeText(this, "Materia guardada con éxito", Toast.LENGTH_SHORT).show();
                cargarTablas();
            } else {
                Toast.makeText(this, "Error al guardar (La clave ya existe)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    // ==========================================
    // VISUALIZAR MATERIAS
    // ==========================================
    private void mostrarDialogoVerMaterias() {
        materiaManager.open();
        ArrayList<Materia> lista = materiaManager.obtenerTodasLasMaterias();
        materiaManager.close();

        if (lista.isEmpty()) {
            Toast.makeText(this, "No hay materias registradas", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] materiasArray = new String[lista.size()];
        for (int i = 0; i < lista.size(); i++) {
            Materia m = lista.get(i);
            materiasArray[i] = m.getClaveMateria() + " - " + m.getNombreMateria();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Lista de Materias");
        builder.setItems(materiasArray, null);
        builder.setPositiveButton("Cerrar", null);
        builder.show();
    }

    // ==========================================
    // EDITAR MATERIA
    // ==========================================
    private void mostrarDialogoBuscarParaEditarMateria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Materia");
        builder.setMessage("Ingrese la Clave de la Materia a editar:");

        final EditText input = new EditText(this);
        input.setHint("Clave de materia");
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Buscar", (dialog, which) -> {
            String clave = input.getText().toString().trim();
            materiaManager.open();
            Materia materia = materiaManager.obtenerMateriaPorClave(clave);
            materiaManager.close();

            if (materia != null) {
                mostrarDialogoEdicionMateria(materia);
            } else {
                Toast.makeText(this, "Materia no encontrada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEdicionMateria(Materia materia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificando a: " + materia.getClaveMateria());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(this);
        etNombre.setText(materia.getNombreMateria());
        layout.addView(etNombre);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            materia.setNombreMateria(etNombre.getText().toString().trim());

            materiaManager.open();
            boolean exito = materiaManager.actualizarMateria(materia);
            materiaManager.close();

            if (exito) {
                Toast.makeText(this, "Materia actualizada", Toast.LENGTH_SHORT).show();
                cargarTablas();
            } else {
                Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ELIMINAR MATERIA
    // ==========================================
    private void mostrarDialogoEliminarMateria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Materia");
        builder.setMessage("Ingrese la Clave de la Materia a eliminar:");

        final EditText input = new EditText(this);
        input.setHint("Clave de materia");
        LinearLayout layout = new LinearLayout(this);
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String clave = input.getText().toString().trim();
            materiaManager.open();
            boolean exito = materiaManager.eliminarMateria(clave);
            materiaManager.close();

            if (exito) {
                Toast.makeText(this, "Materia eliminada correctamente", Toast.LENGTH_SHORT).show();
                cargarTablas();
            } else {
                Toast.makeText(this, "No se encontró la materia o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ASIGNAR MATERIA A ALUMNO
    // ==========================================
    private void mostrarDialogoAsignarMateriaAlumno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Asignar Materia a Alumno");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumControl = new EditText(this);
        etNumControl.setHint("Número de Control del Alumno");
        layout.addView(etNumControl);

        final EditText etClave = new EditText(this);
        etClave.setHint("Clave de la Materia");
        layout.addView(etClave);

        final EditText etCalificacion = new EditText(this);
        etCalificacion.setHint("Calificación (ej. 10.0)");
        layout.addView(etCalificacion);

        builder.setView(layout);

        builder.setPositiveButton("Asignar", (dialog, which) -> {
            String numControl = etNumControl.getText().toString().trim();
            String clave = etClave.getText().toString().trim();
            String calificacionStr = etCalificacion.getText().toString().trim();

            if (numControl.isEmpty() || clave.isEmpty() || calificacionStr.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double calificacion = 0;
            try {
                calificacion = Double.parseDouble(calificacionStr);
            } catch (Exception e) {
                Toast.makeText(this, "Calificación inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            alumnoManager.open();
            boolean exito = alumnoManager.asignarMateria(numControl, clave, calificacion);
            alumnoManager.close();

            if (exito) {
                Toast.makeText(this, "Materia asignada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al asignar (¿Ya está asignada o no existe el alumno/materia?)", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ASIGNAR MATERIA A DOCENTE
    // ==========================================
    private void mostrarDialogoAsignarMateriaDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Asignar Materia a Docente");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumEmpleado = new EditText(this);
        etNumEmpleado.setHint("Número de Empleado del Docente");
        layout.addView(etNumEmpleado);

        final EditText etClave = new EditText(this);
        etClave.setHint("Clave de la Materia");
        layout.addView(etClave);

        builder.setView(layout);

        builder.setPositiveButton("Asignar", (dialog, which) -> {
            String numEmpleado = etNumEmpleado.getText().toString().trim();
            String clave = etClave.getText().toString().trim();

            if (numEmpleado.isEmpty() || clave.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            docenteManager.open();
            boolean exito = docenteManager.asignarMateria(numEmpleado, clave);
            docenteManager.close();

            if (exito) {
                Toast.makeText(this, "Materia asignada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al asignar (¿Ya está asignada o no existe el docente/materia?)", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // ASIGNAR ALUMNO A DOCENTE
    // ==========================================
    private void mostrarDialogoAsignarAlumnoDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Asignar Alumno a Docente");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumEmpleado = new EditText(this);
        etNumEmpleado.setHint("Número de Empleado del Docente");
        layout.addView(etNumEmpleado);

        final EditText etNumControl = new EditText(this);
        etNumControl.setHint("Número de Control del Alumno");
        layout.addView(etNumControl);

        builder.setView(layout);

        builder.setPositiveButton("Asignar", (dialog, which) -> {
            String numEmpleado = etNumEmpleado.getText().toString().trim();
            String numControl = etNumControl.getText().toString().trim();

            if (numEmpleado.isEmpty() || numControl.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            docenteManager.open();
            boolean exito = docenteManager.asignarAlumno(numEmpleado, numControl);
            docenteManager.close();

            if (exito) {
                Toast.makeText(this, "Alumno asignado al docente con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al asignar (¿Ya está asignado o no existe el docente/alumno?)", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    // ==========================================
    // CERRAR SESIÓN
    // ==========================================
    private void cerrarSesion() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
