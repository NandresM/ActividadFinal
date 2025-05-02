package co.edu.uniminuto.buscadorlibros;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.MessageDigest;

public class Login extends AppCompatActivity {
    private EditText editTextUsuario;
    private EditText editTextPassword;
    private Button botonIniciarSesion;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "login_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Verificar si ya hay una sesión activa


        // Inicializar vistas
        initViews();

        // Configurar el botón de inicio de sesión
        botonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });
    }
    private void initViews() {
        editTextUsuario = findViewById(R.id.editTextUsuario);
        editTextPassword = findViewById(R.id.editTextPassword);
        botonIniciarSesion = findViewById(R.id.botonIniciarSesion);
    }

    private void iniciarSesion() {
        // Obtener valores ingresados
        String usuario = editTextUsuario.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validar campos
        if (TextUtils.isEmpty(usuario)) {
            editTextUsuario.setError("El usuario es requerido");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("La contraseña es requerida");
            return;
        }

        // Aquí puedes implementar tu lógica de autenticación real
        // Por ahora, para simplificar, aceptaremos cualquier usuario/contraseña
        // o puedes establecer credenciales fijas para pruebas

        if (usuario.equals("admin") && password.equals("admin")) {
            // Guardar sesión
            saveLoginStatus(true, usuario);

            // Mostrar mensaje de éxito
            Toast.makeText(this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();

            // Abrir MainActivity
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

    private boolean isLoggedIn() {
        return sharedPreferences.getBoolean("isLoggedIn", false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
    }
}