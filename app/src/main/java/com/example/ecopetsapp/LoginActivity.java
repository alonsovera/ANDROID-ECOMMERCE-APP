package com.example.ecopetsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin, passwordLogin;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Vinculamos los campos y botones con los elementos del layout
        emailLogin = findViewById(R.id.emailLogin);
        passwordLogin = findViewById(R.id.passwordLogin);
        loginButton = findViewById(R.id.loginButton);

        // Acción al hacer clic en el botón de login
        loginButton.setOnClickListener(v -> {
            String email = emailLogin.getText().toString();
            String password = passwordLogin.getText().toString();

            // Validamos que los campos no estén vacíos
            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para realizar el login
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el login es exitoso, redirigimos al HomeActivity
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, PrincipalActivity.class)); // Cambia a la pantalla principal

                        finish(); // Finalizamos la actividad de login para evitar que el usuario regrese a ella

                    } else {
                        // Si el login falla
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para ir a la pantalla de registro
    public void goToRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
