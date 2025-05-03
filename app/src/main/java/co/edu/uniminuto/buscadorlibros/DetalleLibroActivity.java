package co.edu.uniminuto.buscadorlibros;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

import co.edu.uniminuto.buscadorlibros.model.Estanteria;
import co.edu.uniminuto.buscadorlibros.model.Libro;

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

    private SharedPreferences preferenciasCalif;
    private SharedPreferences preferenciasLogin;
    private static final String PREF_CALIFICACIONES = "calificaciones_libros";
    private static final String PREF_LOGIN = "login_preferences";

    private Estanteria estanteria;
    private Libro libro;
    private String libroId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);

        // Inicializar vistas
        imagenDetalleLibro        = findViewById(R.id.imagenDetalleLibro);
        textoDetalleTitulo        = findViewById(R.id.textoDetalleTitulo);
        textoDetalleAutor         = findViewById(R.id.textoDetalleAutor);
        textoDetalleDescripcion   = findViewById(R.id.textoDetalleDescripcion);
        ratingBarLibro            = findViewById(R.id.ratingBarLibro);
        botonGuardarCalificacion  = findViewById(R.id.botonGuardarCalificacion);
        botonGuardarLibro         = findViewById(R.id.botonGuardarLibro);
        botonEstanteria           = findViewById(R.id.botonEstanteria);
        botonCerrarSesion         = findViewById(R.id.botonLaCerrarSesion);

        // Preferencias
        preferenciasCalif = getSharedPreferences(PREF_CALIFICACIONES, Context.MODE_PRIVATE);
        preferenciasLogin = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);

        // Singleton Estantería
        estanteria = Estanteria.getInstance(this);

        // Obtener ID del libro del Intent
        libroId = getIntent().getStringExtra("LIBRO_ID");
        if (libroId != null) {
            // CAMBIO PRINCIPAL: Primero intentar obtener de estantería
            libro = estanteria.obtenerLibro(libroId);

            // Si no está en estantería, crear un libro temporal con los extras del Intent
            if (libro == null) {
                libro = crearLibroDesdeIntent(getIntent());
            }

            // Mostrar información del libro
            if (libro != null && libro.getInfoVolumen() != null) {
                mostrarDatosLibro();

                // Cargar calificación previa
                float calif = preferenciasCalif.getFloat(libroId, 0f);
                if (calif > 0f) {
                    ratingBarLibro.setRating(calif);
                }

                // Actualizar botón guardar libro
                updateGuardarLibroButton();
            }
        }

        // Listeners
        botonGuardarCalificacion.setOnClickListener(v -> guardarCalificacion());
        botonGuardarLibro.setOnClickListener(v -> toggleGuardarLibro());
        botonEstanteria.setOnClickListener(v -> irAMiEstanteria());
    }

    // NUEVO MÉTODO: Crea un objeto Libro a partir de los extras del Intent
    private Libro crearLibroDesdeIntent(Intent intent) {
        if (intent == null) return null;

        // Verifica si hay información básica en el intent
        String titulo = intent.getStringExtra("LIBRO_TITULO");
        String autor = intent.getStringExtra("LIBRO_AUTOR");
        String descripcion = intent.getStringExtra("LIBRO_DESCRIPCION");
        String imagenUrl = intent.getStringExtra("LIBRO_IMAGEN");

        // Si no hay información básica, no podemos crear un libro
        if (libroId == null) return null;

        // Crear un nuevo objeto Libro con los datos disponibles
        Libro nuevoLibro = new Libro();
        nuevoLibro.setId(libroId);

        // Crear y configurar la información del volumen
        Libro.InfoVolumen infoVolumen = new Libro.InfoVolumen();
        if (infoVolumen instanceof Serializable) {
            // Configurar título
            try {
                java.lang.reflect.Field tituloField = infoVolumen.getClass().getDeclaredField("titulo");
                tituloField.setAccessible(true);
                tituloField.set(infoVolumen, titulo);
            } catch (Exception e) {
                Log.e("DetalleLibro", "Error al establecer título: " + e.getMessage());
            }

            // Configurar autores
            if (autor != null) {
                try {
                    java.lang.reflect.Field autoresField = infoVolumen.getClass().getDeclaredField("autores");
                    autoresField.setAccessible(true);
                    autoresField.set(infoVolumen, new String[]{autor});
                } catch (Exception e) {
                    Log.e("DetalleLibro", "Error al establecer autores: " + e.getMessage());
                }
            }

            // Configurar descripción
            if (descripcion != null) {
                try {
                    java.lang.reflect.Field descripcionField = infoVolumen.getClass().getDeclaredField("descripcion");
                    descripcionField.setAccessible(true);
                    descripcionField.set(infoVolumen, descripcion);
                } catch (Exception e) {
                    Log.e("DetalleLibro", "Error al establecer descripción: " + e.getMessage());
                }
            }

            // Configurar enlaces de imagen
            if (imagenUrl != null) {
                try {
                    Libro.ImagenEnlaces imagenEnlaces = new Libro.ImagenEnlaces();
                    java.lang.reflect.Field miniaturaField = imagenEnlaces.getClass().getDeclaredField("miniatura");
                    miniaturaField.setAccessible(true);
                    miniaturaField.set(imagenEnlaces, imagenUrl);

                    java.lang.reflect.Field imagenEnlacesField = infoVolumen.getClass().getDeclaredField("imagenEnlaces");
                    imagenEnlacesField.setAccessible(true);
                    imagenEnlacesField.set(infoVolumen, imagenEnlaces);
                } catch (Exception e) {
                    Log.e("DetalleLibro", "Error al establecer imagen: " + e.getMessage());
                }
            }

            // Establecer la información del volumen en el libro
            try {
                java.lang.reflect.Field infoVolumenField = nuevoLibro.getClass().getDeclaredField("infoVolumen");
                infoVolumenField.setAccessible(true);
                infoVolumenField.set(nuevoLibro, infoVolumen);
            } catch (Exception e) {
                Log.e("DetalleLibro", "Error al establecer infoVolumen: " + e.getMessage());
            }
        }

        return nuevoLibro;
    }

    // MÉTODO EXTRAÍDO: Muestra los datos del libro en la interfaz
    private void mostrarDatosLibro() {
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
                Glide.with(this)
                        .load(urlImagen)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imagenDetalleLibro);
            }
        }
    }

    private void updateGuardarLibroButton() {
        if (libro == null) return;
        if (estanteria.contieneLibro(libro.getId())) {
            botonGuardarLibro.setText("Quitar de Mi Estantería");
        } else {
            botonGuardarLibro.setText("Guardar en Mi Estantería");
        }
    }

    private void toggleGuardarLibro() {
        if (libro == null) return;
        String id = libro.getId();
        if (estanteria.contieneLibro(id)) {
            boolean removed = estanteria.eliminarLibro(id);
            Toast.makeText(this, removed ? "Libro eliminado" : "Error al eliminar", Toast.LENGTH_SHORT).show();
        } else {
            boolean added = estanteria.agregarLibro(libro);
            Toast.makeText(this, added ? "Libro guardado" : "Error al guardar", Toast.LENGTH_SHORT).show();
        }
        updateGuardarLibroButton();
    }

    private void guardarCalificacion() {
        if (libro == null) return;
        float rating = ratingBarLibro.getRating();
        preferenciasCalif.edit().putFloat(libro.getId(), rating).apply();
        Toast.makeText(this, "Calificación guardada: " + rating, Toast.LENGTH_SHORT).show();
    }

    /**
     * Navega a Mi Estantería
     */
    private void irAMiEstanteria() {
        startActivity(new Intent(DetalleLibroActivity.this, EstanteriaActivity.class));
        finish();
    }
}
