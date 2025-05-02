package co.edu.uniminuto.buscadorlibros;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class DetalleLibroActivity extends Activity {
    private ImageView imagenDetalleLibro;
    private TextView textoDetalleTitulo;
    private TextView textoDetalleAutor;
    private TextView textoDetalleDescripcion;
    private RatingBar ratingBarLibro;
    private Button botonGuardarCalificacion;

    private String libroId;
    private SharedPreferences preferencias;
    private static final String PREF_CALIFICACIONES = "calificaciones_libros";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);

        // Inicializar vistas
        imagenDetalleLibro = findViewById(R.id.imagenDetalleLibro);
        textoDetalleTitulo = findViewById(R.id.textoDetalleTitulo);
        textoDetalleAutor = findViewById(R.id.textoDetalleAutor);
        textoDetalleDescripcion = findViewById(R.id.textoDetalleDescripcion);
        ratingBarLibro = findViewById(R.id.ratingBarLibro);
        botonGuardarCalificacion = findViewById(R.id.botonGuardarCalificacion);

        // Inicializar SharedPreferences para guardar calificaciones
        preferencias = getSharedPreferences(PREF_CALIFICACIONES, Context.MODE_PRIVATE);

        // Obtener datos del libro
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            libroId = extras.getString("LIBRO_ID", "");

            // Cargar datos en la interfaz
            textoDetalleTitulo.setText(extras.getString("LIBRO_TITULO", ""));
            textoDetalleAutor.setText(extras.getString("LIBRO_AUTOR", ""));
            textoDetalleDescripcion.setText(extras.getString("LIBRO_DESCRIPCION", "Sin descripción disponible"));

            // Cargar imagen
            String urlImagen = extras.getString("LIBRO_IMAGEN", "");
            if (!urlImagen.isEmpty()) {
                // Asegúrate de que la URL use HTTPS
                if (urlImagen.startsWith("http:")) {
                    urlImagen = urlImagen.replace("http:", "https:");
                }

                Glide.with(this)
                        .load(urlImagen)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(imagenDetalleLibro);
            }

            // Cargar calificación guardada si existe
            if (!libroId.isEmpty()) {
                float calificacionGuardada = preferencias.getFloat(libroId, 0);
                if (calificacionGuardada > 0) {
                    ratingBarLibro.setRating(calificacionGuardada);
                }
            }
        }

        // Configurar botón para guardar calificación
        botonGuardarCalificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCalificacion();
            }
        });
    }

    private void guardarCalificacion() {
        if (libroId != null && !libroId.isEmpty()) {
            float calificacion = ratingBarLibro.getRating();

            // Guardar en SharedPreferences
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putFloat(libroId, calificacion);
            editor.apply();

            Toast.makeText(this, "Calificación guardada: " + calificacion, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No se pudo guardar la calificación", Toast.LENGTH_SHORT).show();
        }
    }
}
