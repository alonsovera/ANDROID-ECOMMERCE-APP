package com.example.ecopetsapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombre, apellido, email, password, confirmPassword, direccion, telefono;
    private Spinner provinciaSpinner, distritoSpinner;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;  // Reference to the Firebase Realtime Database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializamos FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();  // Get reference to the database

        // Vinculamos los campos con el layout
        nombre = findViewById(R.id.nombresRegister);
        apellido = findViewById(R.id.apellidoRegister);
        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passwordRegister);
        confirmPassword = findViewById(R.id.confirmPasswordRegister);
        direccion = findViewById(R.id.userAdress);
        telefono = findViewById(R.id.telefonoRegister);

        provinciaSpinner = findViewById(R.id.provinciaSpinner);
        distritoSpinner = findViewById(R.id.distritoSpinner);
        registerButton = findViewById(R.id.registerButton);

        // Adaptador para el Spinner de provincias de Lima
        ArrayAdapter<CharSequence> provinciaAdapter = ArrayAdapter.createFromResource(this, R.array.provincias_lima, android.R.layout.simple_spinner_item);
        provinciaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provinciaSpinner.setAdapter(provinciaAdapter);

        // Al seleccionar una provincia, se actualizarán los distritos correspondientes
        provinciaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDistricts(position); // Actualizar distritos según la provincia seleccionada
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });

        // Acción al hacer clic en el botón de registro
        registerButton.setOnClickListener(v -> {
            String emailStr = email.getText().toString();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();
            String telefonoStr = telefono.getText().toString();  // Obtén el número de teléfono
            String nombreStr = nombre.getText().toString();  // Obtén el nombre
            String apellidoStr = apellido.getText().toString();  // Obtén el apellido
            String direccionStr = direccion.getText().toString();  // Obtén la dirección

            // Validamos que los campos no estén vacíos
            if (!emailStr.isEmpty() && !passwordStr.isEmpty() && !confirmPasswordStr.isEmpty()) {
                if (passwordStr.equals(confirmPasswordStr)) {
                    // Si las contraseñas coinciden, proceder con el registro
                    registerUser(emailStr, passwordStr);
                } else {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para registrar al usuario
    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Si el registro es exitoso, obtener el UID del usuario
                        String userId = mAuth.getCurrentUser().getUid();  // Get UID of the logged-in user

                        // Crear un objeto User con los datos del registro
                        User newUser = new User(
                                nombre.getText().toString(),
                                apellido.getText().toString(),
                                email,
                                telefono.getText().toString(),
                                direccion.getText().toString()
                        );

                        // Guardar el usuario en Realtime Database
                        mDatabase.child("users").child(userId).setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "User registered and saved to database!", Toast.LENGTH_SHORT).show();
                                        finish(); // Finaliza la actividad de registro
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "Failed to save data in database", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Si falla el registro, verificamos el tipo de error
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthUserCollisionException e) {
                            // Si el correo ya está registrado
                            Toast.makeText(RegisterActivity.this, "This email is already in use", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            // Para otros tipos de errores
                            Toast.makeText(RegisterActivity.this, "Registration failed. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Método para actualizar los distritos según la provincia seleccionada
    private void updateDistricts(int position) {
        int districtArray = 0;

        // Solo se manejan los distritos de Lima
        switch (position) {
            case 0: // Lima
                districtArray = R.array.distritos_lima;
                break;
            // No se necesitan más casos ya que solo manejamos Lima
        }

        // Actualizar Spinner de distritos
        ArrayAdapter<CharSequence> districtAdapter = ArrayAdapter.createFromResource(this, districtArray, android.R.layout.simple_spinner_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        distritoSpinner.setAdapter(districtAdapter);
    }
}
