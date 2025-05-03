package co.edu.uniminuto.buscadorlibros.repository;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//helper: metodos predefinidos para la BD
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "biblioteca.db";
    public static final int DB_VERSION = 1;

    // Tablas
    public static final String TABLE_LIBROS = "libros";
    public static final String TABLE_CALIFICACIONES = "calificaciones";
    public static final String TABLE_FAVORITOS = "favoritos";

    // Columnas comunes
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LIBRO_ID = "libroId";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    // Columnas para libros
    public static final String COLUMN_TITULO = "titulo";
    public static final String COLUMN_AUTOR = "autor";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_IMAGEN_URL = "imagenUrl";

    // Columnas para calificaciones
    public static final String COLUMN_CALIFICACION = "calificacion";

    /**
     * Constructor del DatabaseHelper
     * @param context Contexto de la aplicación
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Libros
        String CREATE_LIBROS_TABLE = "CREATE TABLE " + TABLE_LIBROS + " ("
                + COLUMN_ID + " TEXT PRIMARY KEY, "
                + COLUMN_TITULO + " TEXT NOT NULL, "
                + COLUMN_AUTOR + " TEXT, "
                + COLUMN_DESCRIPCION + " TEXT, "
                + COLUMN_IMAGEN_URL + " TEXT, "
                + COLUMN_TIMESTAMP + " INTEGER DEFAULT (strftime('%s', 'now'))"
                + ")";
        db.execSQL(CREATE_LIBROS_TABLE);

        // Crear tabla Calificaciones
        String CREATE_CALIFICACIONES_TABLE = "CREATE TABLE " + TABLE_CALIFICACIONES + " ("
                + COLUMN_LIBRO_ID + " TEXT PRIMARY KEY, "
                + COLUMN_CALIFICACION + " REAL NOT NULL, "
                + COLUMN_TIMESTAMP + " INTEGER DEFAULT (strftime('%s', 'now')), "
                + "FOREIGN KEY(" + COLUMN_LIBRO_ID + ") REFERENCES " + TABLE_LIBROS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_CALIFICACIONES_TABLE);

        // Crear tabla Favoritos
        String CREATE_FAVORITOS_TABLE = "CREATE TABLE " + TABLE_FAVORITOS + " ("
                + COLUMN_LIBRO_ID + " TEXT PRIMARY KEY, "
                + COLUMN_TIMESTAMP + " INTEGER DEFAULT (strftime('%s', 'now')), "
                + "FOREIGN KEY(" + COLUMN_LIBRO_ID + ") REFERENCES " + TABLE_LIBROS + "(" + COLUMN_ID + ") ON DELETE CASCADE"
                + ")";
        db.execSQL(CREATE_FAVORITOS_TABLE);

        // Crear índices para mejorar rendimiento
        db.execSQL("CREATE INDEX idx_libros_titulo ON " + TABLE_LIBROS + "(" + COLUMN_TITULO + ")");
        db.execSQL("CREATE INDEX idx_libros_autor ON " + TABLE_LIBROS + "(" + COLUMN_AUTOR + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En caso de actualización de la base de datos, eliminamos las tablas y las volvemos a crear
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALIFICACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIBROS);
        onCreate(db);
    }

    /**
     * Método para habilitar las restricciones de clave foránea
     */
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

}
