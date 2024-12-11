package com.example.ecopetsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.ecopetsapp.databinding.ActivityLocalizarBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView

class LocalizarActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocalizarBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLocalizarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el DrawerLayout y NavigationView
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        // Configuración del ActionBarDrawerToggle para abrir y cerrar el drawer
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar el NavigationView para responder a las opciones seleccionadas
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_carrito -> navigateToCarrito()
                R.id.nav_about_us -> navigateToAboutUs()
                R.id.nav_location -> navigateToLocation()
                R.id.nav_buscar -> navigateToBuscar()
                R.id.nav_social_media -> navigateToSocialMedia()
                R.id.nav_help -> navigateToHelp()
                R.id.nav_log_out -> logOut()
                else -> Toast.makeText(this, "Unknown Option", Toast.LENGTH_SHORT).show()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        // Obtener el fragmento del mapa y notificar cuando el mapa esté listo
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Métodos para navegación a otras actividades
     */
    private fun navigateToCarrito() {
        Toast.makeText(this, "Carrito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, CarritoActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToBuscar() {
        Toast.makeText(this, "Carrito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PrincipalActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToSocialMedia() {
        Toast.makeText(this, "Carrito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, SocialMediaActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToHelp() {
        Toast.makeText(this, "Carrito", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ActivityHelp::class.java)
        startActivity(intent)
    }

    private fun navigateToAboutUs() {
        Toast.makeText(this, "About Us", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, AboutUsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLocation() {
        Toast.makeText(this, "Location", Toast.LENGTH_SHORT).show()
        // Ya estás en LocalizarActivity, no necesitas acción aquí
    }

    private fun logOut() {
        Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show()
        // Lógica para cerrar sesión
    }

    /**
     * Manipula el mapa una vez que está disponible.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Agregar un marcador en una ubicación específica y mover la cámara
        val location = LatLng(-12.1771722, -77.0015497) // Cambia estas coordenadas por tu ubicación
        mMap.addMarker(MarkerOptions().position(location).title("EcoPets"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    // Maneja el back press para cerrar el drawer si está abierto
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
