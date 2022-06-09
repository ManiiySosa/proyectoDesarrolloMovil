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
import android.widget.ProgressBar;
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


public class CrearServicio extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;

    String userId, serviceId, añosExperiencia, areaEspecialidad, telefono ;
    Uri videoUri;
    private EditText etAñosExperiencia, etAreaEspecialidad, etTelefono;
    private Button btnInsertarVideo, btnCrearServicio;
    private VideoView vvInterprete;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser usuario;
    MediaController mediaController;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_servicio);

        Toolbar miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        userId = usuario.getUid();

        progressDialog = new ProgressDialog(this);
        progressBar = new ProgressBar(this);

        etAñosExperiencia = (EditText) findViewById(R.id.etAñosExperiencia);
        etAreaEspecialidad = (EditText) findViewById(R.id.etAreaEspecialidad);
        etTelefono = (EditText) findViewById(R.id.etTelefono);
        btnInsertarVideo = (Button) findViewById(R.id.btnInsertarVideo);
        btnCrearServicio = (Button) findViewById(R.id.btnCrearServicio);
        vvInterprete = (VideoView) findViewById(R.id.vvInterprete);

        mediaController = new MediaController(this);
        mediaController.setAnchorView(vvInterprete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnInsertarVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(intent, PICK_VIDEO);
            }
        });

        btnCrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 añosExperiencia = etAñosExperiencia.getText().toString();
                 areaEspecialidad = etAreaEspecialidad.getText().toString().trim();
                 telefono = etTelefono.getText().toString().trim();

                if(TextUtils.isEmpty(añosExperiencia)){
                   etAñosExperiencia.setError("años");
                   etAñosExperiencia.requestFocus();
                }else if (TextUtils.isEmpty(areaEspecialidad)) {
                    etAñosExperiencia.setError("Ingresa area especialidad");
                    etAñosExperiencia.requestFocus();
                }else if(TextUtils.isEmpty(telefono)){
                    etTelefono.setError("Ingresa tu telefono");
                    etTelefono.requestFocus();
                }
                    subirDatosServicio();

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

    private void subirDatosServicio() {

        progressDialog.setMessage("creando servicio...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        if(videoUri != null){
            serviceId = db.collection("servicio").document().getId();
            StorageReference filePathVideo = storage.getReference().child("videos").child(videoUri.getLastPathSegment()+"."+getExt(videoUri));
            DocumentReference documentReference = db.collection("servicio").document(serviceId);
            filePathVideo.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String filePathVideo = uri.toString();

                    Map<String, Object> service = new HashMap<>();
                    service.put("serviceId", serviceId);
                    service.put("añosEsperiencia", añosExperiencia);
                    service.put("areaEspecialidad", areaEspecialidad);
                    service.put("telefono", telefono);
                    service.put("videoUri", filePathVideo);

                    Servicio servicio = new Servicio(serviceId, añosExperiencia, areaEspecialidad, telefono, filePathVideo, userId);

                    documentReference.set(servicio).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            progressDialog.dismiss();

                            Intent intent = new Intent(CrearServicio.this, Destacados.class);
                            intent.putExtra("añosExperiencia", añosExperiencia);
                            intent.putExtra("areaEspecialidad", areaEspecialidad);
                            intent.putExtra("telefono", telefono);
                            intent.putExtra("videoUri", videoUri);
                            startActivity(intent);

                            finish();

                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CrearServicio.this, "No se pudo crear el servicio", Toast.LENGTH_SHORT).show();
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });

                }
            });
        }
    }
}