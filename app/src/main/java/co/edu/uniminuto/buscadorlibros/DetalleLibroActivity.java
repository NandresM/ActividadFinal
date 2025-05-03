package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import co.edu.uniminuto.buscadorlibros.model.Libro;
import co.edu.uniminuto.buscadorlibros.repository.CalificacionRepository;
import co.edu.uniminuto.buscadorlibros.repository.LibroRepository;

public class DetalleLibroActivity extends AppCompatActivity {
    private ImageView imagenDetalleLibro;
    private TextView textoDetalleTitulo;
    private TextView textoDetalleAutor;
    private TextView textoDetalleDescripcion;
    private RatingBar ratingBarLibro;
    private Button botonGuardarCalificacion;
    private Button botonGuardarLibro;
    private Button botonEstanteria;
    private Button botonCerrarSesion;

    private CalificacionRepository califRepo;
    private LibroRepository libroRepo;



    private Libro libro;
    private String libroId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);

       initViews();

        //repos

        califRepo = new CalificacionRepository(this);
        libroRepo = new LibroRepository(this);


        // Obtener ID del libro del Intent
        libroId = getIntent().getStringExtra("LIBRO_ID");
        if (libroId != null) {
            libro = libroRepo.getLibroById(libroId);
            if (libro == null) {
                libro = crearLibroDesdeIntent(getIntent());
            }
            if (libro != null && libro.getInfoVolumen() != null) {
                mostrarDatosLibro();

                // Cargar calificación
                Float calif = califRepo.getCalificacionByLibro(libroId);
                if (calif != null) {
                    ratingBarLibro.setRating(calif);
                }
                updateGuardarLibroButton();
            }
        }




    }

    private void initViews() {
        imagenDetalleLibro        = findViewById(R.id.imagenDetalleLibro);
        textoDetalleTitulo        = findViewById(R.id.textoDetalleTitulo);
        textoDetalleAutor         = findViewById(R.id.textoDetalleAutor);
        textoDetalleDescripcion   = findViewById(R.id.textoDetalleDescripcion);
        ratingBarLibro            = findViewById(R.id.ratingBarLibro);
        botonGuardarCalificacion  = findViewById(R.id.botonGuardarCalificacion);
        botonGuardarLibro         = findViewById(R.id.botonGuardarLibro);
        botonEstanteria           = findViewById(R.id.botonEstanteria);
        botonCerrarSesion         = findViewById(R.id.botonLaCerrarSesion);
        botonGuardarCalificacion.setOnClickListener(this::guardarCalificacion);
        botonGuardarLibro.setOnClickListener(this::toggleGuardarLibro);
        botonEstanteria.setOnClickListener(this::irAMiEstanteria);
        botonCerrarSesion.setOnClickListener(this::cerrarSesion);

    }


    private Libro crearLibroDesdeIntent(Intent intent) {
        if (intent == null) return null;

        // Verifica si hay información básica en el intent
        String id = intent.getStringExtra("LIBRO_ID");
        String titulo = intent.getStringExtra("LIBRO_TITULO");
        String autor = intent.getStringExtra("LIBRO_AUTOR");
        String descripcion = intent.getStringExtra("LIBRO_DESCRIPCION");
        String imagenUrl = intent.getStringExtra("LIBRO_IMAGEN");

        // Si no hay información básica, no podemos crear un libro
        if (id == null || titulo == null) return null;

        // Crear un nuevo objeto Libro con los datos disponibles
        Libro nuevoLibro = new Libro();
        nuevoLibro.setId(id);

        // Crear y configurar la información del volumen
        Libro.InfoVolumen infoVolumen = new Libro.InfoVolumen();
        infoVolumen.setTitulo(titulo);
        infoVolumen.setAutores(new String[]{autor});
        infoVolumen.setDescripcion(descripcion);

        Libro.ImagenEnlaces imagenEnlaces = new Libro.ImagenEnlaces();
        imagenEnlaces.setMiniatura(imagenUrl);
        infoVolumen.setImagenEnlaces(imagenEnlaces);

        nuevoLibro.setInfoVolumen(infoVolumen);
        return nuevoLibro;
    }


    private void mostrarDatosLibro() {
        if (libro == null || libro.getInfoVolumen() == null) return;

        textoDetalleTitulo.setText(libro.getInfoVolumen().getTitulo());

        String[] autores = libro.getInfoVolumen().getAutores();
        if (autores != null && autores.length > 0) {
            textoDetalleAutor.setText(autores[0]);
        }

        if (libro.getInfoVolumen().getDescripcion() != null) {
            textoDetalleDescripcion.setText(libro.getInfoVolumen().getDescripcion());
        }

        if (libro.getInfoVolumen().getImagenEnlaces() != null) {
            String urlImagen = libro.getInfoVolumen().getImagenEnlaces().getMiniatura();
            if (urlImagen != null && !urlImagen.isEmpty()) {
                if (urlImagen.startsWith("http:")) {
                    urlImagen = urlImagen.replace("http:", "https:");
                }
                try {
                    Glide.with(this)
                            .load(urlImagen)
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(imagenDetalleLibro);
                } catch (Exception e) {
                    Log.e("DetalleLibroActivity", "Error cargando imagen: " + e.getMessage());
                    // Establecer imagen por defecto en caso de error
                    imagenDetalleLibro.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        }
    }

    private void updateGuardarLibroButton() {
        if (libro == null) return;

        // Verificar si el libro existe en la base de datos
        Libro libroEnBD = libroRepo.getLibroById(libro.getId());
        if (libroEnBD != null) {
            botonGuardarLibro.setText("Quitar de Mi Estantería");
        } else {
            botonGuardarLibro.setText("Guardar en Mi Estantería");
        }
    }

    private void toggleGuardarLibro(View view) {
        if (libro == null) return;

        String id = libro.getId();
        // Verificar si el libro existe en la base de datos
        Libro libroEnBD = libroRepo.getLibroById(id);

        if (libroEnBD != null) {
            // Eliminar de SQLite
            int deleted = libroRepo.deleteLibro(id);
            if (deleted > 0) {
                Toast.makeText(this, "Libro eliminado de tu estantería", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al eliminar el libro", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Guardar en SQLite
            long resultado = libroRepo.insertLibro(libro);
            if (resultado != -1) {
                Toast.makeText(this, "Libro guardado en tu estantería", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar el libro", Toast.LENGTH_SHORT).show();
            }
        }
        updateGuardarLibroButton();
    }

    private void guardarCalificacion(View view) {
        if (libroId != null) {
            float rating = ratingBarLibro.getRating();
            // Guarda en SQLite
            boolean resultado = califRepo.upsertCalificacion(libroId, rating);
            if (resultado) {
                Toast.makeText(this, "Calificación guardada: " + rating, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error al guardar la calificación", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void irAMiEstanteria(View view) {
        startActivity(new Intent(DetalleLibroActivity.this, EstanteriaActivity.class));
        finish();
    }

    private void cerrarSesion(View view) {


        Intent intent = new Intent(this,Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show();
        startActivity(intent);

    }
}
