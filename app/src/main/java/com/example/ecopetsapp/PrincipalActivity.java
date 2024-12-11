package com.example.ecopetsapp;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecopetsapp.Modal.Productos;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private FirebaseAuth auth;
    private String CurrentUserId;
    private DatabaseReference UserRef, ProductosRef;
    private String Telefono = "";
    private FloatingActionButton botonFlotante;
    private RecyclerView reciclerMenu;
    RecyclerView.LayoutManager layoutManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            Telefono = bundle.getString("phone");

        }

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");
        ProductosRef = FirebaseDatabase.getInstance().getReference().child("products");
        reciclerMenu=findViewById(R.id.recycler_menu);
        reciclerMenu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        reciclerMenu.setLayoutManager(layoutManager);
        botonFlotante = (FloatingActionButton)findViewById(R.id.fab);
        botonFlotante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this, CarritoActivity.class);
                startActivity(intent);
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("EcoPets");
        setActionBar(toolbar);


        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView nombreHeader = (TextView) headerView.findViewById(R.id.usuario_nombre_perfil);

        UserRef.child(CurrentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() || snapshot.hasChild("imagen")){
                    nombreHeader.setText(snapshot.child("nombre").getValue().toString());

                }else if(snapshot.exists()){
                    nombreHeader.setText(snapshot.child("nombre").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
       /* if (firebaseUser == null) {
            EnviarAllLogin();
        } else {

        }*/
        VerificarUsuarioExistente();

        FirebaseRecyclerOptions<Productos> options = new FirebaseRecyclerOptions.Builder<Productos>()
                .setQuery(ProductosRef, Productos.class).build();

        FirebaseRecyclerAdapter<Productos, ProductoViewHolder> adapter = new FirebaseRecyclerAdapter<Productos, ProductoViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductoViewHolder holder, int position, @NonNull Productos model) {
                holder.productoNom.setText(model.getNombre());
                holder.productoCantidad.setText("Cantidad: " +model.getCantidad());
                holder.productoDescripcion.setText(model.getDescripcion());
                holder.productoPrecio.setText("S/. : " +model.getPrecio());

                Picasso.get().load(model.getImagen()).into(holder.productoImagen);

                holder.productoImagen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(PrincipalActivity.this, ProductoDetalleActivity.class);
                        intent.putExtra("pid",model.getPid());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.productos_items_layout,viewGroup, false);
                ProductoViewHolder holder = new ProductoViewHolder(view);
                return holder;
            }
        };

        reciclerMenu.setAdapter(adapter);
        adapter.startListening();


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.activity_principal_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
         return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_carrito){
            ActivityCarrito();
        }
        else if(id == R.id.nav_about_us){
            ActivityAboutUs();
        }
        else if(id == R.id.nav_buscar){
            ActivityBuscar();
        }
        else if(id == R.id.nav_social_media){
            ActivitySocialMedia();
        }
        else if(id == R.id.nav_help){
            ActivityHelp();
        }else if(id == R.id.nav_location){
            ActivityLocation();
        }
        else if(id == R.id.nav_log_out){
            auth.signOut();
            //EnviarAlLogin();
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void ActivityLocation() {
        Toast.makeText(this,"Location", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PrincipalActivity.this, LocalizarActivity.class);
        startActivity(intent);
    }


    private void ActivityAboutUs() {
        Toast.makeText(this,"About us", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(PrincipalActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    private void ActivityCarrito() {
        Intent intent = new Intent(PrincipalActivity.this, CarritoActivity.class);
        startActivity(intent);
    }
    private void ActivityBuscar() {
        Intent intent = new Intent(PrincipalActivity.this, BuscarActivity.class);
        startActivity(intent);
    }
    private void ActivitySocialMedia() {
        Intent intent = new Intent(PrincipalActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
    private void ActivityHelp() {
        Intent intent = new Intent(PrincipalActivity.this, ActivityHelp.class);
        startActivity(intent);
    }


    private void VerificarUsuarioExistente(){
        final String CurrentUserId = auth.getCurrentUser().getUid();

    }


}
