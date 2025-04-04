package com.example.aplicativomovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;
import com.example.aplicativomovil.Mensajes.EnviarMensajeActivity;
import com.example.aplicativomovil.entidades.listacontactos;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicativomovil.databinding.ActivityNavigationDrawerBinding;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Actividad principal con Navigation Drawer que permite navegar entre distintas pantallas de la aplicación,
 * incluyendo el mapa, conexión con ESP32, mensajería y gestión de contactos.
 */
public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerBinding binding;

    /**
     * Método llamado al crear la actividad.
     * Configura el Navigation Drawer, el AppBar, y los listeners de navegación.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Vincula la vista utilizando ViewBinding
        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura la toolbar como ActionBar
        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);

        // Configura el botón flotante (FAB) para abrir la actividad de mensajería
        binding.appBarNavigationDrawer.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Mesajeria", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();

                Intent intent = new Intent(NavigationDrawerActivity.this, EnviarMensajeActivity.class);
                startActivity(intent);
            }
        });

        // Configura el Navigation Drawer y sus destinos principales
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();

        // Configura el controlador de navegación
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Configura acciones personalizadas para los ítems del menú de navegación
        navigationView.setNavigationItemSelectedListener(item -> {
            Intent intent;

            if (item.getItemId() == R.id.nav_home) {
                // Ya está en home, no hace nada
            } else if (item.getItemId() == R.id.nav_mapa) {
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_esp_32) {
                intent = new Intent(this, bluetoothConexion.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.agregar_amigo) {
                intent = new Intent(this, listacontactos.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else if (item.getItemId() == R.id.nav_cerrar_sesion) {
                Toast.makeText(this, "Cerrar Sesión con Éxito", Toast.LENGTH_SHORT).show();
                cerrarSesion();
                return true;
            }

            // Acción por defecto si no se reconoció el ítem
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        });
    }

    /**
     * Cierra la sesión del usuario y lo redirige al MainActivity.
     */
    public void cerrarSesion() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        // Limpia la pila de actividades para que no se pueda volver con "back"
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Infla el menú de opciones (en la ActionBar).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    /**
     * Maneja la navegación "Up" desde la barra superior (ActionBar).
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
