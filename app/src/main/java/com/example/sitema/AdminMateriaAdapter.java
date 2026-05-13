package com.example.sitema;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminMateriaAdapter extends RecyclerView.Adapter<AdminMateriaAdapter.VH> {

    private ArrayList<Materia> lista;

    public AdminMateriaAdapter(ArrayList<Materia> lista) {
        this.lista = lista;
    }

    public void actualizar(ArrayList<Materia> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_materia, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Materia m = lista.get(position);
        holder.tvClave.setText(m.getClaveMateria());
        holder.tvNombre.setText(m.getNombreMateria());
        holder.tvCreditos.setText("—");   // Materia no tiene créditos aún
        int bgColor = (position % 2 == 0) ? 0xFFF9F9F9 : 0xFFFFFFFF;
        holder.itemView.setBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvClave, tvNombre, tvCreditos;

        VH(@NonNull View itemView) {
            super(itemView);
            tvClave    = itemView.findViewById(R.id.tv_admin_materia_clave);
            tvNombre   = itemView.findViewById(R.id.tv_admin_materia_nombre);
            tvCreditos = itemView.findViewById(R.id.tv_admin_materia_creditos);
        }
    }
}
