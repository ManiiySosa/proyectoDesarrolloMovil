package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private static final int GALLERY_INTENT = 1;

    private RadioButton rbSordo, rbInterprete;
    private EditText etNombre, etCorreo, etContraseña, etCiudad;
    private Button btnInsertarFoto, btnSiguiente, btnIrLogin;
    private ProgressDialog pd;

    private char tipo;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
   // private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rbSordo = (RadioButton) findViewById(R.id.rbSordo);
        rbInterprete = (RadioButton) findViewById(R.id.rbInterprete);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etContraseña = (EditText) findViewById(R.id.etContraseña);
        etCiudad = (EditText)  findViewById(R.id.etCiudad);
       // btnInsertarFoto = (Button)  findViewById(R.id.btnInsertarFoto);
        btnSiguiente = (Button) findViewById(R.id.btnSiguiente);
        btnIrLogin = (Button) findViewById(R.id.btnIrLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
       // storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        
        /*btnInsertarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });*/
        
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        btnIrLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && requestCode == RESULT_OK){
            Uri uri = data.getData();
            StorageReference filePath = storageReference.child("fotos").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Register.this, "tu foto se ha subido correctamente", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }*/

    private void createUser() {
        String tipo =" ";
        if(rbSordo.isChecked()){
            tipo = "S";
        }
        if(rbInterprete.isChecked()){
            tipo = "I";
        }
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contraseña = etContraseña.getText().toString().trim();
        String ciudad = etCiudad.getText().toString().trim();

        if(TextUtils.isEmpty(tipo)){
            Toast.makeText(this, "Elige un tipo de cuenta", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(nombre)) {
            etNombre.setError("Ingresa tu nombre");
            etNombre.requestFocus();
        }else if (TextUtils.isEmpty(correo)){
            etCorreo.setError("Ingresa un correo");
            etCorreo.requestFocus();
        } else if (TextUtils.isEmpty(contraseña)) {
            etContraseña.setError("Ingresa una contraseña");
            etContraseña.requestFocus();
        }else if(TextUtils.isEmpty(ciudad)){
            etCiudad.setError("Ingresa tu ciudad");
            etCiudad.requestFocus();
        }else{

            String finalTipo = tipo;
            firebaseAuth.createUserWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        userId = firebaseAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userId);

                        Map<String, Object> user = new HashMap<>();
                        user.put("nombre", nombre);
                        user.put("correo", correo);
                        user.put("contraseña", contraseña);
                        user.put("ciudad", ciudad);
                        user.put("tipo", finalTipo);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSucces: Datos registrados " + userId);
                            }
                        });

                        Intent intent = new Intent(Register.this, ConfiguracionPerfil.class);
                        intent.putExtra("nombre", nombre);
                        intent.putExtra("correo", correo);
                        intent.putExtra("contraseña", contraseña);
                        intent.putExtra("ciudad", ciudad);
                        intent.putExtra("tipo", finalTipo);
                        startActivity(intent);

                        pd.setMessage("Espera un momento...");
                        pd.show();

                        Toast.makeText(Register.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Register.this, "Usuario  no registrado"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                        pd.dismiss();
                }
            });
        }

    }

}