package com.example.ecopetsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfirmarActivity extends AppCompatActivity {

    private TextView textoTotal, textoCantidad;
    private Button btnConfirmarPago;
    private String precioTotal, cantidadTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar);

        // Inicializamos las vistas
        textoTotal = findViewById(R.id.textoTotal);
        btnConfirmarPago = findViewById(R.id.btnConfirmarPago);

        // Recibimos el precio total y la cantidad total que se pasó desde CarritoActivity
        precioTotal = getIntent().getStringExtra("Total");
        cantidadTotal = getIntent().getStringExtra("CantidadTotal");

        // Mostramos el precio total y la cantidad en los TextViews
        textoTotal.setText("S/. " + precioTotal);

        // Acción para el botón de Confirmar Pago
        btnConfirmarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Mostrar un Toast de agradecimiento
                Toast.makeText(ConfirmarActivity.this, "Gracias por tu compra", Toast.LENGTH_SHORT).show();

                // Redirigir al inicio de la aplicación (PrincipalActivity)
                Intent intent = new Intent(ConfirmarActivity.this, PrincipalActivity.class);
                startActivity(intent);
                finish(); // Cerrar ConfirmarActivity
            }
        });
    }
}