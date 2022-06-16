package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import mx.desarrollomovil.uv.servicioslsm.model.Calificacion;

public class CalificarInterprete extends AppCompatActivity {
    public static final int PICK_VIDEO = 1;
    String nombre, userId, ratingId;
    Uri videoUri;
    TextView tvNombreInterpreteCal;
    RatingBar rbCalidad;
    VideoView vvSordo;
    Button btnInsertarVideoSordo, btnEnviarCalificacion;
    MediaController mediaController;
    FirebaseStorage storage;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar_interprete);

        this.setTitle("Calificar interprete");

        nombre = getIntent().getStringExtra("nombreCompleto");
        userId = getIntent().getStringExtra("userId");

        progressDialog = new ProgressDialog(this);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();

        tvNombreInterpreteCal = (TextView) findViewById(R.id.tvNombreInterpreteCal);
        rbCalidad = (RatingBar) findViewById(R.id.rbCalidad);
        vvSordo = (VideoView) findViewById(R.id.vvSordo);
        btnInsertarVideoSordo = (Button) findViewById(R.id.btnInsertarVideoSordo);
        btnEnviarCalificacion = (Button) findViewById(R.id.btnEnviarCalificacion);

        tvNombreInterpreteCal.setText("Calificar a " + nombre);

        mediaController = new MediaController(this);

        btnInsertarVideoSordo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

        btnEnviarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("calificando interprete...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                float rating = rbCalidad.getRating();

                if(videoUri != null){
                    ratingId = db.collection("calificacion").document().getId();
                    StorageReference filePathVideo = storage.getReference().child("videos").child(videoUri.getLastPathSegment()+"."+getExt(videoUri));
                    DocumentReference documentReference = db.collection("calificacion").document(ratingId);
                    filePathVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String filePathVideo = uri.toString();

                            Map<String, Object> calificacion = new HashMap<>();
                            calificacion.put("ratingId", ratingId);
                            calificacion.put("rating", rating);
                            calificacion.put("videoUrl", filePathVideo);
                            calificacion.put("userId", userId);

                            Calificacion cal = new Calificacion(ratingId, rating, filePathVideo, userId);

                            documentReference.set(cal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();

                                    Intent intent = new Intent(getBaseContext(), Destacados.class);
                                    startActivity(intent);
                                    Toast.makeText(CalificarInterprete.this, "Interprete calificado", Toast.LENGTH_SHORT).show();

                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CalificarInterprete.this, "No se pudo calificar al interprete", Toast.LENGTH_SHORT).show();
                                    Log.w("TAG", "Error adding document" + e.getMessage());
                                }
                            });
                        }
                    });

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO || requestCode == RESULT_OK || data != null || data.getData() != null) {
            Uri uri = data.getData();
            StorageReference filePathVideo = storage.getReference().child("videos").child(uri.getLastPathSegment()+"."+getExt(uri));
            filePathVideo.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Toast.makeText(getBaseContext(), "video seleccionado", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) ;

            mediaController.setAnchorView(vvSordo);
            vvSordo.setVideoURI(data.getData());
            vvSordo.requestFocus();
            vvSordo.setMediaController(mediaController);
            //vvInterprete.start();
            btnInsertarVideoSordo.setText("cambiar video");
            videoUri = data.getData();
        }
    }

    //obtener extension del video
    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}