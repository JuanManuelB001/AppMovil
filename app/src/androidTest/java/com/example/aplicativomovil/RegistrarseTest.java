package com.example.aplicativomovil;

import android.content.Intent;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrarseTest {

    @Rule
    public ActivityScenarioRule<registrarse> activityRule =
            new ActivityScenarioRule<>(registrarse.class);

    @Before
    public void setUp() {
        // Setup inicial si es necesario
    }

    @Test
    public void testRegistrarseSuccess() {
        // Introducir los datos en los campos
        onView(withId(R.id.txtNombreUsuario)).perform(replaceText("John Doe"));
        onView(withId(R.id.txtTelefono)).perform(replaceText("1234567890"));
        onView(withId(R.id.txtCorreo)).perform(replaceText("johndoe@example.com"));
        onView(withId(R.id.txtcontra)).perform(replaceText("password123"));
        onView(withId(R.id.txtConfirmarContrasena)).perform(replaceText("password123"));

        // Hacer clic en el botón "Guardar"
        onView(withId(R.id.btnGuardar)).perform(click());


    }

    @Test
    public void testRegistrarseFailure() {
        // Ingresar datos incorrectos para probar la validación
        onView(withId(R.id.txtNombreUsuario)).perform(replaceText("John Doe"));
        onView(withId(R.id.txtTelefono)).perform(replaceText("1234567890"));
        onView(withId(R.id.txtCorreo)).perform(replaceText("johndoe@example.com"));
        onView(withId(R.id.txtcontra)).perform(replaceText("123"));
        onView(withId(R.id.txtConfirmarContrasena)).perform(replaceText("123"));

        // Hacer clic en el botón "Guardar"
        onView(withId(R.id.btnGuardar)).perform(click());

        // Verificar que el mensaje de error se muestra (en este caso, que la contraseña debe tener al menos 6 caracteres)
        //onView(withText("Error: Correo en Uso")).check(matches(isDisplayed()));
    }
}
