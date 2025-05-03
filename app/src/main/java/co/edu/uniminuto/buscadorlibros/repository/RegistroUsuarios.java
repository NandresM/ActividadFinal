package co.edu.uniminuto.buscadorlibros.repository;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;



public class RegistroUsuarios {
    private SQLiteDatabase db;
    public void registrarUsuario(String username, String password) {

        // Guardar el usuario y su hash en la base de datos
        // Por ejemplo:
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password_hash", password);
        db.insert("usuarios", null, values);
    }

}
