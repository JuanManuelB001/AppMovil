package com.example.aplicativomovil;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

import com.example.aplicativomovil.Mensajes.Mensaje;
import com.example.aplicativomovil.token.Email;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// Clase principal que maneja la conexión Bluetooth con un ESP32 y el envío de mensajes de alerta a contactos mediante Firebase
public class bluetoothConexion extends AppCompatActivity {

    // Constantes y variables principales
    private static final String TAG = "BluetoothConexion"; // Tag para logs
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // UUID estándar para comunicación Bluetooth SPP
    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 100; // Código de solicitud de permisos

    // Bluetooth
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private BluetoothDevice dispositivoSeleccionado;
    private ConnectedThread conexionBluetooth; // Hilo para comunicación Bluetooth

    // Componentes de interfaz de usuario
    private Button btnBuscar, btnConectar, btnDesconectar;
    private Spinner spinnerDispositivos;
    private ArrayAdapter<String> dispositivoAdapter;
    private final ArrayList<String> dispositivosEncontrados = new ArrayList<>();

    // Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private List<String> contactosCorreos;
    private List<String> contactosIds;
    private String correoUsuario;

    // Control de alertas
    private boolean mensajeEnviado = false;

    // Permite manejar el resultado al solicitar activar el Bluetooth
    private final ActivityResultLauncher<Intent> enableBluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> Log.d(TAG, "Bluetooth habilitado por el usuario.")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dashboard);

        inicializarUI();            // Inicializa botones y spinner
        inicializarBluetooth();    // Verifica y solicita activar el Bluetooth

        // Inicializa Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Verifica si hay sesión activa
        if (currentUser == null) {
            Toast.makeText(this, "Usuario no autenticado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Obtiene el correo electrónico del usuario autenticado
        Email email = new Email(this);
        correoUsuario = email.getEmail(currentUser);

        // Inicializa listas de contactos
        contactosCorreos = new ArrayList<>();
        contactosIds = new ArrayList<>();
    }

    // Inicializa componentes de la UI y sus eventos
    private void inicializarUI() {
        btnBuscar = findViewById(R.id.idBtnVerContacto);
        btnConectar = findViewById(R.id.IdBtnAñadir);
        btnDesconectar = findViewById(R.id.IdBtnDesconectar);
        spinnerDispositivos = findViewById(R.id.IdContactoRegist);

        dispositivoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dispositivosEncontrados);
        dispositivoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDispositivos.setAdapter(dispositivoAdapter);

        // Eventos de botones
        btnBuscar.setOnClickListener(v -> buscarDispositivos());
        btnConectar.setOnClickListener(v -> conectarDispositivo());
        btnDesconectar.setOnClickListener(v -> desconectarDispositivo());
    }

    // Verifica disponibilidad de Bluetooth y solicita activarlo si está apagado
    private void inicializarBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            mostrarToast("El dispositivo no soporta Bluetooth.");
            finish();
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothLauncher.launch(enableBtIntent);
        }
    }

    // Busca dispositivos Bluetooth emparejados
    private void buscarDispositivos() {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            mostrarToast("Bluetooth no habilitado.");
            return;
        }

        // Verifica permisos
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
            return;
        }

        dispositivosEncontrados.clear(); // Limpia lista previa

        Set<BluetoothDevice> dispositivosVinculados = bluetoothAdapter.getBondedDevices();
        if (dispositivosVinculados.size() > 0) {
            for (BluetoothDevice dispositivo : dispositivosVinculados) {
                dispositivosEncontrados.add(dispositivo.getName());
            }
            dispositivoAdapter.notifyDataSetChanged();
        } else {
            mostrarToast("No se encontraron dispositivos emparejados.");
        }

        // Asigna el dispositivo seleccionado al spinner
        spinnerDispositivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nombreDispositivo = dispositivosEncontrados.get(position);
                dispositivoSeleccionado = obtenerDispositivoPorNombre(nombreDispositivo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                dispositivoSeleccionado = null;
            }
        });
    }

    // Devuelve el dispositivo Bluetooth emparejado con el nombre proporcionado
    private BluetoothDevice obtenerDispositivoPorNombre(String nombre) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
            return null;
        }

        for (BluetoothDevice dispositivo : bluetoothAdapter.getBondedDevices()) {
            if (dispositivo.getName().equals(nombre)) {
                return dispositivo;
            }
        }
        return null;
    }

    // Establece conexión con el dispositivo Bluetooth seleccionado
    private void conectarDispositivo() {
        Toast.makeText(this, "Conectando Espera un Momento", Toast.LENGTH_SHORT).show();

        if (dispositivoSeleccionado == null) {
            mostrarToast("Selecciona un dispositivo para conectar.");
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
            return;
        }

        try {
            bluetoothSocket = dispositivoSeleccionado.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
            bluetoothSocket.connect(); // Intenta conectar al socket
            conexionBluetooth = new ConnectedThread(bluetoothSocket); // Crea hilo de comunicación
            conexionBluetooth.start(); // Inicia la comunicación
            mostrarToast("Conexión exitosa.");
        } catch (IOException e) {
            mostrarToast("Error al conectar: " + e.getMessage());
        }
    }

    // Cierra la conexión Bluetooth y libera recursos
    private void desconectarDispositivo() {
        if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
            try {
                if (conexionBluetooth != null) {
                    conexionBluetooth.cancel(); // Detiene hilo
                    conexionBluetooth = null;
                }
                bluetoothSocket.close(); // Cierra socket
                bluetoothSocket = null;
                mostrarToast("Dispositivo desconectado.");
            } catch (IOException e) {
                mostrarToast("Error al desconectar: " + e.getMessage());
            }
        } else {
            mostrarToast("No hay dispositivo conectado.");
        }
    }

    // Envía un carácter al dispositivo Bluetooth
    private void realizarAccionBT(char comando) {
        if (conexionBluetooth != null) {
            conexionBluetooth.enviarComando(comando);
        } else {
            mostrarToast("Conexión no establecida.");
        }
    }

    // Muestra mensajes rápidos en pantalla
    private void mostrarToast(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    // Hilo que se encarga de la comunicación por Bluetooth
    private class ConnectedThread extends Thread {
        private final OutputStream outputStream;
        private final InputStream inputStream;

        ConnectedThread(BluetoothSocket socket) {
            OutputStream tmpOut = null;
            InputStream tmpIn = null;
            try {
                tmpOut = socket.getOutputStream();
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                mostrarToast("Error al obtener flujo de entrada o salida: " + e.getMessage());
            }
            outputStream = tmpOut;
            inputStream = tmpIn;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.read(buffer); // Lee datos
                    String mensaje = new String(buffer, 0, bytes);

                    // Procesa el mensaje recibido
                    if (mensaje.contains("Fuera de rango")) {
                        runOnUiThread(() -> mostrarToast("El ESP32 está fuera de rango."));
                        if (!mensajeEnviado) {
                            mensajeEnviado = true;
                            enviarMensaje(); // Envía alerta a contactos
                        }
                    } else if (mensaje.contains("Dentro del rango")) {
                        if (mensajeEnviado) {
                            mensajeEnviado = false;
                        }
                        runOnUiThread(() -> mostrarToast("El ESP32 está dentro del rango."));
                    }
                } catch (IOException e) {
                    mostrarToast("Error al leer mensaje: " + e.getMessage());
                    break;
                }
            }
        }

        public void enviarComando(char comando) {
            try {
                outputStream.write(comando);
            } catch (IOException e) {
                mostrarToast("Error al enviar comando: " + e.getMessage());
            }
        }

        public void cancel() {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                mostrarToast("Error al cerrar flujos: " + e.getMessage());
            }
        }
    }

    // Obtiene los contactos del usuario y luego llama a enviarMensajeAContactos
    private void enviarMensaje() {
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

                        if (!contactosCorreos.isEmpty()) {
                            enviarMensajeAContactos();
                        } else {
                            Toast.makeText(this, "No hay contactos para enviar el mensaje.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar contactos.", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al cargar contactos: ", task.getException());
                    }
                });
    }

    // Envía un mensaje de alerta a todos los contactos del usuario
    private void enviarMensajeAContactos() {
        String mensajeTexto = "PRECAUSION ESTE USUARIO FUE HURTADO. ⚠️";

        if (mensajeTexto.isEmpty()) {
            Toast.makeText(this, "Por favor escribe un mensaje.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (String correoAmigo : contactosCorreos) {
            Log.d(TAG, "Contactos a enviar mensaje: " + correoAmigo);

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
                                    .addOnSuccessListener(documentReference ->
                                            Toast.makeText(this, "Mensaje enviado a " + correoAmigo, Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Error al enviar mensaje a " + correoAmigo, Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Error al enviar mensaje", e);
                                    });
                        } else {
                            Toast.makeText(this, "Amigo no encontrado: " + correoAmigo, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}



