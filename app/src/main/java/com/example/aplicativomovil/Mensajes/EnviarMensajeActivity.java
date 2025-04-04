package com.example.aplicativomovil.Mensajes;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicativomovil.R;
import com.example.aplicativomovil.token.Email;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
/**
 * Actividad para enviar mensajes a contactos registrados y visualizar los mensajes recibidos.
 * Utiliza Firebase Authentication y Firestore para gestionar los datos.
 */
public class EnviarMensajeActivity extends AppCompatActivity {

    private Spinner spinnerContactos;
    private EditText editTextMensaje;
    private Button buttonEnviar;
    private RecyclerView rvMensajesContactos;

    public MensajesAdapter mensajesAdapter;
    public FirebaseFirestore db;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;

    public List<String> contactosCorreos;
    public List<String> contactosIds;
    private List<Mensaje> mensajesList;

    private String correoUsuario;

    /**
     * Método que se ejecuta al iniciar la actividad.
     * Inicializa vistas, Firebase, carga contactos y mensajes.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensajes);

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        } else {
            Toast.makeText(this, "usuario Activo", Toast.LENGTH_SHORT).show();
        }

        // Obtener correo del usuario actual
        Email email = new Email(this);
        correoUsuario = email.getEmail(currentUser);

        // Inicializar vistas y listas
        rvMensajesContactos = findViewById(R.id.rvMensajesContactos);
        mensajesList = new ArrayList<>();
        mensajesAdapter = new MensajesAdapter(mensajesList);
        rvMensajesContactos.setLayoutManager(new LinearLayoutManager(this));
        rvMensajesContactos.setAdapter(mensajesAdapter);

        spinnerContactos = findViewById(R.id.spinnerContactos);
        editTextMensaje = findViewById(R.id.editTextMensaje);
        buttonEnviar = findViewById(R.id.buttonEnviar);
        contactosCorreos = new ArrayList<>();
        contactosIds = new ArrayList<>();

        // Cargar contactos del usuario y sus mensajes recibidos
        cargarContactos();
        mirarMensajesDeContacto(db, correoUsuario);

        // Enviar mensaje al presionar botón
        buttonEnviar.setOnClickListener(v -> enviarMensaje());
    }

    /**
     * Carga los contactos del usuario actual desde Firestore.
     * Llena el spinner con los correos electrónicos de los amigos.
     */
    private void cargarContactos() {
        db.collection("Usuarios")
                .document(currentUser.getUid())
                .collection("amigos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        contactosCorreos.clear();
                        contactosIds.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String correo = document.getString("correo");
                            String idAmigo = document.getString("amigoId");
                            if (correo != null && idAmigo != null) {
                                contactosCorreos.add(correo);
                                contactosIds.add(idAmigo);
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, contactosCorreos);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerContactos.setAdapter(adapter);
                    } else {
                        Toast.makeText(this, "Error al cargar contactos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Consulta y muestra los mensajes recibidos por el usuario actual desde Firestore.
     *
     * @param db               Instancia de FirebaseFirestore.
     * @param correoContacto   Correo electrónico del usuario actual.
     */
    private void mirarMensajesDeContacto(FirebaseFirestore db, String correoContacto) {
        db.collection("Usuarios")
                .whereEqualTo("Correo Electronico", correoContacto)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String contactoId = document.getId();
                        db.collection("Usuarios")
                                .document(contactoId)
                                .collection("mensajes_recibidos")
                                .get()
                                .addOnSuccessListener(mensajesQuery -> {
                                    for (QueryDocumentSnapshot mensajeDoc : mensajesQuery) {
                                        Mensaje mensaje = mensajeDoc.toObject(Mensaje.class);
                                        mensajesList.add(mensaje);
                                    }
                                    mensajesAdapter.notifyDataSetChanged();
                                })
                                .addOnFailureListener(e -> Log.e("EnviarMensaje", "Error al consultar mensajes", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("EnviarMensaje", "Error al consultar contacto", e));
    }

    /**
     * Envía un mensaje al contacto seleccionado desde el spinner.
     * Crea una entrada en la colección "mensajes_recibidos" del destinatario.
     */
    private void enviarMensaje() {
        String correoAmigo = spinnerContactos.getSelectedItem().toString().trim();
        String mensajeTexto = editTextMensaje.getText().toString().trim();

        if (mensajeTexto.isEmpty()) {
            Toast.makeText(this, "Por favor escribe un mensaje.", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("Usuarios").whereEqualTo("Correo Electronico", correoAmigo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        String amigoId = task.getResult().getDocuments().get(0).getId();
                        Mensaje mensaje = new Mensaje(correoUsuario, correoAmigo, mensajeTexto, System.currentTimeMillis());

                        db.collection("Usuarios")
                                .document(amigoId)
                                .collection("mensajes_recibidos")
                                .add(mensaje)
                                .addOnSuccessListener(documentReference -> {
                                    Toast.makeText(this, "Mensaje enviado correctamente.", Toast.LENGTH_SHORT).show();
                                    editTextMensaje.setText("");
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al enviar mensaje.", Toast.LENGTH_SHORT).show();
                                    Log.e("EnviarMensaje", "Error al enviar mensaje", e);
                                });
                    } else {
                        Toast.makeText(this, "Amigo no encontrado.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

