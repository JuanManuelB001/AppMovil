package com.example.aplicativomovil.messageCloud;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.aplicativomovil.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
/**
 * Servicio que gestiona los tokens y mensajes de Firebase Cloud Messaging (FCM).
 * Extiende de {@link FirebaseMessagingService}.
 */
public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    /**
     * Se ejecuta cuando el servicio es creado.
     * Aquí se puede obtener el token de registro de FCM.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Obtener el token de registro FCM
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Error al obtener el token de FCM", task.getException());
                            return;
                        }

                        // Token obtenido correctamente
                        String token = task.getResult();
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);

                        // Aquí puedes enviar el token a tu servidor o guardarlo
                        // Toast opcional: Toast.makeText(MyFirebaseInstanceIDService.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Se llama cuando FCM genera un nuevo token para el dispositivo.
     * Este método es importante para mantener el token actualizado en tu backend.
     *
     * @param token El nuevo token generado por FCM.
     */
    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Nuevo token generado: " + token);

        // Aquí puedes guardar o enviar el nuevo token a tu servidor
    }

    /**
     * Se ejecuta cuando se recibe un mensaje desde FCM mientras la app está en primer plano.
     *
     * @param message Objeto {@link RemoteMessage} que contiene los datos del mensaje.
     */
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        Log.d(TAG, "Mensaje recibido: " + message.getMessageId());

        // Aquí puedes mostrar una notificación o procesar datos del mensaje
    }
}

