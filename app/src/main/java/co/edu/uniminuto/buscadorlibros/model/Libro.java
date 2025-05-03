package co.edu.uniminuto.buscadorlibros.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Libro   {
    private String id;
    @SerializedName("volumeInfo")
    private InfoVolumen infoVolumen;

    // Constructor completo
    public Libro(String id, InfoVolumen infoVolumen) {
        this.id = id;
        this.infoVolumen = infoVolumen;
    }

    // Constructor vacío para frameworks
    public Libro() {
    }

    // Getters y setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InfoVolumen getInfoVolumen() {
        return infoVolumen;
    }

    public void setInfoVolumen(InfoVolumen infoVolumen) {
        this.infoVolumen = infoVolumen;
    }

    /**
     * Clase interna para mapear los detalles del volumen del libro
     */
    public static class InfoVolumen implements Serializable {
        @SerializedName("title")
        private String titulo;
        @SerializedName("authors")
        private String[] autores;
        @SerializedName("description")
        private String descripcion;
        @SerializedName("imageLinks")
        private ImagenEnlaces imagenEnlaces;

        public InfoVolumen(String titulo, String[] autores, String descripcion, ImagenEnlaces imagenEnlaces) {
            this.titulo = titulo;
            this.autores = autores;
            this.descripcion = descripcion;
            this.imagenEnlaces = imagenEnlaces;
        }

        public InfoVolumen() {
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String[] getAutores() {
            return autores;
        }

        public void setAutores(String[] autores) {
            this.autores = autores;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public ImagenEnlaces getImagenEnlaces() {
            return imagenEnlaces;
        }

        public void setImagenEnlaces(ImagenEnlaces imagenEnlaces) {
            this.imagenEnlaces = imagenEnlaces;
        }
    }

    /**
     * Clase interna para mapear los enlaces de imagen del volumen
     */
    public static class ImagenEnlaces implements Serializable {
        @SerializedName("thumbnail")
        private String miniatura;
        @SerializedName("medium")
        private String mediana;
        @SerializedName("large")
        private String grande;

        public ImagenEnlaces(String miniatura, String mediana, String grande) {
            this.miniatura = miniatura;
            this.mediana    = mediana;
            this.grande     = grande;
        }

        public ImagenEnlaces() {
        }

        public String getMiniatura() {
            return miniatura;
        }

        public void setMiniatura(String miniatura) {
            this.miniatura = miniatura;
        }

        public String getMediana() {
            return mediana;
        }

        public void setMediana(String mediana) {
            this.mediana = mediana;
        }

        public String getGrande() {
            return grande;
        }

        public void setGrande(String grande) {
            this.grande = grande;
        }
    }

}
