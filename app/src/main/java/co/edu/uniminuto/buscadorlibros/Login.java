package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Base64;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import android.os.Bundle;



import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class Login extends AppCompatActivity {
    private EditText editTextUsuario;
    private EditText editTextPassword;
    private Button botonIniciarSesion;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_preferences";

    // Credenciales
    private static final String ADMIN_USERNAME       = "admin";
    // Hash de contraseña "adm123" generado previamente con BCrypt (cost 10)
    private static final String ADMIN_PASSWORD_HASH = "$2a$10$8w4zrtAhGH8MhN2cKFnu/O6njuhW8XxzadV77f.uopEc5xWKCoS0S";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences   = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        initViews();

        // Verificar si el usuario está logueado
        String prueba = "adm123";
        String nuevoHash = BCrypt.withDefaults()
                .hashToString(10, prueba.toCharArray());
        Log.d("BCryptGen", "Hash de adm123 = " + nuevoHash);

    }

    private void initViews() {
        editTextUsuario     = findViewById(R.id.editTextUsuario);
        editTextPassword    = findViewById(R.id.editTextPassword);
        botonIniciarSesion  = findViewById(R.id.botonIniciarSesion);

        botonIniciarSesion.setOnClickListener(this::iniciarSesion);
    }

    private void iniciarSesion(View view) {
        String usuario  = editTextUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString();

        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar usuario
        if (!ADMIN_USERNAME.equals(usuario)) {
            Toast.makeText(this, "Usuario incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar contra lo alamcenao
        Log.d("LoginDebug", "ADMIN_PASSWORD_HASH = " + ADMIN_PASSWORD_HASH);
        BCrypt.Result result = BCrypt.verifyer()
                .verify(password.toCharArray(), ADMIN_PASSWORD_HASH);
        Log.d("LoginDebug", "verify() result: " + result.verified);

        if (result.verified) {
            // Éxito
            saveLoginStatus(true, usuario);
            Toast.makeText(this, "Bienvenido, " + usuario, Toast.LENGTH_SHORT).show();
            startMainActivity(view);
        } else {
            // Fallo
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginStatus(boolean isLoggedIn, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putString("username", username);
        editor.apply();
    }

    private void startMainActivity(View view) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}