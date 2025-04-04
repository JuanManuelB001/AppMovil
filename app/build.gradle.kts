plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    //FIREBASE
    //id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.aplicativomovil"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.aplicativomovil"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11  // O VERSION_17, dependiendo de tu preferencia
        targetCompatibility = JavaVersion.VERSION_11  // O VERSION_17, dependiendo de tu preferencia
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.play.services.maps)
    //FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:33.5.0"))
    implementation ("com.google.firebase:firebase-firestore:24.8.1")
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    //FIREBASE AUTENTICATION
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.firebase.database)
    implementation(libs.play.services.location)

    //FIREBASE CLOUD MESSASING
    implementation(libs.firebase.messaging)
    implementation ("com.google.firebase:firebase-messaging:24.1.0")
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //FIREBASAE FIREBABASE CLOUD MESSASING(FCM)
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.google.firebase:firebase-messaging:23.0.0")
    //IMPLEMENTACIONES TEST
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    // Para pruebas unitarias con Mockito
    testImplementation ("org.mockito:mockito-core:4.8.0" )// Última versión disponible de Mockito
    // Para pruebas instrumentadas en Android con Mockito
    androidTestImplementation ("org.mockito:mockito-android:4.8.0") // Si haces pruebas instrumentadas
    // Dependencia de JUnit (asegúrate de tenerla también)
    testImplementation ("junit:junit:4.13.2")  // Para pruebas unitarias
    androidTestImplementation ("io.mockk:mockk-android:1.12.0") // PRUEBAS MOCKK
    testImplementation ("io.mockk:mockk:1.12.0")
}