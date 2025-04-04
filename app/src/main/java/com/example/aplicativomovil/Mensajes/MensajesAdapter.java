package com.example.aplicativomovil.Mensajes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativomovil.R;

import java.util.List;

/**
 * Adaptador personalizado para mostrar mensajes en un RecyclerView.
 * Cada ítem representa un mensaje recibido con su contenido, remitente y hora de envío.
 */
public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.ViewHolder> {

    /** Lista de mensajes a mostrar */
    private List<Mensaje> mensajesList;

    /**
     * Constructor que recibe la lista de mensajes.
     *
     * @param mensajesList Lista de objetos {@link Mensaje}.
     */
    public MensajesAdapter(List<Mensaje> mensajesList) {
        this.mensajesList = mensajesList;
    }

    /**
     * Crea nuevas vistas (invocado por el LayoutManager).
     *
     * @param parent   El ViewGroup padre.
     * @param viewType Tipo de vista (no se usa en este caso).
     * @return Una nueva instancia de ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mensaje, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asocia los datos de un mensaje con una vista.
     *
     * @param holder   ViewHolder que se va a actualizar.
     * @param position Posición del elemento dentro del dataset.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mensaje mensaje = mensajesList.get(position);
        holder.textoMensaje.setText(mensaje.getMensaje());
        holder.textoRemitente.setText(mensaje.getDe());
        holder.textoTimestamp.setText(String.valueOf(mensaje.getTimestamp()));
    }

    /**
     * @return Cantidad total de elementos en la lista.
     */
    @Override
    public int getItemCount() {
        return mensajesList.size();
    }

    /**
     * Clase interna que representa el ViewHolder.
     * Contiene las vistas que se van a llenar con los datos del mensaje.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** Texto del contenido del mensaje */
        TextView textoMensaje;

        /** Texto con el remitente del mensaje */
        TextView textoRemitente;

        /** Texto con la marca de tiempo del mensaje */
        TextView textoTimestamp;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView Vista del ítem del RecyclerView.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            textoMensaje = itemView.findViewById(R.id.textoMensaje);
            textoRemitente = itemView.findViewById(R.id.textoRemitente);
            textoTimestamp = itemView.findViewById(R.id.textoTimestamp);
        }
    }
}

