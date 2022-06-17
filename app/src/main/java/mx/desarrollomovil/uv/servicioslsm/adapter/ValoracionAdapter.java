package mx.desarrollomovil.uv.servicioslsm.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mx.desarrollomovil.uv.servicioslsm.R;
import mx.desarrollomovil.uv.servicioslsm.model.Calificacion;

public class ValoracionAdapter extends RecyclerView.Adapter<ValoracionAdapter.ValoracionesAdapterViewHolder> {
    List<Calificacion> valoraciones;
    Context context;

    public ValoracionAdapter(Context context, List<Calificacion> valoraciones){
        this.context = context;
        this.valoraciones = valoraciones;
    }

    @NonNull
    @Override
    public ValoracionesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewvaloraciones, parent, false);
        ValoracionAdapter.ValoracionesAdapterViewHolder holder = new ValoracionAdapter.ValoracionesAdapterViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ValoracionesAdapterViewHolder holder, int position) {
        Calificacion valoracion = valoraciones.get(position);
        holder.rbValoracion.setRating(valoracion.getRating());
        holder.vvValoracion.setVideoURI(Uri.parse(valoracion.getVideoUrl()));

    }

    @Override
    public int getItemCount() {
        return valoraciones.size();
    }

    public static class ValoracionesAdapterViewHolder extends RecyclerView.ViewHolder {
        List<Calificacion> valoraciones;
        Context context;
        RatingBar rbValoracion;
        VideoView vvValoracion;


        public ValoracionesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            this.context = itemView.getContext();
            rbValoracion = (RatingBar) itemView.findViewById(R.id.rbValoracion);
            vvValoracion = (VideoView) itemView.findViewById(R.id.vvValoracionSordo);

        }
    }
}
