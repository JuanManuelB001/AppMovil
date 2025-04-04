package com.example.aplicativomovil;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.Mensajes.EnviarMensajeActivity;
import com.example.aplicativomovil.entidades.listacontactos;
import com.example.aplicativomovil.token.Tokens;
import com.example.aplicativomovil.token.Email;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * Clase principal de la actividad Home, donde se presentan distintas opciones para el usuario:
 * acceder al mapa, gestionar contactos de emergencia, conectar a un dispositivo Bluetooth, ver mensajes,
 * y restaurar la contraseña.
 */
public class homeActivity extends AppCompatActivity implements View.OnClickListener {

    // Declaración de botones de la interfaz
    private Button btnmapa, btnrestaurarContrasena;
    private Button btnConecDIs;          // Botón para ir a la conexión Bluetooth
    private Button btnContact;           // Botón para acceder a contactos de emergencia
    private ImageButton btnVermensajes;  // Botón para ver mensajes recibidos

    // Variables de Firebase
    private FirebaseAuth mAuth;          // Autenticación Firebase
    private String correoUsuario;        // Correo electrónico del usuario (no usado en esta clase actualmente)
    private FirebaseFirestore db;        // Base de datos Firestore (no usado en esta clase actualmente)

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activar compatibilidad con pantalla completa y márgenes del sistema
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar botones con sus IDs
        btnmapa = findViewById(R.id.btnMapa);
        btnrestaurarContrasena = findViewById(R.id.btnRestaurarContrasena);
        btnConecDIs = findViewById(R.id.btnConecDIs);
        btnContact = findViewById(R.id.btnContact);
        btnVermensajes = findViewById(R.id.btnVermensajes);

        // Asignar listeners a los botones
        btnmapa.setOnClickListener(this);
        btnrestaurarContrasena.setOnClickListener(this);
        btnConecDIs.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnVermensajes.setOnClickListener(this);

        // Inicializar autenticación de Firebase
        mAuth = FirebaseAuth.getInstance();

        // Generar y registrar token del dispositivo en Firebase (usado para notificaciones)
        Tokens tk = new Tokens(homeActivity.this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tk.setToken(user);
    }

    /**
     * Método que responde a los clics en los distintos botones de la interfaz.
     * Lanza la actividad correspondiente según el botón presionado.
     */
    @Override
    public void onClick(View view) {
        Intent intent;

        // Redirigir según el botón presionado
        if (view.getId() == R.id.btnMapa) {
            intent = new Intent(homeActivity.this, MapsActivity.class); // Ir al mapa
        } else if (view.getId() == R.id.btnRestaurarContrasena) {
            intent = new Intent(homeActivity.this, restaurarContrasenaActivity.class); // Ir a restaurar contraseña
        } else if (view.getId() == R.id.btnConecDIs) {
            intent = new Intent(homeActivity.this, bluetoothConexion.class); // Ir a conexión Bluetooth
        } else if (view.getId() == R.id.btnContact) {
            intent = new Intent(homeActivity.this, listacontactos.class); // Ir a contactos
        } else if (view.getId() == R.id.btnVermensajes) {
            intent = new Intent(homeActivity.this, EnviarMensajeActivity.class); // Ir a mensajes
        } else {
            return; // Si no se reconoce el botón, no hacer nada
        }

        // Iniciar la actividad correspondiente
        startActivity(intent);
    }
}
