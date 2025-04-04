package com.example.aplicativomovil.entidades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativomovil.R;

import java.util.List;

/**
 * Adaptador personalizado para mostrar una lista de nombres de amigos
 * en un RecyclerView.
 */
public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder> {

    /** Lista de nombres de amigos a mostrar */
    private List<String> amigosList;

    /**
     * Constructor del adaptador.
     *
     * @param amigosList Lista de nombres de amigos.
     */
    public AmigosAdapter(List<String> amigosList) {
        this.amigosList = amigosList;
    }

    /**
     * Crea nuevas vistas (invocado por el LayoutManager).
     *
     * @param parent   El ViewGroup padre al que se adjuntará la nueva vista.
     * @param viewType Tipo de vista (no utilizado en este caso).
     * @return Una nueva instancia de AmigoViewHolder.
     */
    @Override
    public AmigoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigo, parent, false);
        return new AmigoViewHolder(view);
    }

    /**
     * Asocia los datos de un elemento de la lista con una vista.
     *
     * @param holder   ViewHolder que se va a actualizar.
     * @param position Posición del elemento dentro del dataset.
     */
    @Override
    public void onBindViewHolder(AmigoViewHolder holder, int position) {
        holder.nombreAmigo.setText(amigosList.get(position));
    }

    /**
     * Devuelve el tamaño de la lista de amigos.
     *
     * @return Número de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return amigosList.size();
    }
    /**
     * ViewHolder que representa un solo ítem (amigo) en el RecyclerView.
     */
    public class AmigoViewHolder extends RecyclerView.ViewHolder {
        /** TextView donde se muestra el nombre del amigo */
        TextView nombreAmigo;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView Vista del ítem individual.
         */
        public AmigoViewHolder(View itemView) {
            super(itemView);
            nombreAmigo = itemView.findViewById(R.id.nombreAmigo);
        }
    }
}



