package com.example.aplicativomovil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IniciarSesionTest  {

    @Rule
    public ActivityTestRule<IniciarSesionActivity> mActivityRule =
            new ActivityTestRule<>(IniciarSesionActivity.class);

    @Test
    public void testIniciarSesion() throws InterruptedException  {
        // Ingresar correo y contraseña
        onView(withId(R.id.txtcorreo)).perform(replaceText("johndoe@example.com"));
        // CERRAR EL TECLADO
        Espresso.closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.txtcontrasena)).perform(replaceText("password123"));
        Espresso.closeSoftKeyboard();
        Thread.sleep(1000);

        // Hacer clic en el botón de Iniciar sesión
        onView(withId(R.id.btnIniciarSesion)).perform(click());

        // Verificar que la siguiente actividad se ha abierto

    }
    @Test
    public void testIniciarSesionFailure() throws InterruptedException  {
        // Ingresar correo y contraseña
        onView(withId(R.id.txtcorreo)).perform(replaceText("johndoe@example.com"));
        // CERRAR EL TECLADO
        Espresso.closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withId(R.id.txtcontrasena)).perform(replaceText("password1"));
        Espresso.closeSoftKeyboard();
        Thread.sleep(1000);

        // Hacer clic en el botón de Iniciar sesión
        onView(withId(R.id.btnIniciarSesion)).perform(click());

        // Verificar que la siguiente actividad se ha abierto

    }
}
