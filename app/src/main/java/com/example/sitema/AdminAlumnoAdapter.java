package com.example.sitema;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminAlumnoAdapter extends RecyclerView.Adapter<AdminAlumnoAdapter.VH> {

    private ArrayList<Alumno> lista;

    public AdminAlumnoAdapter(ArrayList<Alumno> lista) {
        this.lista = lista;
    }

    public void actualizar(ArrayList<Alumno> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_alumno, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Alumno a = lista.get(position);
        holder.tvControl.setText(a.getNumControl());
        holder.tvNombre.setText(a.getNombre());
        holder.tvTelefono.setText(a.getTelefono() != null && !a.getTelefono().isEmpty()
                ? a.getTelefono() : "—");
        // Alternar fondo para filas pares/impares
        int bgColor = (position % 2 == 0) ? 0xFFF9F9F9 : 0xFFFFFFFF;
        holder.itemView.setBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvControl, tvNombre, tvTelefono;

        VH(@NonNull View itemView) {
            super(itemView);
            tvControl   = itemView.findViewById(R.id.tv_admin_alumno_control);
            tvNombre    = itemView.findViewById(R.id.tv_admin_alumno_nombre);
            tvTelefono  = itemView.findViewById(R.id.tv_admin_alumno_telefono);
        }
    }
}
