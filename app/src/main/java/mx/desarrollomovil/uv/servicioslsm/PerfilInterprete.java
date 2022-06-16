package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import mx.desarrollomovil.uv.servicioslsm.model.Servicio;
import mx.desarrollomovil.uv.servicioslsm.model.Usuario;

public class PerfilInterprete extends AppCompatActivity {

    String userId, imgUrl, videoUrl, serviceId, nombreCompleto, areaEspecialidad, añosExperienciaS, telefono;
    ImageView imgPerfilServicio;
    VideoView vvInterpreteServicio;
    TextView tvNombrePerfil, tvEspecialidad, añosExperiencia, tvTelefono;
    Button btnCalificarinterprete;
    FirebaseUser user;
    FirebaseFirestore db;
    Servicio servicio;
    Usuario usuario;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_interprete);
        this.setTitle("Perfil Interprete");

        Toolbar miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = FirebaseAuth.getInstance().getCurrentUser();
       // userId = user.getUid();

        db = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        imgUrl = getIntent().getStringExtra("imgUrl");

        imgPerfilServicio = (ImageView) findViewById(R.id.imgPerfilServicio);
        tvNombrePerfil = (TextView) findViewById(R.id.tvNombrePerfil);
        vvInterpreteServicio = (VideoView) findViewById(R.id.vvInterpreteServicio);
        tvEspecialidad = (TextView) findViewById(R.id.tvEspecialidad);
        añosExperiencia = (TextView) findViewById(R.id.AñosExperiencia);
        tvTelefono = (TextView) findViewById(R.id.tvTelefono);
        btnCalificarinterprete = (Button) findViewById(R.id.btnCalificarInterprete);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(vvInterpreteServicio);

        Glide.with(getApplicationContext()).load(imgUrl).into(imgPerfilServicio);




        this.obtenerDatosServicio();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void obtenerDatosServicio(){
        //CollectionReference servicioRef = db.collection("servicio").document();
        //serviceId = ser;
       // Query query = servicioRef.whereEqualTo("userId", userId);

        db.collection("servicio").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document : task.getResult()){
                        Servicio servicio = document.toObject(Servicio.class);
                        if(servicio.getUserId().equals(userId)){
                            tvNombrePerfil.setText(servicio.getNombreCompleto());
                            vvInterpreteServicio.setVideoURI(Uri.parse(servicio.getVideoUrl()));
                            tvEspecialidad.setText(servicio.getAreaEspecialidad());
                            añosExperiencia.setText(servicio.getAñosExperiencia());
                            tvTelefono.setText(servicio.getTelefono());

                            mediaController.setAnchorView(vvInterpreteServicio);
                            vvInterpreteServicio.setMediaController(mediaController);
                        }
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", e.getMessage());
            }
        });


       /* DocumentReference servicioRef = db.collection("servicio").document("dFpKufAjmTofgyfO6L8k");
        serviceId = servicioRef.getId();
        db.collection("servicio").document(serviceId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                  Servicio perfil = documentSnapshot.toObject(Servicio.class);
                  tvNombrePerfil.setText(perfil.getNombreCompleto());

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "error al cargar datos servicio " + e.getMessage());
            }
        });*/
    }
}