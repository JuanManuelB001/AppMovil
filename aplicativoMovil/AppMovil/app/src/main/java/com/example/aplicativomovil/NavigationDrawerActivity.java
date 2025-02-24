package com.example.aplicativomovil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.example.aplicativomovil.ContactoEmergencia.agregarContactoEmergencia;
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

public class NavigationDrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationDrawerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);
        binding.appBarNavigationDrawer.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                Snackbar.make(view, "Mesajeria", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
                intent = new Intent(NavigationDrawerActivity.this, EnviarMensajeActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        navigationView.setNavigationItemSelectedListener(item ->{
            Intent intent;
            if(item.getItemId() == R.id.nav_home){

            }
            else if(item.getItemId() == R.id.nav_mapa){
                intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }else if(item.getItemId() == R.id.nav_esp_32){
                intent = new Intent(this, bluetoothConexion.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            else if(item.getItemId() == R.id.agregar_amigo){
                intent = new Intent(this, listacontactos.class);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            }
            else if(item.getItemId() == R.id.nav_cerrar_sesion){
                Toast.makeText(this,"Cerrar Sesión con Éxito", Toast.LENGTH_SHORT).show();
                cerrarSesion();
                return true;
            }


            return NavigationUI.onNavDestinationSelected(item, navController)|| super.onOptionsItemSelected(item);

        });
    }
    public void cerrarSesion(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}