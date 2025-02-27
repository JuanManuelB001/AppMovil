package com.example.aplicativomovil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;


import androidx.test.core.app.ActivityScenario;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import static androidx.test.espresso.assertion.ViewAssertions.matches;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    /*
        COMPROBANDO EL ESTADO DE LOS LAYOUT DEL PROYECTO
     */
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.aplicativomovil", appContext.getPackageName());
    }
    @Test
    public void isVisibleNavigationDrawerLayout(){

        try(ActivityScenario<NavigationDrawerActivity> scenario = ActivityScenario.launch(NavigationDrawerActivity.class)){

            onView(withId(R.id.drawer_layout))
                    .check(matches(isDisplayed()));
        }
    }
    @Test
    public void isVisibleMapLayout(){
        try(ActivityScenario<MapsActivity> scenario = ActivityScenario.launch(MapsActivity.class)){
            onView(withId(R.id.map))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void isVisibleIniciarSesionLayout(){
        try(ActivityScenario<IniciarSesionActivity> scenario = ActivityScenario.launch(IniciarSesionActivity.class)){
            onView(withId(R.id.iniciarSesion_layout))
                    .check(matches(isDisplayed()));
        }
    }
    @Test
    public void isVisibleRegistrarseLayout(){
        try(ActivityScenario<registrarse> scenario = ActivityScenario.launch(registrarse.class)){
            onView(withId(R.id.registrarse_layout))
                    .check(matches(isDisplayed()));
        }
    }
    @Test
    public void isVisibleBluetoothLayout(){
        try(ActivityScenario<bluetoothConexion> scenario = ActivityScenario.launch(bluetoothConexion.class)){
            onView(withId(R.id.bluetooth_layout))
                    .check(matches(isDisplayed()));
        }
    }



}