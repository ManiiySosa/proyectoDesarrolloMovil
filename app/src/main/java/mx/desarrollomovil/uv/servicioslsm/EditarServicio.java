package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import mx.desarrollomovil.uv.servicioslsm.model.Servicio;

public class EditarServicio extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;
    Uri videoUri;
    EditText etNombreCompleto, etAñosExperiencia, etAreaEspecialidad, etTelefono;
    Button btnInsertarVideo, btnEditarServicio;
    VideoView vvInterprete;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser usuario;
    MediaController mediaController;
    String userId, serviceId, nombreCompleto, añosExperiencia, areaEspecialidad, telefono ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_servicio);

        this.setTitle("Editar servicio");

        Toolbar miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        userId = usuario.getUid();

        progressDialog = new ProgressDialog(this);

        etNombreCompleto = (EditText) findViewById(R.id.etNombreCompleto);
        etAñosExperiencia = (EditText) findViewById(R.id.etAñosExperiencia);
        etAreaEspecialidad = (EditText) findViewById(R.id.etAreaEspecialidad);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        btnInsertarVideo = (Button) findViewById(R.id.btnInsertarVideo);
        btnEditarServicio = (Button) findViewById(R.id.btnEditarServicio);
        vvInterprete = (VideoView) findViewById(R.id.vvInterprete);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(vvInterprete);

        btnInsertarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

        btnEditarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nombreCompleto = etNombreCompleto.getText().toString();
                añosExperiencia = etAñosExperiencia.getText().toString();
                areaEspecialidad = etAreaEspecialidad.getText().toString().trim();
                telefono = etTelefono.getText().toString().trim();

                editarServicio();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
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

            mediaController.setAnchorView(vvInterprete);
            vvInterprete.setVideoURI(data.getData());
            vvInterprete.requestFocus();
            vvInterprete.setMediaController(mediaController);
            //vvInterprete.start();
            btnInsertarVideo.setText("cambiar video");
            videoUri = data.getData();
        }
    }

    //obtener extension del video
    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void editarServicio() {
        progressDialog.setMessage("editando servicio...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (videoUri != null) {
            serviceId = db.collection("servicio").document().getId();
            StorageReference filePathVideo = storage.getReference().child("videos").child(videoUri.getLastPathSegment() + "." + getExt(videoUri));
            DocumentReference documentReference = db.collection("servicio").document(serviceId);
            filePathVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String filePathVideo = uri.toString();

                    Map<String, Object> service = new HashMap<>();
                    service.put("serviceId", serviceId);
                    service.put("nombreCompleto", nombreCompleto);
                    service.put("añosEsperiencia", añosExperiencia);
                    service.put("areaEspecialidad", areaEspecialidad);
                    service.put("telefono", telefono);
                    service.put("videoUri", filePathVideo);

                    Servicio servicio = new Servicio(serviceId, nombreCompleto, añosExperiencia, areaEspecialidad, telefono, filePathVideo, userId);

                    documentReference.set(servicio).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    progressDialog.dismiss();

                                    Intent intent = new Intent(EditarServicio.this, Destacados.class);
                                    startActivity(intent);
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditarServicio.this, "No se pudo crear el servicio", Toast.LENGTH_SHORT).show();
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });

                }
            });

        }
    }
}