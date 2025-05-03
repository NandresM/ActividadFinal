package co.edu.uniminuto.buscadorlibros.repository;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//helper: metodos predefinidos para la BD
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "biblioteca.db";
    public static final int DB_VERSION = 2;

    public static final String TABLE_LIBROS = "libros";
    public static final String COL_AUTOR       = "autor";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_IMAGENURL   = "imagenUrl";



    public static final String TABLE_CALIFICACIONES = "calificaciones";
    public static final String TABLE_FAVORITOS = "favoritos";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Libros
        db.execSQL("CREATE TABLE " + TABLE_LIBROS + " ("
                + "id TEXT PRIMARY KEY, "
                + "titulo TEXT, "+
                COL_AUTOR + " TEXT, "+
                COL_DESCRIPCION + " TEXT,"+
                COL_IMAGENURL + " TEXT"
                + ");");

        // Crear tabla Calificaciones
        db.execSQL("CREATE TABLE " + TABLE_CALIFICACIONES + " ("
                + "libroId TEXT PRIMARY KEY, "
                + "calificacion REAL, "
                + "FOREIGN KEY(libroId) REFERENCES " + TABLE_LIBROS + "(id) ON DELETE CASCADE"
                + ");");

        // Crear tabla Favoritos
        db.execSQL("CREATE TABLE " + TABLE_FAVORITOS + " ("
                + "libroId TEXT PRIMARY KEY, "
                + "FOREIGN KEY(libroId) REFERENCES " + TABLE_LIBROS + "(id) ON DELETE CASCADE"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALIFICACIONES + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBROS + ";");
        onCreate(db);
    }
}
