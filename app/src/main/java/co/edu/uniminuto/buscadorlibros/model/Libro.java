package co.edu.uniminuto.buscadorlibros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Libro implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("id")
    private String id;

    @SerializedName("volumeInfo")
    private InfoVolumen infoVolumen;

    public Libro() {
    }

    public String getId() {
        return id;
    }

    public InfoVolumen getInfoVolumen() {
        return infoVolumen;
    }

    public void setId(String id) {
        this.id = id;

    }

    public static class InfoVolumen implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("title")
        private String titulo;

        @SerializedName("authors")
        private String[] autores;

        @SerializedName("description")
        private String descripcion;

        @SerializedName("imageLinks")
        private ImagenEnlaces imagenEnlaces;


        public String getTitulo() {
            return titulo;
        }

        public String[] getAutores() {
            return autores;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public ImagenEnlaces getImagenEnlaces() {
            return imagenEnlaces;
        }
    }

    public static class ImagenEnlaces implements Serializable {
        private static final long serialVersionUID = 1L;

        @SerializedName("thumbnail")
        private String miniatura;

        public String getMiniatura() {
            return miniatura;
        }
    }

}
