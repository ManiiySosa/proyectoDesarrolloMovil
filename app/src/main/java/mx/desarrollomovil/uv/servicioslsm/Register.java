package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {

    private static final int GALLERY_INTENT = 1;

    private CheckBox cbSordo, cbInterprete;
    private EditText etNombre, etCorreo, etContraseña, etCiudad;
    private Button btnInsertarFoto, btnCrearCuenta;
    private ProgressDialog pd;

    private char tipo;
    private String userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cbSordo = (CheckBox) findViewById(R.id.cbSordo);
        cbInterprete = (CheckBox) findViewById(R.id.cbInterprete);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etCorreo = (EditText) findViewById(R.id.etCorreo);
        etContraseña = (EditText) findViewById(R.id.etContraseña);
        etCiudad = (EditText)  findViewById(R.id.etCiudad);
        btnInsertarFoto = (Button)  findViewById(R.id.btnInsertarFoto);
        btnCrearCuenta = (Button) findViewById(R.id.btnCrearCuenta);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        pd = new ProgressDialog(this);
        
        btnInsertarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });
        
        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    @Override
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
    }

    private void registerUser() {
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String contraseña = etContraseña.getText().toString().trim();
        String ciudad = etCiudad.getText().toString().trim();

    }

}