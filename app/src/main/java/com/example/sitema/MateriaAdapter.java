package com.example.sitema;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MateriaAdapter extends RecyclerView.Adapter<MateriaAdapter.ViewHolder> {
    private List<MateriaCursada> materias;

    public MateriaAdapter(List<MateriaCursada> materias) {
        this.materias = materias;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_materia_calificacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MateriaCursada materia = materias.get(position);
        holder.tvMateriaNombre.setText(materia.getNombreMateria());
        holder.tvCalificacion.setText(String.valueOf(materia.getCalificacion()));
    }

    @Override
    public int getItemCount() {
        return materias == null ? 0 : materias.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMateriaNombre, tvCalificacion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMateriaNombre = itemView.findViewById(R.id.tv_materia_nombre);
            tvCalificacion = itemView.findViewById(R.id.tv_calificacion);
        }
    }
}
