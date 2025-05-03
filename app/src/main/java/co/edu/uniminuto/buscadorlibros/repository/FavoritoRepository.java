package co.edu.uniminuto.buscadorlibros.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class FavoritoRepository {
    private final SQLiteDatabase db;
    private static final String COL_LIBRO_ID = "libroId";

    public FavoritoRepository(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public long addFavorito(String libroId) {
        ContentValues cv = new ContentValues();
        cv.put("libroId", libroId);
        return db.insert(DatabaseHelper.TABLE_FAVORITOS, null, cv);
    }

    public List<String> getAllFavoritos() {
        List<String> lista = new ArrayList<>();
        Cursor c = db.query(DatabaseHelper.TABLE_FAVORITOS,
                new String[]{"libroId"}, null, null, null, null, null);
        while (c.moveToNext()) {
            lista.add(c.getString(c.getColumnIndexOrThrow("libroId")));
        }
        c.close();
        return lista;
    }

    public boolean isFavorito(String libroId) {
        Cursor c = db.query(
                DatabaseHelper.TABLE_FAVORITOS,
                new String[]{ COL_LIBRO_ID },
                COL_LIBRO_ID + " = ?",
                new String[]{ libroId },
                null, null, null
        );
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public int removeFavorito(String libroId) {
        return db.delete(
                DatabaseHelper.TABLE_FAVORITOS,
                COL_LIBRO_ID + " = ?",
                new String[]{ libroId }
        );
    }
}
