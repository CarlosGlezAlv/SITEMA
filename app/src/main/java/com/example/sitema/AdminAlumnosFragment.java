package com.example.sitema;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;

public class AdminAlumnosFragment extends Fragment {

    private AlumnoManager alumnoManager;
    private AdminAlumnoAdapter adapterAlumnos;
    private TextView tvSinAlumnos;
    private ArrayList<Alumno> listaCompletaAlumnos = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_alumnos, container, false);

        alumnoManager = new AlumnoManager(requireContext());
        
        tvSinAlumnos = view.findViewById(R.id.tv_admin_sin_alumnos);
        adapterAlumnos = new AdminAlumnoAdapter(new ArrayList<>());
        RecyclerView rvAlumnos = view.findViewById(R.id.rv_admin_alumnos);
        rvAlumnos.setAdapter(adapterAlumnos);

        MaterialButton btnAltaAlumno = view.findViewById(R.id.btn_alta_alumno);
        MaterialButton btnEditarAlumno = view.findViewById(R.id.btn_editar_alumno);
        MaterialButton btnEliminarAlumno = view.findViewById(R.id.btn_eliminar_alumno);
        MaterialButton btnAsignarMateriaAlumno = view.findViewById(R.id.btn_asignar_materia_alumno);

        btnAltaAlumno.setOnClickListener(v -> mostrarDialogoAltaAlumno());
        btnEditarAlumno.setOnClickListener(v -> mostrarDialogoBuscarParaEditar());
        btnEliminarAlumno.setOnClickListener(v -> mostrarDialogoEliminar());
        btnAsignarMateriaAlumno.setOnClickListener(v -> mostrarDialogoAsignarMateriaAlumno());

        EditText etBuscarAlumno = view.findViewById(R.id.et_buscar_alumno);
        etBuscarAlumno.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarAlumnos(s.toString());
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        cargarTabla();

        return view;
    }

    private void cargarTabla() {
        alumnoManager.open();
        listaCompletaAlumnos = alumnoManager.obtenerTodosLosAlumnos();
        alumnoManager.close();
        
        EditText etBuscar = getView() != null ? getView().findViewById(R.id.et_buscar_alumno) : null;
        if (etBuscar != null && !etBuscar.getText().toString().isEmpty()) {
            filtrarAlumnos(etBuscar.getText().toString());
        } else {
            adapterAlumnos.actualizar(listaCompletaAlumnos);
            tvSinAlumnos.setVisibility(listaCompletaAlumnos.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    private void filtrarAlumnos(String query) {
        ArrayList<Alumno> filtrada = new ArrayList<>();
        String lowerQuery = query.toLowerCase();
        for (Alumno a : listaCompletaAlumnos) {
            if (a.getNumControl().toLowerCase().contains(lowerQuery) ||
                a.getNombre().toLowerCase().contains(lowerQuery)) {
                filtrada.add(a);
            }
        }
        adapterAlumnos.actualizar(filtrada);
        tvSinAlumnos.setVisibility(filtrada.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void mostrarDialogoAltaAlumno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Alta de Alumno");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumControl = new EditText(requireContext());
        etNumControl.setHint("Número de Control");
        layout.addView(etNumControl);

        final EditText etNombre = new EditText(requireContext());
        etNombre.setHint("Nombre completo");
        layout.addView(etNombre);

        final EditText etTelefono = new EditText(requireContext());
        etTelefono.setHint("Teléfono");
        layout.addView(etTelefono);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String numControl = etNumControl.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String telefono = etTelefono.getText().toString().trim();

            if (numControl.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(requireContext(), "El número de control y nombre son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            alumnoManager.open();
            boolean exito = alumnoManager.insertarAlumno(new Alumno(numControl, nombre, telefono));
            alumnoManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Alumno guardado con éxito", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "Error al guardar (El ID ya existe)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void mostrarDialogoBuscarParaEditar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar Alumno");
        builder.setMessage("Ingrese el Número de Control:");

        final EditText input = new EditText(requireContext());
        input.setHint("Número de control");
        LinearLayout layout = new LinearLayout(requireContext());
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
                Toast.makeText(requireContext(), "Alumno no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEdicion(Alumno alumno) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Modificando a: " + alumno.getNumControl());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(requireContext());
        etNombre.setText(alumno.getNombre());
        layout.addView(etNombre);

        final EditText etTelefono = new EditText(requireContext());
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
                Toast.makeText(requireContext(), "Alumno actualizado", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Eliminar Alumno");
        builder.setMessage("Ingrese el Número de Control a eliminar:");

        final EditText input = new EditText(requireContext());
        input.setHint("Número de control");
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String numControl = input.getText().toString().trim();
            alumnoManager.open();
            boolean exito = alumnoManager.eliminarAlumno(numControl);
            alumnoManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Alumno eliminado correctamente", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "No se encontró el alumno o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoAsignarMateriaAlumno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Asignar Materia a Alumno");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumControl = new EditText(requireContext());
        etNumControl.setHint("Número de Control del Alumno");
        layout.addView(etNumControl);

        final EditText etClave = new EditText(requireContext());
        etClave.setHint("Clave de la Materia");
        layout.addView(etClave);

        final EditText etCalificacion = new EditText(requireContext());
        etCalificacion.setHint("Calificación (ej. 10.0)");
        layout.addView(etCalificacion);

        builder.setView(layout);

        builder.setPositiveButton("Asignar", (dialog, which) -> {
            String numControl = etNumControl.getText().toString().trim();
            String clave = etClave.getText().toString().trim();
            String calificacionStr = etCalificacion.getText().toString().trim();

            if (numControl.isEmpty() || clave.isEmpty() || calificacionStr.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            double calificacion = 0;
            try {
                calificacion = Double.parseDouble(calificacionStr);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Calificación inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            alumnoManager.open();
            boolean exito = alumnoManager.asignarMateria(numControl, clave, calificacion);
            alumnoManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Materia asignada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error al asignar (¿Ya está asignada o no existe el alumno/materia?)", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
