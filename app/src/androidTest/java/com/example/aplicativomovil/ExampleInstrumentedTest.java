package com.example.aplicativomovil;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;

import com.example.aplicativomovil.Mensajes.EnviarMensajeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void isVisibleMensajeLayout() {
        // SIMULACION FIREBASE MOCKITO
        FirebaseUser mockUser = Mockito.mock(FirebaseUser.class);

        // Configura el comportamiento del mock (ID y correo)
        Mockito.when(mockUser.getUid()).thenReturn("KBDm2mtva1dUfseX7QhaR5evWLl1");
        Mockito.when(mockUser.getEmail()).thenReturn("johndoe@example.com");

        // Crear FirebaseAuth simulado
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);
        Mockito.when(mockAuth.getCurrentUser()).thenReturn(mockUser);

        // Simular FirebaseFirestore si es necesario
        FirebaseFirestore mockFirestore = Mockito.mock(FirebaseFirestore.class);

        // Iniciar la actividad en el escenario
        try (ActivityScenario<EnviarMensajeActivity> scenario = ActivityScenario.launch(EnviarMensajeActivity.class)) {
            // En este punto debes reemplazar las dependencias en la actividad
            scenario.onActivity(activity -> {
                // Aquí puedes hacer que la actividad utilice los mocks en lugar de los objetos reales
                activity.mAuth = mockAuth;  // Reemplazar el FirebaseAuth en la actividad
                activity.db = mockFirestore;  // Si es necesario, reemplazar Firestore
            });

            // Simulamos la acción de hacer clic en el botón
            Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());

            // Esperamos a que la acción se complete
            Espresso.onIdle();

            // Verificamos si el layout correspondiente se muestra
            Espresso.onView(ViewMatchers.withId(R.id.mensajeria_layout))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}
