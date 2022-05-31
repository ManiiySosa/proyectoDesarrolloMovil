package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText etEmailLogin, etPasswordLogin;
    private Button btnEntrar, btnRegistrarse;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        etEmailLogin = (EditText) findViewById(R.id.etEmailLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);

        progressDialog = new ProgressDialog(this);

        btnEntrar.setOnClickListener(view -> {
            login();
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityRegister();
            }
        });
    }

    private void openActivityRegister() {
        Intent intent = new Intent(MainActivity.this, Register.class);
        startActivity(intent);
    }

    private void login(){
        String correo = etEmailLogin.getText().toString().trim();
        String contraseña = etPasswordLogin.getText().toString().trim();

        if(TextUtils.isEmpty(correo)){
            etEmailLogin.setError("Debes ingresar un correo");
            etEmailLogin.requestFocus();
        }else if(TextUtils.isEmpty(contraseña)){
            Toast.makeText(MainActivity.this, "Falta ingresar la contraseña", Toast.LENGTH_LONG);
            etPasswordLogin.requestFocus();
        }else{
            firebaseAuth.signInWithEmailAndPassword(correo, contraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       progressDialog.setMessage(getResources().getString(R.string.txt_progressdialog_login));
                       progressDialog.show();
                       Toast.makeText(getBaseContext(), getResources().getString(R.string.texto_toast_login), Toast.LENGTH_LONG);
                       startActivity(new Intent(MainActivity.this, Destacados.class));
                    }else{
                        Log.w("TAG", "Error inicio sesion", task.getException());
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
}