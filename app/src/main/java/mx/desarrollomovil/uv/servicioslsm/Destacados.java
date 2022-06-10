package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.desarrollomovil.uv.servicioslsm.adapter.ServiceAdapter;
import mx.desarrollomovil.uv.servicioslsm.model.Servicio;

public class Destacados extends AppCompatActivity {
    //ArrayList<Usuario> perfiles;
    //ArrayList<Servicio> perfilesServicios;
    //private RecyclerView listaServicios;
    String userId;
    private Toolbar miToolbar;
    CircleImageView civ;
    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser usuario;
    MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destacados);
        //this.setTitle("Interpretes Destacados");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        userId = usuario.getUid();

        miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        civ = (CircleImageView) findViewById(R.id.imgPerfil);

        recyclerView = (RecyclerView) findViewById(R.id.rvPerfil);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //DocumentReference documentReference = db.collection("users").whereEqualTo("tipo", );




    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        serviceAdapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mCrearServicio:
                Intent intent = new Intent(this, CrearServicio.class);
                startActivity(intent);
                break;

            case R.id.mEditarServicio:

                break;

            case R.id.mVerValoraciones:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void inicializarAdapter(){
        UsuarioAdapter adapter = new UsuarioAdapter(perfilesServicios);
        listaServicios.setAdapter(adapter);
    }

    public void inicializarListaServicios(){
        perfiles = new ArrayList<Usuario>();
        perfilesServicios = new ArrayList<Servicio>();

        perfiles.add(new Usuario(
                1, "Manuel", "Sosa", "mani@sosa", "12345", "Xalapa", R.drawable.ic_perfil, 'I'
        ));
        perfiles.add(new Usuario(
                2, "Benito", "Sousa", "beni@sousa", "12345", "Xalapa", R.drawable.ic_perfil, 'I'
        ));

        perfilesServicios.add(new Servicio(
                1, 5, "Educacion", "2233445566", R.drawable.ic_launcher_background, 1
        ));*/
}