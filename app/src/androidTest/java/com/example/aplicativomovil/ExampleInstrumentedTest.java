package com.example.aplicativomovil;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.aplicativomovil.Mensajes.EnviarMensajeActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void isVisibleMensajeLayout() {
        // SIMULACIÓN FIREBASE MOCKITO
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
            // Acceso a la actividad
            scenario.onActivity(activity -> {
                // Aquí inyectamos los mocks en la actividad
                activity.mAuth = mockAuth;
                activity.db = mockFirestore;
            });

            // Simulamos la acción de hacer clic en el botón
            Espresso.onView(ViewMatchers.withId(R.id.fab)).perform(ViewActions.click());

            // Esperamos a que la acción se complete
            Espresso.onIdle();

            // Verificamos si se muestra el layout correspondiente
            Espresso.onView(ViewMatchers.withId(R.id.mensajeria_layout))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
        }
    }
}
