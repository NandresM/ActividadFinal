package co.edu.uniminuto.buscadorlibros.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import co.edu.uniminuto.buscadorlibros.model.Libro;

public class LibroRepository {

        private final SQLiteDatabase db;

        public LibroRepository(Context context) {
            DatabaseHelper helper = new DatabaseHelper(context);
            db = helper.getWritableDatabase();
        }

        // CREATE
        public long insertLibro(Libro libro) {
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.TABLE_LIBROS + "." + "id", libro.getId());
            cv.put(DatabaseHelper.TABLE_LIBROS + "." + "titulo", libro.getInfoVolumen().getTitulo());
            cv.put(DatabaseHelper.TABLE_LIBROS + "." + "autor", libro.getInfoVolumen().getAutores()[0]);
            return db.insert(DatabaseHelper.TABLE_LIBROS, null, cv);
        }

        // READ ALL
        public List<Libro> getAllLibros() {
            List<Libro> lista = new ArrayList<>();
            Cursor c = db.query(
                    DatabaseHelper.TABLE_LIBROS,
                    new String[]{"id", "titulo", "autor"},
                    null, null, null, null, null
            );
            while (c.moveToNext()) {
                Libro libro = new Libro();
                libro.setId(c.getString(c.getColumnIndexOrThrow("id")));
                // Completa otros campos si tu modelo lo permite
                lista.add(libro);
            }
            c.close();
            return lista;
        }

        // READ BY ID
        public Libro getLibroById(String id) {
            Cursor c = db.query(
                    DatabaseHelper.TABLE_LIBROS,
                    new String[]{"id", "titulo", "autor"},
                    "id = ?", new String[]{id},
                    null, null, null
            );
            if (!c.moveToFirst()) {
                c.close();
                return null;
            }
            Libro libro = new Libro();
            libro.setId(c.getString(c.getColumnIndexOrThrow("id")));
            // Completa otros campos si lo necesitas
            c.close();
            return libro;
        }

        // UPDATE
        public int updateLibro(Libro libro) {
            ContentValues cv = new ContentValues();
            cv.put("titulo", libro.getInfoVolumen().getTitulo());
            cv.put("autor", libro.getInfoVolumen().getAutores()[0]);
            return db.update(
                    DatabaseHelper.TABLE_LIBROS,
                    cv,
                    "id = ?",
                    new String[]{libro.getId()}
            );
        }

        // DELETE
        public int deleteLibro(String id) {
            return db.delete(
                    DatabaseHelper.TABLE_LIBROS,
                    "id = ?", new String[]{id}
            );
        }
}
