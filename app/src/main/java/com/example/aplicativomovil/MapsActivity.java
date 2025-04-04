package com.example.aplicativomovil;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.aplicativomovil.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;


/**
 * Actividad que muestra un mapa de Google Maps y coloca un marcador en la ubicación actual del usuario.
 * Requiere permisos de ubicación (ACCESS_FINE_LOCATION) para funcionar correctamente.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Objeto del mapa de Google
    private ActivityMapsBinding binding; // View binding para acceder a las vistas del layout
    private static final int MY_PERMISSION_REQUEST_LOCATION = 1; // Código de solicitud para permisos de ubicación
    private FusedLocationProviderClient fusedLocationClient; // Cliente para acceder a la ubicación del dispositivo
    private double textoLatitud, textoLongitud; // Variables para almacenar latitud y longitud

    /**
     * Método llamado cuando se crea la actividad.
     * Inicializa el binding, el cliente de ubicación y configura el mapa.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializa el proveedor de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtiene el fragmento del mapa y lo prepara de forma asíncrona
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Solicita la última ubicación conocida del dispositivo.
     * Si se obtiene correctamente, coloca un marcador en el mapa y centra la cámara allí.
     */
    public void obtenerUbicacion() {
        // Verifica si los permisos de ubicación han sido concedidos
        if (ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Solicita el permiso al usuario si no ha sido concedido
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_LOCATION);
            return;
        }

        // Obtiene la última ubicación conocida
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Verifica que se haya obtenido una ubicación válida
                        if (location != null) {
                            textoLatitud = location.getLatitude();
                            textoLongitud = location.getLongitude();

                            // Crea una LatLng con la ubicación actual
                            LatLng currentLocation = new LatLng(textoLatitud, textoLongitud);
                            float zoomLevel = 15.0f; // Nivel de zoom del mapa

                            // Agrega un marcador y mueve la cámara a la ubicación actual
                            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, zoomLevel));

                            // Muestra la latitud y longitud mediante Toast
                            Toast.makeText(MapsActivity.this, "Latitud: " + textoLatitud, Toast.LENGTH_LONG).show();
                            Toast.makeText(MapsActivity.this, "Longitud: " + textoLongitud, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Método llamado cuando el usuario responde a la solicitud de permisos.
     * Si se concede el permiso, se obtiene la ubicación.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso fue concedido, obtiene la ubicación
                obtenerUbicacion();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método llamado cuando el mapa está listo para ser usado.
     * Llama a obtenerUbicacion para colocar el marcador.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        obtenerUbicacion(); // Llama a obtenerUbicacion al estar el mapa listo
    }
}

