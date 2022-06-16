package mx.desarrollomovil.uv.servicioslsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import mx.desarrollomovil.uv.servicioslsm.PerfilInterprete;
import mx.desarrollomovil.uv.servicioslsm.R;
import mx.desarrollomovil.uv.servicioslsm.model.Usuario;

public class UsuarioAdapter2 extends RecyclerView.Adapter<UsuarioAdapter2.UsuarioAdapterViewHolder> {

    Context context;
    List<Usuario> perfiles;
    List<Usuario> listaPerfiles;
    String tipo, userId;

    public UsuarioAdapter2(Context context, List<Usuario> perfiles, String tipo){
        this.context = context;
        this.perfiles=perfiles;
        this.tipo = tipo;
        listaPerfiles = new ArrayList<>();
        listaPerfiles.addAll(perfiles);
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
        holder.btnCvVerPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PerfilInterprete.class);
                intent.putExtra("userId", perfil.getId());
                intent.putExtra("imgUrl",perfil.getImgUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    public void filtrado(String txtBuscar){
        int longitud = txtBuscar.length();
        if(longitud == 0){
            perfiles.clear();
            perfiles.addAll(listaPerfiles);
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Usuario> coleccion = perfiles.stream().filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                perfiles.clear();
                perfiles.addAll(coleccion);
            }else{
                for(Usuario u: listaPerfiles){
                    if(u.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())){
                        perfiles.add(u);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return perfiles.size();
    }

    public static class UsuarioAdapterViewHolder extends RecyclerView.ViewHolder{
        Context context;
        List<Usuario> perfiles;
        ImageView imgCvPerfil;
        TextView tvCvNombrePerfil, tvCvCiudad, tvCvCorreo;
        String tipo;
        Button btnCvVerPerfil;

        public UsuarioAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            this.context = itemView.getContext();
            imgCvPerfil = (ImageView) itemView.findViewById(R.id.imgCvPerfil);
            tvCvNombrePerfil = (TextView) itemView.findViewById(R.id.tvCvNombrePerfil);
            tvCvCiudad = (TextView) itemView.findViewById(R.id.tvCvCiudad);
            tvCvCorreo = (TextView) itemView.findViewById(R.id.tvCvCorreo);
            btnCvVerPerfil = (Button) itemView.findViewById(R.id.btnCvVerPerfil);

            if(tipo == "I"){
                itemView.setVisibility(View.VISIBLE);
            }else if (tipo == "S"){
                itemView.setVisibility(View.GONE);
            }

        }
    }

}
