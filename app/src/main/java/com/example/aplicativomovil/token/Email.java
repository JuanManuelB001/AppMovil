package com.example.aplicativomovil.token;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

/**
 * Clase auxiliar para obtener el correo electrónico del usuario autenticado con Firebase.
 */
public class Email {

    private FirebaseUser user;
    private Context context;

    /**
     * Constructor que recibe el contexto de la aplicación.
     *
     * @param context Contexto desde donde se crea la clase.
     */
    public Email(Context context) {
        this.context = context;
        this.user = user; // Nota: esto es redundante, ver recomendación abajo
    }

    /**
     * Obtiene el correo electrónico del usuario proporcionado.
     *
     * @param user Objeto {@link FirebaseUser} autenticado.
     * @return El correo electrónico del usuario o un mensaje de error si es nulo.
     */
    public String getEmail(FirebaseUser user) {
        if (user != null) {
            String tokenUser = user.getEmail();
            // Toast opcional para mostrar el correo: Toast.makeText(context, "Usuario " + user.getEmail(), Toast.LENGTH_SHORT).show();
            return tokenUser;
        }
        return "El Usuario no tiene apk instalada";
    }
}

