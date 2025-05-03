package co.edu.uniminuto.buscadorlibros.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniminuto.buscadorlibros.DetalleLibroActivity;

public class Estanteria extends AppCompatActivity {

    private static final String TAG = "Estanteria";
    private static final String PREF_ESTANTERIA = "estanteria_libros";
    private static final String KEY_LIBROS = "libros_guardados";

    private static Estanteria instancia;
    private final SharedPreferences preferencias;
    private final Gson gson;
    private List<Libro> librosGuardados;


    /**
     * Constructor privado para implementar el patrón Singleton
     * @param context Contexto de la aplicación
     */
    private Estanteria(Context context) {
        preferencias = context.getApplicationContext().getSharedPreferences(PREF_ESTANTERIA, Context.MODE_PRIVATE);
        gson = new Gson();
        cargarLibros();
    }

    /**
     * Obtiene la instancia única de Estanteria (patrón Singleton)
     * @param context Contexto de la aplicación
     * @return Instancia de Estanteria
     */
    public static synchronized Estanteria getInstance(android.content.Context context) {
        if (instancia == null) {
            instancia = new Estanteria(context);
        }
        return instancia;
    }

    /**
     * Carga los libros guardados desde SharedPreferences
     */
    private void cargarLibros() {
        String librosJson = preferencias.getString(KEY_LIBROS, "");
        if (librosJson.isEmpty()) {
            librosGuardados = new ArrayList<>();
        } else {
            try {
                Type tipo = new TypeToken<List<Libro>>() {}.getType();
                librosGuardados = gson.fromJson(librosJson, tipo);
                if (librosGuardados == null) {
                    librosGuardados = new ArrayList<>();
                }
            } catch (Exception e) {
                Log.e(TAG, "Error al cargar libros: " + e.getMessage());
                librosGuardados = new ArrayList<>();
            }
        }
    }

    /**
     * Guarda los libros en SharedPreferences
     */
    private void guardarLibros() {
        try {
            String librosJson = gson.toJson(librosGuardados);
            preferencias.edit().putString(KEY_LIBROS, librosJson).apply();
        } catch (Exception e) {
            Log.e(TAG, "Error al guardar libros: " + e.getMessage());
        }
    }

    /**
     * Agrega un libro a la estantería
     * @param libro Libro a agregar
     * @return true si se agregó correctamente, false si ya existía o hubo un error
     */
    public boolean agregarLibro(Libro libro) {
        if (libro == null || libro.getId() == null) {
            return false;
        }

        // Verificar si el libro ya existe
        if (contieneLibro(libro.getId())) {
            return false;
        }

        // Agregar el libro y guardar
        librosGuardados.add(libro);
        guardarLibros();
        return true;
    }

    /**
     * Elimina un libro de la estantería
     * @param libroId ID del libro a eliminar
     * @return true si se eliminó correctamente, false si no existía o hubo un error
     */
    public boolean eliminarLibro(String libroId) {
        if (libroId == null || libroId.isEmpty()) {
            return false;
        }

        boolean eliminado = false;
        for (int i = 0; i < librosGuardados.size(); i++) {
            if (librosGuardados.get(i).getId().equals(libroId)) {
                librosGuardados.remove(i);
                eliminado = true;
                break;
            }
        }

        if (eliminado) {
            guardarLibros();
        }

        return eliminado;
    }

    /**
     * Verifica si un libro ya está en la estantería
     * @param libroId ID del libro a verificar
     * @return true si el libro está en la estantería, false en caso contrario
     */
    public boolean contieneLibro(String libroId) {
        if (libroId == null || libroId.isEmpty()) {
            return false;
        }

        for (Libro libro : librosGuardados) {
            if (libro.getId().equals(libroId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Obtiene un libro específico de la estantería
     * @param libroId ID del libro a obtener
     * @return El libro si existe, null en caso contrario
     */
    public Libro obtenerLibro(String libroId) {
        if (libroId == null || libroId.isEmpty()) {
            return null;
        }

        for (Libro libro : librosGuardados) {
            if (libro.getId().equals(libroId)) {
                return libro;
            }
        }

        return null;
    }

    /**
     * Obtiene todos los libros guardados en la estantería
     * @return Lista de libros guardados
     */
    public List<Libro> obtenerTodosLosLibros() {
        return new ArrayList<>(librosGuardados);
    }

    /**
     * Obtiene la cantidad de libros en la estantería
     * @return Número de libros guardados
     */
    public int contarLibros() {
        return librosGuardados.size();
    }

    /**
     * Limpia todos los libros de la estantería
     */
    public void limpiarEstanteria() {
        librosGuardados.clear();
        guardarLibros();
    }
}