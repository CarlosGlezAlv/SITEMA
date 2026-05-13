package com.example.sitema;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminDocenteAdapter extends RecyclerView.Adapter<AdminDocenteAdapter.VH> {

    private ArrayList<Docente> lista;

    public AdminDocenteAdapter(ArrayList<Docente> lista) {
        this.lista = lista;
    }

    public void actualizar(ArrayList<Docente> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_docente, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Docente d = lista.get(position);
        holder.tvEmpleado.setText(d.getNumEmpleado());
        holder.tvNombre.setText(d.getNombre());
        holder.tvDir.setText(d.getDireccion() != null && !d.getDireccion().isEmpty()
                ? d.getDireccion() : "—");
        int bgColor = (position % 2 == 0) ? 0xFFF9F9F9 : 0xFFFFFFFF;
        holder.itemView.setBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvEmpleado, tvNombre, tvDir;

        VH(@NonNull View itemView) {
            super(itemView);
            tvEmpleado = itemView.findViewById(R.id.tv_admin_docente_empleado);
            tvNombre   = itemView.findViewById(R.id.tv_admin_docente_nombre);
            tvDir      = itemView.findViewById(R.id.tv_admin_docente_dir);
        }
    }
}
