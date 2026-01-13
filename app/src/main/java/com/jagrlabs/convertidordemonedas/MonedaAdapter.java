package com.jagrlabs.convertidordemonedas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MonedaAdapter extends RecyclerView.Adapter<MonedaAdapter.MonedaViewHolder> {

    // Usamos la lista de la clase Moneda que creamos antes
    private final List<Moneda> listaMonedas;

    public MonedaAdapter(List<Moneda> listaMonedas) {
        this.listaMonedas = listaMonedas;
    }

    @NonNull
    @Override
    public MonedaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el diseño de la tarjeta que creamos (item_moneda.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_moneda, parent, false);
        return new MonedaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonedaViewHolder holder, int position) {
        // Obtenemos la moneda de la posición actual
        Moneda moneda = listaMonedas.get(position);

        // Ponemos los datos en los TextViews
        holder.tvCodigo.setText(moneda.getCodigo());
        holder.tvNombre.setText(moneda.getNombre());
    }

    @Override
    public int getItemCount() {
        return listaMonedas.size();
    }

    // El ViewHolder es el que "sujeta" las vistas del diseño item_moneda.xml
    public static class MonedaViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo, tvNombre;

        public MonedaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvNombre = itemView.findViewById(R.id.tvNombre);
        }
    }
}