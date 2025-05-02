package co.edu.uniminuto.buscadorlibros.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaLibros {
    @SerializedName("items")
    private List<Libro> libros;

    @SerializedName("totalItems")
    private int totalItems;

    public List<Libro> getLibros() {
        return libros;
    }

    public int getTotalItems() {
        return totalItems;
    }
}
