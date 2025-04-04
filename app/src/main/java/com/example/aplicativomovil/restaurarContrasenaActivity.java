package com.example.aplicativomovil;

import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;

public class restaurarContrasenaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtcontrasena, txtconfContrasena;
    private Button btnaceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_restaurar_contrasena);

        // Ajuste de insets para compatibilidad con barras del sistema (pantallas con notch, etc.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de componentes
        txtcontrasena = findViewById(R.id.txtcontrasena);
        txtconfContrasena = findViewById(R.id.txtcofircontrasena);
        btnaceptar = findViewById(R.id.btnAceptar);

        // Listener para el botón Aceptar
        btnaceptar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Obtener los valores ingresados
        String contrasena = txtcontrasena.getText().toString().trim();
        String confContrasena = txtconfContrasena.getText().toString().trim();

        // Verificar que se haya hecho clic en el botón correcto
        if (view.getId() == R.id.btnAceptar) {

            // Validar que las contraseñas coincidan
            if (!contrasena.equals(confContrasena)) {
                Toast.makeText(this, "Error: Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
                limpiarCampos();
                return;
            }

            // Validar longitud mínima de la contraseña
            if (contrasena.length() < 6) {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtener el usuario actual
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // Verificar si hay un usuario autenticado
            if (user != null) {
                user.updatePassword(contrasena)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Contraseña actualizada exitosamente
                                Toast.makeText(this, "Éxito: Contraseña Actualizada", Toast.LENGTH_SHORT).show();
                                // Redirigir al usuario a la pantalla principal
                                Intent intent = new Intent(this, homeActivity.class);
                                startActivity(intent);
                                finish(); // Finalizar esta actividad para evitar volver atrás
                            } else {
                                // Fallo al actualizar contraseña (puede deberse a sesión expirada)
                                Toast.makeText(this, "Error al actualizar contraseña", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                // No hay sesión iniciada
                Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para limpiar los campos de contraseña
    private void limpiarCampos() {
        txtcontrasena.setText("");
        txtconfContrasena.setText("");
    }
}
