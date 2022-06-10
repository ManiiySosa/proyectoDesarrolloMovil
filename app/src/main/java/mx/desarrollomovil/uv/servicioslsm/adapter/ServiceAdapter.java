package mx.desarrollomovil.uv.servicioslsm.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import mx.desarrollomovil.uv.servicioslsm.R;
import mx.desarrollomovil.uv.servicioslsm.model.Servicio;

public class ServiceAdapter extends FirestoreRecyclerAdapter<Servicio, ServiceAdapter.ViewHolder>{
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ServiceAdapter(@NonNull FirestoreRecyclerOptions<Servicio> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Servicio model) {


        //holder.tvNombrePerfil.setText(model.getUserId());
        holder.vvInterprete.setVideoURI(Uri.parse(model.getVideoUrl()));

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewperfil, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView vvInterprete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            vvInterprete = itemView.findViewById(R.id.vvInterprete);

        }
    }
}
