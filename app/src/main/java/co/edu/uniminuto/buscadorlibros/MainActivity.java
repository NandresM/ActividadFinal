package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import co.edu.uniminuto.buscadorlibros.adaptadores.AdaptadorLibros;
import co.edu.uniminuto.buscadorlibros.api.ClienteApi;
import co.edu.uniminuto.buscadorlibros.api.ServicioGoogleBooks;
import co.edu.uniminuto.buscadorlibros.model.Libro;
import co.edu.uniminuto.buscadorlibros.model.RespuestaLibros;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText editTextBusqueda;
    private Button botonBuscar;
    private RecyclerView recyclerViewLibros;
    private AdaptadorLibros adaptadorLibros;
    private Button botonEstanteria;
    private Button botonLaCerrarSesion;

    // Reemplaza con tu propia API key de Google Books
    private static final String API_KEY = "AIzaSyB0cKtzZyHmtCRwto1mhfrei3oWly6N9Qw";
    private static final int MAX_RESULTADOS = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
       initViews();

        // Configurar RecyclerView
        recyclerViewLibros.setLayoutManager(new LinearLayoutManager(this));
        adaptadorLibros = new AdaptadorLibros(this, new AdaptadorLibros.OnLibroClickListener() {
            @Override
            public void onLibroClick(Libro libro) {
                abrirDetalleLibro(libro);
            }
        });
        recyclerViewLibros.setAdapter(adaptadorLibros);

        // Configurar el botón de búsqueda
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarBusqueda();
            }
        });

        // Configurar el botón de Mi Estantería




        // Configurar la acción de búsqueda en el EditText
        editTextBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    realizarBusqueda();
                    return true;
                }
                return false;
            }
        });
        botonEstanteria.setOnClickListener(v -> irAMiEstanteria());
    }

    private void initViews() {
        editTextBusqueda = findViewById(R.id.editTextBusqueda);
        botonBuscar = findViewById(R.id.botonBuscar);
        recyclerViewLibros = findViewById(R.id.recyclerViewLibros);
        botonEstanteria = findViewById(R.id.botonEstanteria);
        botonLaCerrarSesion = findViewById(R.id.botonLaCerrarSesion);

        botonLaCerrarSesion.setOnClickListener(this::cerrarSesion);
    }

    private void realizarBusqueda() {
        String consulta = editTextBusqueda.getText().toString().trim();

        if (consulta.isEmpty()) {
            Toast.makeText(this, "Ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
            return;
        }

        ServicioGoogleBooks servicio = ClienteApi.getCliente().create(ServicioGoogleBooks.class);
        Call<RespuestaLibros> llamada = servicio.buscarLibros(consulta, MAX_RESULTADOS, API_KEY);

        llamada.enqueue(new Callback<RespuestaLibros>() {
            @Override
            public void onResponse(Call<RespuestaLibros> call, Response<RespuestaLibros> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RespuestaLibros respuesta = response.body();
                    if (respuesta.getLibros() != null && !respuesta.getLibros().isEmpty()) {
                        adaptadorLibros.actualizarDatos(respuesta.getLibros());
                    } else {
                        mostrarMensajeError("No se encontraron libros");
                    }
                } else {
                    mostrarMensajeError("Error al obtener resultados");
                }
            }

            @Override
            public void onFailure(Call<RespuestaLibros> call, Throwable t) {
                mostrarMensajeError("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarMensajeError(String mensaje) {
        Toast.makeText(MainActivity.this, mensaje, Toast.LENGTH_SHORT).show();
    }

    private void abrirDetalleLibro(Libro libro) {
        Intent intent = new Intent(this, DetalleLibroActivity.class);
        // Enviar el ID del libro
        intent.putExtra("LIBRO_ID", libro.getId());

        // Enviar información básica para evitar otra llamada API si es necesario
        if (libro.getInfoVolumen() != null) {
            intent.putExtra("LIBRO_TITULO", libro.getInfoVolumen().getTitulo());

            if (libro.getInfoVolumen().getAutores() != null && libro.getInfoVolumen().getAutores().length > 0) {
                intent.putExtra("LIBRO_AUTOR", libro.getInfoVolumen().getAutores()[0]);
            }

            if (libro.getInfoVolumen().getDescripcion() != null) {
                intent.putExtra("LIBRO_DESCRIPCION", libro.getInfoVolumen().getDescripcion());
            }

            if (libro.getInfoVolumen().getImagenEnlaces() != null &&
                    libro.getInfoVolumen().getImagenEnlaces().getMiniatura() != null) {
                intent.putExtra("LIBRO_IMAGEN", libro.getInfoVolumen().getImagenEnlaces().getMiniatura());
            }
        }

        startActivity(intent);
    }

    private void irAMiEstanteria() {
        startActivity(new Intent(MainActivity.this, EstanteriaActivity.class));
        finish();
    }
    private void cerrarSesion(View view) {


        Intent intent = new Intent(this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}