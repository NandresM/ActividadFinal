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
    private Button botonFavoritos;

    private SharedPreferences preferenciasCalif;
    private SharedPreferences preferenciasLogin;
    private static final String PREF_CALIFICACIONES = "calificaciones_libros";
    private static final String PREF_LOGIN = "login_preferences";

    private Estanteria estanteria;
    private Libro libro;

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
       // botonFavoritos            = findViewById(R.id.favoritos);

        // Preferencias
        preferenciasCalif = getSharedPreferences(PREF_CALIFICACIONES, Context.MODE_PRIVATE);
        preferenciasLogin = getSharedPreferences(PREF_LOGIN, Context.MODE_PRIVATE);

        // Singleton Estantería
        estanteria = Estanteria.getInstance(this);

        // Leer ID de libro
        String libroId = getIntent().getStringExtra("LIBRO_ID");
        if (libroId != null) {
            libro = estanteria.obtenerLibro(libroId);
            if (libro != null && libro.getInfoVolumen() != null) {
                // Mostrar datos
                textoDetalleTitulo.setText(libro.getInfoVolumen().getTitulo());
                String[] autores = libro.getInfoVolumen().getAutores();
                if (autores != null && autores.length > 0) {
                    textoDetalleAutor.setText(autores[0]);
                }
                textoDetalleDescripcion.setText(libro.getInfoVolumen().getDescripcion());

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
        botonFavoritos.setOnClickListener(v -> {
            // TODO: Implementar favoritos
            Toast.makeText(this, "Función de favoritos aún no implementada", Toast.LENGTH_SHORT).show();
        });
        botonCerrarSesion.setOnClickListener(v -> {
            // Limpiar sesión y volver al login
            preferenciasLogin.edit().clear().apply();
            Intent intent = new Intent(DetalleLibroActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
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
