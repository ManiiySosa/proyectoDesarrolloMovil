package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;


public class CrearServicio extends AppCompatActivity {

    private static final int PICK_VIDEO = 1;

    String userId;
    Uri videoUri;
    private EditText etAñosExperiencia, etAreaEspecialidad, etTelefono;
    private Button btnInsertarVideo, btnCrearServicio;
    private VideoView vvInterprete;
    ProgressDialog progressDialog;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser usuario;
    MediaController mediaController;

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

        etAreaEspecialidad = (EditText) findViewById(R.id.etAñosExperiencia);
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
               subirVideo();
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
            videoUri = data.getData();
            vvInterprete.setVideoURI(videoUri);
            vvInterprete.requestFocus();
            vvInterprete.setMediaController(mediaController);
            vvInterprete.start();
        }
    }

    private String getExt(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void subirVideo() {


    }


}