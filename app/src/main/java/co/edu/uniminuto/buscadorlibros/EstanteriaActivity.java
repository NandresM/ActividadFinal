package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estanteria);

        // 1) Inicializar vistas
        recyclerViewEstanteria = findViewById(R.id.recyclerViewEstanteria);
        textoEstanteriaVacia  = findViewById(R.id.textoEstanteriaVacia);
        botonBiblioteca       = findViewById(R.id.botonBiblioteca);  // Inicializado correctamente

        // 2) Configurar RecyclerView
        estanteria = Estanteria.getInstance(this);
        recyclerViewEstanteria.setLayoutManager(new LinearLayoutManager(this));
        adaptadorLibros = new AdaptadorLibros(this, libro -> abrirDetalleLibro(libro));
        recyclerViewEstanteria.setAdapter(adaptadorLibros);

        // 3) Listener del botón


        // 4) Carga inicial de datos
        cargarLibrosGuardados();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Solo refrescar datos
        cargarLibrosGuardados();
        botonBiblioteca.setOnClickListener (this::abrirBiblioteca);
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
}