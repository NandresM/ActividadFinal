package co.edu.uniminuto.buscadorlibros.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniminuto.buscadorlibros.R;
import co.edu.uniminuto.buscadorlibros.model.Libro;

public class AdaptadorLibros extends RecyclerView.Adapter<AdaptadorLibros.ViewHolderLibro> {
    private List<Libro> listaLibros;
    private Context contexto;
    private OnLibroClickListener onLibroClickListener;

    // Interface para el manejo de clicks
    public interface OnLibroClickListener {
        void onLibroClick(Libro libro);
    }

    public AdaptadorLibros(Context contexto, OnLibroClickListener listener) {
        this.contexto = contexto;
        this.listaLibros = new ArrayList<>();
        this.onLibroClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolderLibro onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false);
        return new ViewHolderLibro(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderLibro holder, int position) {
        Libro libro = listaLibros.get(position);

        if (libro.getInfoVolumen() != null) {
            // Establecer título
            holder.textoTitulo.setText(libro.getInfoVolumen().getTitulo());

            // Establecer autor
            if (libro.getInfoVolumen().getAutores() != null && libro.getInfoVolumen().getAutores().length > 0) {
                holder.textoAutor.setText(libro.getInfoVolumen().getAutores()[0]);
            } else {
                holder.textoAutor.setText("Autor desconocido");
            }

            // Establecer descripción
            String descripcion = libro.getInfoVolumen().getDescripcion();
            if (descripcion != null && !descripcion.isEmpty()) {
                holder.textoDescripcion.setText(descripcion);
            } else {
                holder.textoDescripcion.setText("Sin descripción disponible");
            }

            // Cargar imagen
            if (libro.getInfoVolumen().getImagenEnlaces() != null &&
                    libro.getInfoVolumen().getImagenEnlaces().getMiniatura() != null) {
                String urlImagen = libro.getInfoVolumen().getImagenEnlaces().getMiniatura();
                // Asegúrate de que la URL use HTTPS
                if (urlImagen.startsWith("http:")) {
                    urlImagen = urlImagen.replace("http:", "https:");
                }

                Glide.with(contexto)
                        .load(urlImagen)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(holder.imagenLibro);
            } else {
                holder.imagenLibro.setImageResource(R.drawable.ic_launcher_background);
            }

            // Configurar listener de clic
            holder.bindClickListener(libro, onLibroClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return listaLibros.size();
    }

    public void actualizarDatos(List<Libro> nuevaLista) {
        this.listaLibros.clear();
        if (nuevaLista != null) {
            this.listaLibros.addAll(nuevaLista);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolderLibro extends RecyclerView.ViewHolder {
        public ImageView imagenLibro;
        public TextView textoTitulo;
        public TextView textoAutor;
        public TextView textoDescripcion;

        public ViewHolderLibro(@NonNull View itemView) {
            super(itemView);
            imagenLibro = itemView.findViewById(R.id.imagenLibro);
            textoTitulo = itemView.findViewById(R.id.textoTitulo);
            textoAutor = itemView.findViewById(R.id.textoAutor);
            textoDescripcion = itemView.findViewById(R.id.textoDescripcion);
        }

        public void bindClickListener(final Libro libro, final OnLibroClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onLibroClick(libro);
                }
            });
        }
    }
}
