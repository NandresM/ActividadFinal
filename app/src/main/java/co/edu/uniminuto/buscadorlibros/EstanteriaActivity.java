package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.edu.uniminuto.buscadorlibros.adaptadores.AdaptadorLibros;
import co.edu.uniminuto.buscadorlibros.model.Estanteria;
import co.edu.uniminuto.buscadorlibros.model.Libro;

public class EstanteriaActivity extends AppCompatActivity {
    private RecyclerView recyclerViewEstanteria;
    private AdaptadorLibros adaptadorLibros;
    private TextView textoEstanteriaVacia;
    private Button botonBiblioteca;
    private Estanteria estanteria;
    private Button botonLaCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estanteria);

       initViews();

        // 2) Configurar RecyclerView
        estanteria = Estanteria.getInstance(this);
        recyclerViewEstanteria.setLayoutManager(new LinearLayoutManager(this));
        adaptadorLibros = new AdaptadorLibros(this, libro -> abrirDetalleLibro(libro));
        recyclerViewEstanteria.setAdapter(adaptadorLibros);

        // 3) Listener del botón


        // 4) Carga inicial de datos
        cargarLibrosGuardados();
    }
    private void initViews() {
        botonBiblioteca = findViewById(R.id.botonBiblioteca);
        botonLaCerrarSesion = findViewById(R.id.botonLaCerrarSesion);
        recyclerViewEstanteria = findViewById(R.id.recyclerViewEstanteria);
        textoEstanteriaVacia  = findViewById(R.id.textoEstanteriaVacia);


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Solo refrescar datos
        cargarLibrosGuardados();
        botonBiblioteca.setOnClickListener (this::abrirBiblioteca);
        botonLaCerrarSesion.setOnClickListener(this::cerrarSesion);

    }

    private void cargarLibrosGuardados() {
        List<Libro> librosGuardados = estanteria.obtenerTodosLosLibros();
        if (librosGuardados.isEmpty()) {
            recyclerViewEstanteria.setVisibility(View.GONE);
            textoEstanteriaVacia.setVisibility(View.VISIBLE);
        } else {
            recyclerViewEstanteria.setVisibility(View.VISIBLE);
            textoEstanteriaVacia.setVisibility(View.GONE);
            adaptadorLibros.actualizarDatos(librosGuardados);
        }
    }

    private void abrirDetalleLibro(Libro libro) {
        Intent intent = new Intent(this, DetalleLibroActivity.class);
        intent.putExtra("LIBRO_ID", libro.getId());

        startActivity(intent);
    }

    private void abrirBiblioteca(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void cerrarSesion(View view) {


        Intent intent = new Intent(this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}