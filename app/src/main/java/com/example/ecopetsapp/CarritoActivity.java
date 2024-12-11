package com.example.ecopetsapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopetsapp.Modal.Carrito;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class CarritoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button Siguiente;
    private TextView TotalPrecio, mensaje1;
    private String CurrentUserID;
    private FirebaseAuth auth;

    private double PrecioTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);

        recyclerView = (RecyclerView)findViewById(R.id.carrito_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager( this);
        recyclerView.setLayoutManager(layoutManager);

        Siguiente = (Button)findViewById(R.id.siguiente_proceso);
        TotalPrecio = (TextView)findViewById(R.id.precio_total);
        mensaje1 = (TextView)findViewById(R.id.mensaje1);
        auth = FirebaseAuth.getInstance();
        CurrentUserID = auth.getCurrentUser().getUid();

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarritoActivity.this, ConfirmarActivity.class);
                intent.putExtra("Total", String.valueOf(PrecioTotal));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        VerificarEstadoOrden();


        final DatabaseReference CarListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        FirebaseRecyclerOptions<Carrito> options = new FirebaseRecyclerOptions.Builder<Carrito>()
                .setQuery(CarListRef.child("Usuario compra").child(CurrentUserID).child("products"),Carrito.class).build();

        FirebaseRecyclerAdapter<Carrito, CarritoViewHolder> adapter = new FirebaseRecyclerAdapter<Carrito, CarritoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CarritoViewHolder holder, int position, @NonNull Carrito model) {
                holder.carProductoNombre.setText(model.getNombre());
                holder.carProductoPrecio.setText("Precio: " +model.getPrecio());
                holder.carProductoCantidad.setText("Cantidad: " +model.getCantidad());


                double unTipoPrecio  = (Double.valueOf(model.getPrecio()))*Integer.valueOf(model.getCantidad());

                PrecioTotal = PrecioTotal + unTipoPrecio;
                TotalPrecio.setText("Total: "+String.valueOf(PrecioTotal));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{
                                "Edit",
                                "Delete"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarritoActivity.this);
                        builder.setTitle("Product options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i==0){
                                    Intent intent = new Intent(CarritoActivity.this, ProductoDetalleActivity.class);
                                    intent.putExtra("precio", model.getPrecio());
                                    startActivity(intent);
                                }
                                if(i==1){
                                    CarListRef.child("Usuario compra")
                                            .child(CurrentUserID)
                                            .child("products")
                                            .child(model.getPrecio()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(CarritoActivity.this,"Producto eliminado", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CarritoActivity.this, PrincipalActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CarritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item_layout, parent, false);
                CarritoViewHolder holder = new CarritoViewHolder(view);
                return holder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }




    private void VerificarEstadoOrden() {

    }

}