package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.desarrollomovil.uv.servicioslsm.adapter.ValoracionAdapter;
import mx.desarrollomovil.uv.servicioslsm.model.Calificacion;

public class Valoraciones extends AppCompatActivity {

    ValoracionAdapter valoracionAdapter;
    private List<Calificacion> valoraciones;
    RecyclerView rvValoraciones;
    CircleImageView cvimgSordo;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser usuario;
    FirebaseAuth auth;
    TextView tvNombreSordo;
    String imgSordo, userId, userIdValoracion;
    RatingBar rbValoracion;
    VideoView vvValoracionSordo;
    MediaController mediaController;
    Uri videoUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valoraciones);

        this.setTitle("valoraciones");

        Toolbar miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        userId = usuario.getUid();

        rvValoraciones = (RecyclerView) findViewById(R.id.rvValoraciones);

        cvimgSordo = (CircleImageView) findViewById(R.id.cvimgSordo);
        tvNombreSordo = (TextView)  findViewById(R.id.tvNombreSordo);
        rbValoracion = (RatingBar) findViewById(R.id.rbValoracion);
        vvValoracionSordo = (VideoView) findViewById(R.id.vvValoracionSordo);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(vvValoracionSordo);

        rvValoraciones.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
        valoraciones = new ArrayList<>();
        valoracionAdapter = new ValoracionAdapter(getBaseContext(), valoraciones);
        rvValoraciones.setAdapter(valoracionAdapter);
        rvValoraciones.setHasFixedSize(true);

       /* db.collection("calificacion").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Calificacion cal = document.toObject(Calificacion.class);
                        userIdValoracion = cal.getUserId();
                        }
                    }
                }
        });*/

        /*DocumentReference dr = db.collection("users").document(userId);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    if(userId.equals(userIdValoracion)){
                        imgSordo = ds.getString("imgUrl");
                    }
                    Glide.with(getBaseContext()).load(imgSordo).into(cvimgSordo);
                }

               // cvimgSordo.setImageURI(Uri.parse(imgSordo));
            }

        });*/

        this.getValoraciones();


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }

    public void getValoraciones(){
        db.collection("calificacion").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        Calificacion valoracion = document.toObject(Calificacion.class);
                        valoraciones.add(valoracion);
                       /* videoUri = Uri.parse(valoracion.getVideoUrl());
                        mediaController.setAnchorView(vvValoracionSordo);
                        vvValoracionSordo.setVideoURI(videoUri);
                        vvValoracionSordo.requestFocus();
                        vvValoracionSordo.setMediaController(mediaController);*/
                        valoracionAdapter.notifyDataSetChanged();

                    }

                    mediaController.setAnchorView(vvValoracionSordo);

                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.w("TAG", e.getMessage());
            }
        });

    }



























}