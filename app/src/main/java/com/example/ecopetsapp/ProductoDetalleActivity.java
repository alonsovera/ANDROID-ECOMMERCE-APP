package com.example.ecopetsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.PixelCopy;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ecopetsapp.Modal.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductoDetalleActivity extends AppCompatActivity {
    private Button agregarCarrito;
    private NumberPicker numberPicker;
    private ImageView productoImagen;
    TextView productoPrecio, productoDescripcion, productoNombre;
    private String productoID= "", estado = "Normal", CurrentUserID;
    private FirebaseAuth auth;
    private int stockProducto = 0;  // Para almacenar la cantidad de stock disponible

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalle);

        productoID = getIntent().getStringExtra("pid");
        agregarCarrito = (Button) findViewById(R.id.boton_siguiente_detalles);
        numberPicker = (NumberPicker) findViewById(R.id.number_button);
        productoImagen = (ImageView) findViewById(R.id.producto_imagen_detalles);
        productoPrecio = (TextView) findViewById(R.id.producto_precio_detalles);
        productoDescripcion = (TextView) findViewById(R.id.producto_descripcion_detalles);
        productoNombre = (TextView) findViewById(R.id.producto_nombre_detalles);

        // Inicializar NumberPicker
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setValue(1);

        // Botones para incrementar y decrementar el valor del NumberPicker
        Button buttonMinus = findViewById(R.id.button_minus);
        Button buttonPlus = findViewById(R.id.button_plus);

        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberPicker.getValue() > 1) {
                    numberPicker.setValue(numberPicker.getValue() - 1);
                }
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (numberPicker.getValue() < 10) {
                    numberPicker.setValue(numberPicker.getValue() + 1);
                }
            }
        });

        ObtenerDatosProducto(productoID);

        auth = FirebaseAuth.getInstance();
        CurrentUserID = auth.getCurrentUser().getUid();

        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estado.equals("Pedido") || estado.equals("Enviado")) {
                    Toast.makeText(ProductoDetalleActivity.this, "Esperando a que el primer pedido finalice...", Toast.LENGTH_SHORT).show();
                } else {
                    agregarAlaLista();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        VerificarEstadoOrden();
    }

    private void VerificarEstadoOrden() {
        DatabaseReference OrdenRef;
        OrdenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserID);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             if (snapshot.exists()){
                 String envioEstado = snapshot.child("estado").getValue().toString();
                 if(envioEstado.equals("Enviado")){
                     estado = "Enviado";
                 }else if(envioEstado.equals("No Enviado")){
                     estado = "Pedido";
                 }
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ObtenerDatosProducto(String productoID){
        DatabaseReference ProductoRef = FirebaseDatabase.getInstance().getReference().child("products");
        ProductoRef.child(productoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Productos productos = snapshot.getValue(Productos.class);
                    productoNombre.setText(productos.getNombre());
                    productoDescripcion.setText(productos.getDescripcion());
                    productoPrecio.setText(productos.getPrecio());
                    Picasso.get().load(productos.getImagen()).into(productoImagen);
                    // Obtener el stock del producto
                    stockProducto = Integer.parseInt(productos.getCantidad());
                    // Establecer el valor máximo del NumberPicker según el stock
                    numberPicker.setMaxValue(stockProducto);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void agregarAlaLista() {
        String CurrentTime, CurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat data = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate= data.format(calendar.getTime());

        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = time.format(calendar.getTime());

        final DatabaseReference CartListRef  = FirebaseDatabase.getInstance().getReference().child("Carrito");
        final HashMap<String ,Object> map = new HashMap<>();
        map.put("pid", productoID);
        map.put("nombre", productoNombre.getText().toString());
        map.put("precio", productoPrecio.getText().toString());
        map.put("fecha", CurrentDate);
        map.put("hora", CurrentTime);
        map.put("cantidad", String.valueOf(numberPicker.getValue()));

        CartListRef.child("Usuario compra").child(CurrentUserID).child("products").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    CartListRef.child("Administracion").child(CurrentUserID).child("products").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductoDetalleActivity.this, "Agregado...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductoDetalleActivity.this, PrincipalActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}