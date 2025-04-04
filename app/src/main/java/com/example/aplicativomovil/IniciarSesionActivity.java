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

/**
 * Actividad encargada del inicio de sesión del usuario utilizando Firebase Authentication.
 * También permite ir a la actividad de registro y alternar la visibilidad de la contraseña.
 */
public class IniciarSesionActivity extends AppCompatActivity implements View.OnClickListener {

    // Firebase
    private FirebaseAuth mAuth;

    // UI Elements
    private EditText txtCorreo, txtContrasena;
    private Button btnIniciar, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Habilitar modo pantalla completa compatible con los bordes del sistema
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_iniciar_sesion);

        // Ajustar el padding para evitar que el contenido se solape con las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.iniciarSesion_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        // Vincular los campos de texto
        txtCorreo = findViewById(R.id.txtcorreo);
        txtContrasena = findViewById(R.id.txtcontrasena);

        // Botones de la interfaz
        btnIniciar = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnCrearSesion);

        // Asignar eventos de clic
        btnIniciar.setOnClickListener(this);
        btnRegistrarse.setOnClickListener(this);

        // Funcionalidad para mostrar/ocultar la contraseña
        final boolean[] isPasswordVisible = {false};
        txtContrasena.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Verifica si se tocó el ícono del ojo al final del campo
                if (event.getRawX() >= (txtContrasena.getRight() - txtContrasena.getCompoundPaddingEnd())) {
                    if (isPasswordVisible[0]) {
                        // Ocultar contraseña
                        txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        txtContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_24, 0);
                    } else {
                        // Mostrar contraseña
                        txtContrasena.setInputType(InputType.TYPE_CLASS_TEXT);
                        txtContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_24, 0);
                    }
                    // Mantener el cursor al final
                    txtContrasena.setSelection(txtContrasena.getText().length());
                    isPasswordVisible[0] = !isPasswordVisible[0];
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Maneja los clics en los botones.
     * @param view Vista que fue clicada.
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        if (view.getId() == R.id.btnIniciarSesion) {
            // Obtener los valores de email y contraseña
            String email = txtCorreo.getText().toString();
            String password = txtContrasena.getText().toString();

            // Validar que los campos no estén vacíos
            if (!validar(email, password)) {
                Toast.makeText(this, "Ingrese email/Contraseña", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Espere un Momento", Toast.LENGTH_SHORT).show();
                btnIniciar.setEnabled(false); // Deshabilitar el botón mientras se autentica

                // Intentar iniciar sesión con Firebase
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                // Autenticación exitosa
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // Error de autenticación
                                Toast.makeText(IniciarSesionActivity.this, "Usuario o Contraseña Incorrectos.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        });

                btnIniciar.setEnabled(true); // Rehabilitar el botón
            }

        } else if (view.getId() == R.id.btnCrearSesion) {
            // Redirigir a la actividad de registro
            intent = new Intent(IniciarSesionActivity.this, registrarse.class);
            startActivity(intent);
        }
    }

    /**
     * Valida que el correo y la contraseña no estén vacíos.
     * @param email Correo ingresado
     * @param passwd Contraseña ingresada
     * @return true si ambos campos están completos, false en caso contrario
     */
    private boolean validar(String email, String passwd) {
        return !(email.isEmpty() || passwd.isEmpty());
    }

    /**
     * Actualiza la interfaz según el resultado de la autenticación.
     * @param user Usuario autenticado o null si falló la autenticación.
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(IniciarSesionActivity.this, "Bienvenido: " + user.getEmail(), Toast.LENGTH_LONG).show();
            // Redirige a la actividad principal del usuario
            Intent intent = new Intent(IniciarSesionActivity.this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish(); // Cierra esta actividad para evitar volver atrás
        } else {
            Log.d("LOGIN", "No hay usuario autenticado");
            txtCorreo.setText("");
            txtContrasena.setText("");
        }
    }
}
