package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracionPerfil extends AppCompatActivity {

    String nombre, correo, contraseña, ciudad, tipo, userId;
    FirebaseAuth auth;
    FirebaseDatabase db;
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

        imageView = (CircleImageView) findViewById(R.id.imgPerfil);
        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        tvNombreUsuario = (TextView) findViewById(R.id.tvNombreId);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Terminando perfil...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
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
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 45);
            }
        });

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(selectedImage != null){
                    StorageReference reference = storage.getReference().child("users").child(auth.getCurrentUser().getUid());
                    reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        userId = auth.getCurrentUser().getUid();

                                        Usuario addNewUser = new Usuario(userId, nombre, correo, contraseña, ciudad, imageUrl, tipo);

                                        db.getReference().child("users").child(userId).setValue(addNewUser)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                        Toast.makeText(ConfiguracionPerfil.this, "creacion perfil exitoso", Toast.LENGTH_SHORT).show();
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }else{
                    Usuario addNewUser = new Usuario(userId, nombre, correo, contraseña, ciudad, "sin imagen", tipo);

                    db.getReference().child("users").child(userId).setValue(addNewUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    Toast.makeText(ConfiguracionPerfil.this, "creacion perfil exitoso", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data != null){
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

        }
    }
}