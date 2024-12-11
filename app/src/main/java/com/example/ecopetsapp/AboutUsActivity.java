package com.example.ecopetsapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AboutUsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference UserRef;
    private FirebaseAuth auth;
    private String CurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("users");

        // Inicializar el DrawerLayout y ActionBarDrawerToggle (esto es lo que haces para activar el menú lateral)
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Configuración del ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        TextView nombreHeader = (TextView) headerView.findViewById(R.id.usuario_nombre_perfil);

        // Si necesitas establecer eventos en los items del menú de navegación:
        navigationView.setNavigationItemSelectedListener(item -> {
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
            }
            else if(id == R.id.nav_location){
                ActivityLocation();
            }
            else if(id == R.id.nav_log_out){
                auth.signOut();
                //EnviarAlLogin();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

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
        Intent intent = new Intent(AboutUsActivity.this, LocalizarActivity.class);
        startActivity(intent);
    }


    private void ActivityAboutUs() {
        Toast.makeText(this,"About us", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AboutUsActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    private void ActivityCarrito() {
        Toast.makeText(this,"Carrito", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AboutUsActivity.this, CarritoActivity.class);
        startActivity(intent);
    }
    private void ActivityBuscar() {
        Toast.makeText(this,"Products", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AboutUsActivity.this, PrincipalActivity.class);
        startActivity(intent);
    }
    private void ActivitySocialMedia() {
        Toast.makeText(this,"Social Media", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AboutUsActivity.this, SocialMediaActivity.class);
        startActivity(intent);
    }
    private void ActivityHelp() {
        Toast.makeText(this,"Help", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AboutUsActivity.this, ActivityHelp.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
