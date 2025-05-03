package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class Login extends AppCompatActivity {
    private EditText editTextUsuario, editTextPassword;
    private Button botonIniciarSesion;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        initViews();

        botonIniciarSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void initViews() {
        editTextUsuario    = findViewById(R.id.editTextUsuario);
        editTextPassword   = findViewById(R.id.editTextPassword);
        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
    }

    private void iniciarSesion() {
        String usuario  = editTextUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validaciones básicas de campos vacíos
        if (TextUtils.isEmpty(usuario)) {
            editTextUsuario.setError("El usuario es requerido");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("La contraseña es requerida");
            return;
        }

        // Comparación directa sin hashing
        if (usuario.equals("admin") && password.equals("adm123")) {
            saveLoginStatus(true, usuario);
            Toast.makeText(this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();
            startMainActivity();
            finish();
        } else {
            Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveLoginStatus(boolean isLoggedIn, String username) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.putString("username", username);
        editor.apply();
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }
}