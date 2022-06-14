package mx.desarrollomovil.uv.servicioslsm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import mx.desarrollomovil.uv.servicioslsm.R;
import mx.desarrollomovil.uv.servicioslsm.model.Usuario;

public class UsuarioAdapter2 extends RecyclerView.Adapter<UsuarioAdapter2.UsuarioAdapterViewHolder> {

    Context context;
    List<Usuario> perfiles;
    String tipo;

    public UsuarioAdapter2(Context context, List<Usuario> perfiles, String tipo){
        this.context = context;
        this.perfiles=perfiles;
        this.tipo = tipo;
    }

    @NonNull
    @Override
    public UsuarioAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewperfil, parent, false);
        UsuarioAdapterViewHolder holder = new UsuarioAdapterViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UsuarioAdapterViewHolder holder, int position) {
        Usuario perfil = perfiles.get(position);
        Glide.with(context).load(perfiles.get(position).getImgUrl()).into(holder.imgCvPerfil);
        holder.tvCvNombrePerfil.setText(perfil.getNombre());
        holder.tvCvCiudad.setText(perfil.getCiudad());
        holder.tvCvCorreo.setText(perfil.getCorreo());
    }

    @Override
    public int getItemCount() {
        return perfiles.size();
    }

    public static class UsuarioAdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCvPerfil;
        TextView tvCvNombrePerfil, tvCvCiudad, tvCvCorreo;
        String tipo;

        public UsuarioAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCvPerfil = (ImageView) itemView.findViewById(R.id.imgCvPerfil);
            tvCvNombrePerfil = (TextView) itemView.findViewById(R.id.tvCvNombrePerfil);
            tvCvCiudad = (TextView) itemView.findViewById(R.id.tvCvCiudad);
            tvCvCorreo = (TextView) itemView.findViewById(R.id.tvCvCorreo);

            if(tipo == "I"){
                itemView.setVisibility(View.VISIBLE);
            }else if (tipo == "S"){
                itemView.setVisibility(View.GONE);
            }

        }
    }
}
