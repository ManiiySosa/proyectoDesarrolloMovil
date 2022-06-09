package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.desarrollomovil.uv.servicioslsm.model.Usuario;

public class ConfiguracionPerfil extends AppCompatActivity {

    private static final int GALLERY_INTENT = 1;
    String nombre, correo, contraseña, ciudad, tipo, userId;
    FirebaseAuth auth;
    //FirebaseDatabase db;
    FirebaseFirestore db;
    FirebaseStorage storage;
    Uri selectedImage;
    CircleImageView imageView;
    ProgressDialog progressDialog;
    Button btnContinuar;
    TextView tvNombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_perfil);

        this.setTitle("Elegir foto de perfil");

        imageView = (CircleImageView) findViewById(R.id.imgPerfil);
        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        tvNombreUsuario = (TextView) findViewById(R.id.tvNombreId);

        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        nombre = getIntent().getStringExtra("nombre");
        correo = getIntent().getStringExtra("correo");
        contraseña = getIntent().getStringExtra("contraseña");
        ciudad = getIntent().getStringExtra("ciudad");
        tipo = getIntent().getStringExtra("tipo");

        tvNombreUsuario.setText(nombre);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                // intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        // con firebastore database
        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("configurando perfil...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                if (selectedImage != null) {
                    userId = auth.getCurrentUser().getUid();
                    StorageReference imagePath = storage.getReference().child("fotos").child(selectedImage.getLastPathSegment());
                    DocumentReference documentReference = db.collection("users").document(userId);
                    imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imagePath = uri.toString();

                            Map<String, Object> user = new HashMap<>();
                            user.put("nombre", nombre);
                            user.put("correo", correo);
                            user.put("contraseña", contraseña);
                            user.put("ciudad", ciudad);
                            user.put("tipo", tipo);
                            user.put("imagenUrl", imagePath);

                            Usuario addNewUser = new Usuario(userId, nombre, correo, contraseña, ciudad, imagePath, tipo);

                            documentReference.set(addNewUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    Toast.makeText(ConfiguracionPerfil.this, "creacion perfil exitoso, inicia sesion", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error adding document", e);
                                }
                            });
                        }
                    });
                }else{
                    Usuario addNewUser = new Usuario(userId, nombre, correo, contraseña, ciudad, "sin imagen", tipo);
                    db.collection("users").document(userId).set(addNewUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    Toast.makeText(ConfiguracionPerfil.this, "creacion perfil exitoso, inicia sesion", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error adding document", e);
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData() != null){
            Uri uri = data.getData();
            StorageReference filePath = storage.getReference().child("fotos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ConfiguracionPerfil.this, "foto de perfil seleccionada", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            imageView.setImageURI(data.getData());
            selectedImage = data.getData();
        }
    }
}

//con realtime database
        /* if(data != null){
            if(data.getData() != null){
                Uri uri = data.getData(); //filepath
                FirebaseStorage fs = FirebaseStorage.getInstance();
                StorageReference sr = fs.getReference().child("users").child(userId);
                sr.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String filePath = uri.toString();
                                    HashMap<String, Object> obj = new HashMap<>();
                                    obj.put("img", filePath);
                                    db.getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .updateChildren(obj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                }
                            });
                        }
                    }
                });
                imageView.setImageURI(data.getData());
                selectedImage = data.getData();
            }

        } */