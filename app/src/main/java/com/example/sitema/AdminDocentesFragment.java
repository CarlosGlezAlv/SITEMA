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

public class AdminDocentesFragment extends Fragment {

    private DocenteManager docenteManager;
    private AdminDocenteAdapter adapterDocentes;
    private TextView tvSinDocentes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_docentes, container, false);

        docenteManager = new DocenteManager(requireContext());
        
        tvSinDocentes = view.findViewById(R.id.tv_admin_sin_docentes);
        adapterDocentes = new AdminDocenteAdapter(new ArrayList<>());
        RecyclerView rvDocentes = view.findViewById(R.id.rv_admin_docentes);
        rvDocentes.setAdapter(adapterDocentes);

        MaterialButton btnAltaDocente = view.findViewById(R.id.btn_alta_docente);
        MaterialButton btnEditarDocente = view.findViewById(R.id.btn_editar_docente);
        MaterialButton btnEliminarDocente = view.findViewById(R.id.btn_eliminar_docente);
        MaterialButton btnAsignarMateriaDocente = view.findViewById(R.id.btn_asignar_materia_docente);
        MaterialButton btnAsignarAlumnoDocente = view.findViewById(R.id.btn_asignar_alumno_docente);

        btnAltaDocente.setOnClickListener(v -> mostrarDialogoAltaDocente());
        btnEditarDocente.setOnClickListener(v -> mostrarDialogoBuscarParaEditarDocente());
        btnEliminarDocente.setOnClickListener(v -> mostrarDialogoEliminarDocente());
        btnAsignarMateriaDocente.setOnClickListener(v -> mostrarDialogoAsignarMateriaDocente());
        btnAsignarAlumnoDocente.setOnClickListener(v -> mostrarDialogoAsignarAlumnoDocente());

        cargarTabla();

        return view;
    }

    private void cargarTabla() {
        docenteManager.open();
        ArrayList<Docente> listaD = docenteManager.obtenerTodosLosDocentes();
        docenteManager.close();
        adapterDocentes.actualizar(listaD);
        tvSinDocentes.setVisibility(listaD.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void mostrarDialogoAltaDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Alta de Docente");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumEmpleado = new EditText(requireContext());
        etNumEmpleado.setHint("Número de Empleado");
        layout.addView(etNumEmpleado);

        final EditText etNombre = new EditText(requireContext());
        etNombre.setHint("Nombre completo");
        layout.addView(etNombre);

        final EditText etDireccion = new EditText(requireContext());
        etDireccion.setHint("Dirección");
        layout.addView(etDireccion);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String numEmpleado = etNumEmpleado.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();

            if (numEmpleado.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(requireContext(), "El número de empleado y nombre son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            docenteManager.open();
            boolean exito = docenteManager.insertarDocente(new Docente(numEmpleado, nombre, direccion));
            docenteManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Docente guardado con éxito", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "Error al guardar (El ID ya existe)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void mostrarDialogoBuscarParaEditarDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar Docente");
        builder.setMessage("Ingrese el Número de Empleado a editar:");

        final EditText input = new EditText(requireContext());
        input.setHint("Número de empleado");
        LinearLayout layout = new LinearLayout(requireContext());
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
                Toast.makeText(requireContext(), "Docente no encontrado", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEdicionDocente(Docente docente) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Modificando a: " + docente.getNumEmpleado());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(requireContext());
        etNombre.setText(docente.getNombre());
        layout.addView(etNombre);

        final EditText etDireccion = new EditText(requireContext());
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
                Toast.makeText(requireContext(), "Docente actualizado", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminarDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Eliminar Docente");
        builder.setMessage("Ingrese el Número de Empleado a eliminar:");

        final EditText input = new EditText(requireContext());
        input.setHint("Número de empleado");
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String numEmpleado = input.getText().toString().trim();
            docenteManager.open();
            boolean exito = docenteManager.eliminarDocente(numEmpleado);
            docenteManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Docente eliminado correctamente", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "No se encontró el docente o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoAsignarMateriaDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Asignar Materia a Docente");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumEmpleado = new EditText(requireContext());
        etNumEmpleado.setHint("Número de Empleado del Docente");
        layout.addView(etNumEmpleado);

        final EditText etClave = new EditText(requireContext());
        etClave.setHint("Clave de la Materia");
        layout.addView(etClave);

        builder.setView(layout);

        builder.setPositiveButton("Asignar", (dialog, which) -> {
            String numEmpleado = etNumEmpleado.getText().toString().trim();
            String clave = etClave.getText().toString().trim();

            if (numEmpleado.isEmpty() || clave.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            docenteManager.open();
            boolean exito = docenteManager.asignarMateria(numEmpleado, clave);
            docenteManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Materia asignada con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error al asignar (¿Ya está asignada o no existe el docente/materia?)", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoAsignarAlumnoDocente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Asignar Alumno a Docente");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNumEmpleado = new EditText(requireContext());
        etNumEmpleado.setHint("Número de Empleado del Docente");
        layout.addView(etNumEmpleado);

        final EditText etNumControl = new EditText(requireContext());
        etNumControl.setHint("Número de Control del Alumno");
        layout.addView(etNumControl);

        builder.setView(layout);

        builder.setPositiveButton("Asignar", (dialog, which) -> {
            String numEmpleado = etNumEmpleado.getText().toString().trim();
            String numControl = etNumControl.getText().toString().trim();

            if (numEmpleado.isEmpty() || numControl.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            docenteManager.open();
            boolean exito = docenteManager.asignarAlumno(numEmpleado, numControl);
            docenteManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Alumno asignado al docente con éxito", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Error al asignar (¿Ya está asignado o no existe el docente/alumno?)", Toast.LENGTH_LONG).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
