package co.edu.uniminuto.buscadorlibros.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniminuto.buscadorlibros.model.Libro;

public class LibroRepository {
    private SQLiteDatabase db;


    public LibroRepository(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public long insertLibro(Libro libro) {
        ContentValues cv = new ContentValues();
        cv.put("id", libro.getId());
        cv.put("titulo", libro.getInfoVolumen().getTitulo());
        cv.put("autor", libro.getInfoVolumen().getAutores()[0]);
        cv.put("descripcion", libro.getInfoVolumen().getDescripcion());
        cv.put("imagenUrl", libro.getInfoVolumen().getImagenEnlaces().getMiniatura());
        return db.insert(DatabaseHelper.TABLE_LIBROS, null, cv);
    }

    public List<Libro> getAllLibros() {
        List<Libro> lista = new ArrayList<>();
        Cursor c = db.query(
                DatabaseHelper.TABLE_LIBROS,
                new String[]{ "id", "titulo", "autor", "descripcion", "imagenUrl" },
                null, null, null, null, null
        );
        while (c.moveToNext()) {
            Libro libro = new Libro();
            libro.setId(c.getString(c.getColumnIndexOrThrow("id")));

            // Crear InfoVolumen e ImagenEnlaces
            Libro.InfoVolumen info = new Libro.InfoVolumen();
            info.setTitulo(c.getString(c.getColumnIndexOrThrow("titulo")));
            info.setAutores(new String[]{ c.getString(c.getColumnIndexOrThrow("autor")) });
            info.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion")));

            Libro.ImagenEnlaces img = new Libro.ImagenEnlaces();
            img.setMiniatura(c.getString(c.getColumnIndexOrThrow("imagenUrl")));
            info.setImagenEnlaces(img);

            libro.setInfoVolumen(info);
            lista.add(libro);
        }
        c.close();
        return lista;
    }

    public Libro getLibroById(String id) {
        Cursor c = db.query(
                DatabaseHelper.TABLE_LIBROS,
                new String[]{ "id", "titulo", "autor", "descripcion", "imagenUrl" },
                "id = ?", new String[]{ id }, null, null, null
        );
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        Libro libro = new Libro();
        libro.setId(c.getString(c.getColumnIndexOrThrow("id")));

        Libro.InfoVolumen info = new Libro.InfoVolumen();
        info.setTitulo(c.getString(c.getColumnIndexOrThrow("titulo")));
        info.setAutores(new String[]{ c.getString(c.getColumnIndexOrThrow("autor")) });
        info.setDescripcion(c.getString(c.getColumnIndexOrThrow("descripcion")));
        Libro.ImagenEnlaces img = new Libro.ImagenEnlaces();
        img.setMiniatura(c.getString(c.getColumnIndexOrThrow("imagenUrl")));
        info.setImagenEnlaces(img);

        libro.setInfoVolumen(info);
        c.close();
        return libro;
    }

    public int updateLibro(Libro libro) {
        ContentValues cv = new ContentValues();
        cv.put("titulo", libro.getInfoVolumen().getTitulo());
        cv.put("autor", libro.getInfoVolumen().getAutores()[0]);
        cv.put("descripcion", libro.getInfoVolumen().getDescripcion());
        cv.put("imagenUrl", libro.getInfoVolumen().getImagenEnlaces().getMiniatura());
        return db.update(DatabaseHelper.TABLE_LIBROS, cv, "id= ?", new String[]{libro.getId()});
    }

    public int deleteLibro(String id) {
        return db.delete(DatabaseHelper.TABLE_LIBROS, "id= ?", new String[]{id});
    }
}
