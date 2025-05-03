package co.edu.uniminuto.buscadorlibros.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class CalificacionRepository {
    private final SQLiteDatabase db;
    private static final String COL_LIBRO_ID   = "libroId";
    private static final String COL_CALIFICACION = "calificacion";

    public CalificacionRepository(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public boolean upsertCalificacion(String libroId, float calificacion) {
        ContentValues cv = new ContentValues();

        cv.put(COL_LIBRO_ID, libroId);
        cv.put(COL_CALIFICACION, calificacion);

        int rows = db.update(
                DatabaseHelper.TABLE_CALIFICACIONES,
                cv,
                COL_LIBRO_ID + " = ?",
                new String[]{ libroId }
        );

        if (rows == 0) {
            return db.insert(DatabaseHelper.TABLE_CALIFICACIONES, null, cv) != -1;

        }
        return rows > 0;
    }

    public List<Float> getAllCalificaciones() {
        List<Float> lista = new ArrayList<>();
        Cursor c = db.query(DatabaseHelper.TABLE_CALIFICACIONES,
                new String[]{"calificacion"}, null, null, null, null, null);
        while (c.moveToNext()) {
            lista.add(c.getFloat(c.getColumnIndexOrThrow("calificacion")));
        }
        c.close();
        return lista;
    }

    public Float getCalificacionByLibro(String libroId) {
        Cursor c = db.query(
                DatabaseHelper.TABLE_CALIFICACIONES,
                new String[]{ COL_CALIFICACION },
                COL_LIBRO_ID + " = ?",
                new String[]{ libroId },
                null, null, null
        );
        if (!c.moveToFirst()) { c.close(); return null; }
        float cal = c.getFloat(c.getColumnIndexOrThrow(COL_CALIFICACION));
        c.close();
        return cal;
    }

    public int deleteCalificacion(String libroId) {
        return db.delete(DatabaseHelper.TABLE_CALIFICACIONES, "libroId = ?", new String[]{libroId});
    }
}
