package mx.desarrollomovil.uv.servicioslsm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>{
    ArrayList<Usuario> perfiles;
    ArrayList<Servicio> servicios;

    public UsuarioAdapter(ArrayList<Servicio> servicios){
        this.servicios=servicios;
    }

    @Override
    public UsuarioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewperfil, parent,false);
        return new UsuarioViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UsuarioViewHolder usuarioViewHolder, int position) {
        Usuario perfil = perfiles.get(position);
        Servicio perfilServicio = servicios.get(position);
        usuarioViewHolder.tvNombrePerfil.setText(perfil.getNombre());
        usuarioViewHolder.vvPerfil.setImageResource(perfilServicio.getImg());

    }

    @Override
    public int getItemCount() {
        return servicios.size();
    }

    public static class UsuarioViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNombrePerfil;
        private ImageView vvPerfil;
        private Button btnVerPerfil;

        public UsuarioViewHolder(View itemView) {
            super(itemView);
            tvNombrePerfil = (TextView) itemView.findViewById(R.id.tvNombrePerfil);
            vvPerfil = (ImageView) itemView.findViewById(R.id.vvPerfil);
            btnVerPerfil = (Button) itemView.findViewById(R.id.btnVerPerfil);
        }
    }
}
