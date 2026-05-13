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

public class AdminMateriasFragment extends Fragment {

    private MateriaManager materiaManager;
    private AdminMateriaAdapter adapterMaterias;
    private TextView tvSinMaterias;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_materias, container, false);

        materiaManager = new MateriaManager(requireContext());
        
        tvSinMaterias = view.findViewById(R.id.tv_admin_sin_materias);
        adapterMaterias = new AdminMateriaAdapter(new ArrayList<>());
        RecyclerView rvMaterias = view.findViewById(R.id.rv_admin_materias);
        rvMaterias.setAdapter(adapterMaterias);

        MaterialButton btnAltaMateria = view.findViewById(R.id.btn_alta_materia);
        MaterialButton btnEditarMateria = view.findViewById(R.id.btn_editar_materia);
        MaterialButton btnEliminarMateria = view.findViewById(R.id.btn_eliminar_materia);

        btnAltaMateria.setOnClickListener(v -> mostrarDialogoAltaMateria());
        btnEditarMateria.setOnClickListener(v -> mostrarDialogoBuscarParaEditarMateria());
        btnEliminarMateria.setOnClickListener(v -> mostrarDialogoEliminarMateria());

        cargarTabla();

        return view;
    }

    private void cargarTabla() {
        materiaManager.open();
        ArrayList<Materia> listaM = materiaManager.obtenerTodasLasMaterias();
        materiaManager.close();
        adapterMaterias.actualizar(listaM);
        tvSinMaterias.setVisibility(listaM.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void mostrarDialogoAltaMateria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Alta de Materia");

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etClave = new EditText(requireContext());
        etClave.setHint("Clave de Materia");
        layout.addView(etClave);

        final EditText etNombre = new EditText(requireContext());
        etNombre.setHint("Nombre de la Materia");
        layout.addView(etNombre);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String clave = etClave.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();

            if (clave.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(requireContext(), "La clave y nombre son obligatorios", Toast.LENGTH_SHORT).show();
                return;
            }

            materiaManager.open();
            boolean exito = materiaManager.insertarMateria(new Materia(clave, nombre));
            materiaManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Materia guardada con éxito", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "Error al guardar (La clave ya existe)", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void mostrarDialogoBuscarParaEditarMateria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar Materia");
        builder.setMessage("Ingrese la Clave de la Materia a editar:");

        final EditText input = new EditText(requireContext());
        input.setHint("Clave de materia");
        LinearLayout layout = new LinearLayout(requireContext());
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
                Toast.makeText(requireContext(), "Materia no encontrada", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEdicionMateria(Materia materia) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Modificando a: " + materia.getClaveMateria());

        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText etNombre = new EditText(requireContext());
        etNombre.setText(materia.getNombreMateria());
        layout.addView(etNombre);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            materia.setNombreMateria(etNombre.getText().toString().trim());

            materiaManager.open();
            boolean exito = materiaManager.actualizarMateria(materia);
            materiaManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Materia actualizada", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminarMateria() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Eliminar Materia");
        builder.setMessage("Ingrese la Clave de la Materia a eliminar:");

        final EditText input = new EditText(requireContext());
        input.setHint("Clave de materia");
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setPadding(50, 20, 50, 10);
        layout.addView(input);
        builder.setView(layout);

        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            String clave = input.getText().toString().trim();
            materiaManager.open();
            boolean exito = materiaManager.eliminarMateria(clave);
            materiaManager.close();

            if (exito) {
                Toast.makeText(requireContext(), "Materia eliminada correctamente", Toast.LENGTH_SHORT).show();
                cargarTabla();
            } else {
                Toast.makeText(requireContext(), "No se encontró la materia o no se pudo eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }
}
