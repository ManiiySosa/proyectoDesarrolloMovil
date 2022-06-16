package mx.desarrollomovil.uv.servicioslsm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.desarrollomovil.uv.servicioslsm.adapter.UsuarioAdapter2;
import mx.desarrollomovil.uv.servicioslsm.model.Usuario;

public class Destacados extends AppCompatActivity {
    //ArrayList<Usuario> perfiles;
    //ArrayList<Servicio> perfilesServicios;
    //private RecyclerView listaServicios;
    private List<Usuario> perfiles;
    String userId, tipo, imgUrlPerfilUsuario;
   // Uri imgUrlPerfilUsuario;
    private Toolbar miToolbar;
    CircleImageView civ;
    RecyclerView rvPerfil;
    UsuarioAdapter2 usuarioAdapter;
    FirebaseStorage storage;
    FirebaseFirestore db;
    FirebaseUser usuario;
    FirebaseAuth auth;
    Menu mymenu;
    MenuItem mCrearServicio, mEditarServicio, mVerValoraciones, mCerrarSesion;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destacados);
        //this.setTitle("Interpretes Destacados");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser();
        userId = usuario.getUid();

        miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        civ = (CircleImageView) findViewById(R.id.imgPerfilUsuario);

        rvPerfil = (RecyclerView) findViewById(R.id.rvPerfil);
        searchView = (SearchView) findViewById(R.id.svInterpretes);

        //DocumentReference documentReference = db.collection("users").whereEqualTo("tipo", );
        //usuarioAdapter = new UsuarioAdapter(perfiles, this, tipo);
        rvPerfil.setLayoutManager(new LinearLayoutManager(getBaseContext(), RecyclerView.VERTICAL, false));
        perfiles = new ArrayList<>();
        usuarioAdapter = new UsuarioAdapter2(getBaseContext(),perfiles,tipo);
        rvPerfil.setAdapter(usuarioAdapter);
        rvPerfil.setHasFixedSize(true);

        DocumentReference dr = db.collection("users").document(userId);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot ds = task.getResult();
                    imgUrlPerfilUsuario = ds.getString("imgUrl");
                }
                Glide.with(getBaseContext()).load(imgUrlPerfilUsuario).into(civ);
                //civ.setImageURI(Uri.parse(imgUrlPerfilUsuario));
            }

        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            civ.setTooltipText("click para editar perfl");
        }

        // Glide.with(getBaseContext()).load(imgUrlPerfilUsuario).into(civ);



        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

       this.getPerfiles();

       this.search_view();

    }

    public void init(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        mymenu = menu;
        mCrearServicio = menu.findItem(R.id.mCrearServicio);
        mEditarServicio = menu.findItem(R.id.mEditarServicio);
        mVerValoraciones = menu.findItem(R.id.mVerValoraciones);
        DocumentReference  dr = db.collection("users").document(userId);
        dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.getString("tipo").equals("S")){
                    mCrearServicio.setVisible(false);
                    mEditarServicio.setVisible(false);
                    mVerValoraciones.setVisible(false);
                }
            }
        });
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
                Intent intent2 = new Intent(getBaseContext(), EditarServicio.class);
                startActivity(intent2);

                break;
            case R.id.mVerValoraciones:
                Intent intent3 = new Intent(getBaseContext(), Valoraciones.class);
                startActivity(intent3);

                break;
            case  R.id.mCerrarSesion:
                auth.signOut();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPerfiles() {
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot document : task.getResult()){
                        Usuario perfil = document.toObject(Usuario.class);
                        if(perfil.getTipo().equals("I")){
                            perfiles.add(perfil);
                            usuarioAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", e.getMessage());
            }
        });

    }

    private void search_view(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usuarioAdapter.filtrado(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                usuarioAdapter.filtrado(newText);
                return false;
            }
        });
    }

}