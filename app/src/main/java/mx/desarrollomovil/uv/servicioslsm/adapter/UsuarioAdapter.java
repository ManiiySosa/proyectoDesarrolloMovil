/*package mx.desarrollomovil.uv.servicioslsm.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mx.desarrollomovil.uv.servicioslsm.PerfilInterprete;
import mx.desarrollomovil.uv.servicioslsm.R;
import mx.desarrollomovil.uv.servicioslsm.model.Usuario;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.ViewHolder> {
    private List<Usuario> perfiles;
    private LayoutInflater layoutInflater;
    private Context context;
    private String tipo;

    public UsuarioAdapter(List<Usuario> perfiles, Context context, String tipo){
        this.layoutInflater = LayoutInflater.from(context);
        this.perfiles=perfiles;
        this.context=context;
        this.tipo=tipo;
    }

    @NonNull
    @Override
    public UsuarioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardviewperfil, null);
        return new UsuarioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(perfiles.get(position));
        holder.setList(perfiles, position);
        holder.setOnClickListener();


    }

    @Override
    public int getItemCount() {
        return perfiles.size();
    }

    public void setItems(List<Usuario> perfiles){
        this.perfiles=perfiles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgPerfil;
        TextView tvCvNombrePerfil, tvCvCiudad, tvCvCorreo;
        Button btnCvVerPerfil;
        List<Usuario> perfiles;
        Context context;
        String id, tipo;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.context = itemView.getContext();
            imgPerfil = itemView.findViewById(R.id.imgCvPerfil);
            tvCvNombrePerfil = itemView.findViewById(R.id.tvCvNombrePerfil);
            tvCvCiudad = itemView.findViewById(R.id.tvCvCiudad);
            tvCvCorreo = itemView.findViewById(R.id.tvCvCorreo);
            btnCvVerPerfil = itemView.findViewById(R.id.btnCvVerPerfil);

            if(tipo.equals("i")){
                itemView.setVisibility(View.VISIBLE);
            }else if(tipo.equals("s")){
                itemView.setVisibility(View.INVISIBLE);
            }


        }

        void setOnClickListener(){
            btnCvVerPerfil.setOnClickListener(this);
        }

        void bindData(final Usuario item){
            imgPerfil.setImageURI(Uri.parse(item.getImgUrl()));
            tvCvNombrePerfil.setText(item.getNombre());
            tvCvCiudad.setText(item.getCiudad());
            tvCvCorreo.setText(item.getCorreo());
            this.id = item.getId();

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, PerfilInterprete.class);
            intent.putExtra("id", id);
            context.startActivity(intent);
        }

        public void setList(List<Usuario> perfiles, int position) {
            this.perfiles = perfiles;
            this.position= position;
        }
    }
}
*/