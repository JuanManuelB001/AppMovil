package com.example.aplicativomovil;

import static android.content.ContentValues.TAG;

import com.google.android.gms.tasks.Task;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText txtCorreo, txtContrasena;
    private Button btnIniciar, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_sesion);

        // Establecer padding para los system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.iniciarSesion_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Inicializar componentes
        txtCorreo = findViewById(R.id.txtcorreo); // Cambiado el ID
        txtContrasena = findViewById(R.id.txtcontrasena); // Usar el ID correcto

        // Botones
        btnIniciar = findViewById(R.id.btnIniciarSesion);
        btnIniciar.setOnClickListener(this);

        btnRegistrarse = findViewById(R.id.btnCrearSesion);
        btnRegistrarse.setOnClickListener(this);

        // Configuración para mostrar/ocultar la contraseña
        final boolean[] isPasswordVisible = {false};
        txtContrasena.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (txtContrasena.getRight() - txtContrasena.getCompoundPaddingEnd())) {
                    if (isPasswordVisible[0]) {
                        txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        txtContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_24, 0);
                    } else {
                        txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT);
                        txtContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_24, 0);
                    }
                    txtContrasena.setSelection(txtContrasena.getText().length());
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        if (view.getId() == R.id.btnIniciarSesion) {
            String email = txtCorreo.getText().toString();
            String password = txtContrasena.getText().toString();
            if (!validar(email, password)) {
                Toast.makeText(this, "Ingrese email/Contraseña", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Espere un Momento", Toast.LENGTH_SHORT).show();
                btnIniciar.setEnabled(false);
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                Toast.makeText(IniciarSesionActivity.this, "Usuario o Contraseña Incorrectos.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        });
            }
            btnIniciar.setEnabled(true);
        } else if (view.getId() == R.id.btnCrearSesion) {
            intent = new Intent(IniciarSesionActivity.this, registrarse.class);
            startActivity(intent);
        }
    }

    private boolean validar(String email, String passwd) {
        return !(email.isEmpty() || passwd.isEmpty());
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(IniciarSesionActivity.this, "Bienvenido: " + user.getEmail(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(IniciarSesionActivity.this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish();// Esto asegura que no se regrese a la actividad anterior.
        } else {
            Log.d(TAG, "No hay usuario autenticado");
            txtCorreo.setText("");
            txtContrasena.setText("");
        }
    }

    }