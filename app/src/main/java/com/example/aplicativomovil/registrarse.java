package com.example.aplicativomovil;

import android.annotation.SuppressLint;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Actividad para registrar un nuevo usuario con Firebase Authentication y Firestore.
 * Permite ingresar nombre, teléfono, correo y contraseña, y almacena los datos en la base de datos.
 */
public class registrarse extends AppCompatActivity implements View.OnClickListener {

    // Inputs de UI
    private EditText txtNombre, txtTelefono, txtCorreoElectronico;
    private EditText txtContrasena, txtConfirmarContrasena;
    private EditText contrasena, confirContrasena;
    private Button btnGuardar;

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // ID generado para el documento del usuario en Firestore
    public String documentId;

    /**
     * Método principal llamado al crear la actividad.
     * Configura los elementos de UI, visibilidad de contraseña e inicializa Firebase.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrarse);

        // Aplicar padding a la vista para evitar solapamiento con barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registrarse_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Referencias a campos de texto
        txtNombre = findViewById(R.id.txtNombreUsuario);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreoElectronico = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtcontra);
        txtConfirmarContrasena = findViewById(R.id.txtConfirmarContrasena);

        contrasena = findViewById(R.id.txtcontra);
        confirContrasena = findViewById(R.id.txtConfirmarContrasena);

        // Botón guardar
        btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);

        // Lógica para mostrar/ocultar contraseña en el campo "Contraseña"
        final boolean[] isPasswordVisible1 = {false};
        contrasena.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (contrasena.getRight() - contrasena.getCompoundPaddingEnd())) {
                    if (isPasswordVisible1[0]) {
                        contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        contrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_24, 0);
                    } else {
                        contrasena.setInputType(InputType.TYPE_CLASS_TEXT);
                        contrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_24, 0);
                    }
                    contrasena.setSelection(contrasena.getText().length());
                    isPasswordVisible1[0] = !isPasswordVisible1[0];
                    return true;
                }
            }
            return false;
        });

        // Lógica para mostrar/ocultar confirmación de contraseña
        final boolean[] isPasswordVisible2 = {false};
        confirContrasena.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (confirContrasena.getRight() - confirContrasena.getCompoundPaddingEnd())) {
                    if (isPasswordVisible2[0]) {
                        confirContrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        confirContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_24, 0);
                    } else {
                        confirContrasena.setInputType(InputType.TYPE_CLASS_TEXT);
                        confirContrasena.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_24, 0);
                    }
                    confirContrasena.setSelection(confirContrasena.getText().length());
                    isPasswordVisible2[0] = !isPasswordVisible2[0];
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Método llamado cuando se hace clic en el botón.
     * Valida los datos ingresados y llama a {@link #crearUsuario(String, String)} si son válidos.
     */
    @Override
    public void onClick(View view) {
        String nombre = txtNombre.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String correo = txtCorreoElectronico.getText().toString().trim();
        String contra = txtContrasena.getText().toString().trim();
        String confirmContra = txtConfirmarContrasena.getText().toString().trim();

        if (view.getId() == R.id.btnGuardar) {
            if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show();
            } else if (!contra.equals(confirmContra)) {
                Toast.makeText(this, "Contraseña no Coincide", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Espere un Momento", Toast.LENGTH_SHORT).show();
                btnGuardar.setEnabled(false);
                crearUsuario(correo, contra);
            }
        }
    }

    /**
     * Crea un usuario con Firebase Authentication.
     */
    private void crearUsuario(String correo, String contra) {
        String nombre = txtNombre.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(correo, contra)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        postUsuario(nombre, telefono, correo);
                    } else {
                        if (contra.length() < 6) {
                            Toast.makeText(this, "Error: Mínimo 6 caracteres", Toast.LENGTH_LONG).show();
                        }
                        btnGuardar.setEnabled(true);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: Correo en uso", Toast.LENGTH_LONG).show();
                    btnGuardar.setEnabled(true);
                });
    }

    /**
     * Interfaz para obtener el total de documentos en Firestore de forma asíncrona.
     */
    public interface CountCallback {
        void onCountReady(int count);
    }

    /**
     * Cuenta la cantidad de usuarios actuales en Firestore y llama al callback con el resultado.
     */
    private void totalDatos(CountCallback callback) {
        db.collection("Usuarios").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = task.getResult().size();
                        callback.onCountReady(count);
                    } else {
                        Log.w("Error", "Error obteniendo documentos.", task.getException());
                        callback.onCountReady(0);
                    }
                });
    }

    /**
     * Envía los datos del nuevo usuario a Firestore.
     */
    private void postUsuario(String nombre, String telefono, String correoElectronico) {
        Map<String, Object> user = new HashMap<>();
        List<Map<String, Object>> contacto_emergencia = new ArrayList<>();

        user.put("Nombre", nombre);
        user.put("Telefono", telefono);
        user.put("Correo Electronico", correoElectronico);
        user.put("Contacto Emergencia", contacto_emergencia);

        totalDatos(count -> {
            documentId = "usuario_" + count;

            db.collection("Usuarios")
                    .document(documentId)
                    .set(user)
                    .addOnSuccessListener(aVoid -> {
                        Intent intent = new Intent(registrarse.this, MainActivity.class);
                        btnGuardar.setEnabled(true);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error al guardar el usuario: ", e);
                        Toast.makeText(registrarse.this, "Fallo al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        btnGuardar.setEnabled(true);
                    });
        });
    }

    /**
     * Limpia todos los campos de entrada del formulario.
     */
    private void limpiarCampos() {
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreoElectronico.setText("");
        txtContrasena.setText("");
        txtConfirmarContrasena.setText("");
    }
}
