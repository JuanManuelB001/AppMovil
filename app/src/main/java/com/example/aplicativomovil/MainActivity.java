package com.example.aplicativomovil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.aplicativomovil.token.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

/**
 * Actividad principal de inicio de la aplicación.
 * Muestra dos opciones: Iniciar Sesión o Crear una Nueva Cuenta.
 * Si ya hay una sesión activa, redirige automáticamente al `NavigationDrawerActivity`.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCrearSesion, btnIniciarSesion;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilita el diseño de borde a borde (Edge-to-Edge)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajusta el padding del layout para que no se solape con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar botones y asignar listener
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);

        btnCrearSesion = findViewById(R.id.btnCrearSesion);
        btnCrearSesion.setOnClickListener(this);

        // Comprueba si ya hay un usuario autenticado
        comprobarSesion();
    }

    /**
     * Comprueba si hay una sesión de usuario activa.
     * Si existe un usuario autenticado, se redirige automáticamente a la pantalla principal del usuario.
     */
    public void comprobarSesion() {
        Intent intent;
        Object firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            // Usuario ya autenticado, redirige al NavigationDrawerActivity
            intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish(); // Finaliza MainActivity para evitar volver atrás
        }
    }

    /**
     * Maneja los clics en los botones de la actividad.
     * @param v Vista que fue clicada (botón)
     */
    @Override
    public void onClick(View v) {
        Intent intent;
        if (v.getId() == R.id.btnCrearSesion) {
            // Redirige a la actividad de registro
            intent = new Intent(this, registrarse.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnIniciarSesion) {
            // Redirige a la actividad de inicio de sesión
            intent = new Intent(this, IniciarSesionActivity.class);
            startActivity(intent);
        }
    }
}
