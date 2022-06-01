package mx.desarrollomovil.uv.servicioslsm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.VideoView;

import java.util.ArrayList;

public class Destacados extends AppCompatActivity {

    ArrayList<Usuario> perfiles;
    ArrayList<Servicio> perfilesServicios;
    private RecyclerView listaServicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destacados);

        Toolbar miToolbar = (Toolbar) findViewById(R.id.miToolbar);
        setSupportActionBar(miToolbar);

        listaServicios = (RecyclerView) findViewById(R.id.rvPerfiles);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listaServicios.setLayoutManager(llm);
        inicializarListaServicios();
        inicializarAdapter();


    }

    public void inicializarAdapter(){
        UsuarioAdapter adapter = new UsuarioAdapter(perfilesServicios);
        listaServicios.setAdapter(adapter);
    }

    public void inicializarListaServicios(){
        perfiles = new ArrayList<Usuario>();
        perfilesServicios = new ArrayList<Servicio>();

        /*perfiles.add(new Usuario(
                1, "Manuel", "Sosa", "mani@sosa", "12345", "Xalapa", R.drawable.ic_perfil, 'I'
        ));
        perfiles.add(new Usuario(
                2, "Benito", "Sousa", "beni@sousa", "12345", "Xalapa", R.drawable.ic_perfil, 'I'
        ));

        perfilesServicios.add(new Servicio(
                1, 5, "Educacion", "2233445566", R.drawable.ic_launcher_background, 1
        ));*/
    }
}