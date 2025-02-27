package com.example.aplicativomovil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicativomovil.DataBase.dataBaseHelper;
import com.example.aplicativomovil.token.Tokens;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCrearSesion, btnIniciarSesion ;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // INICIALIZAR AUTH
        mAuth = FirebaseAuth.getInstance();

        // INICAR BOTONES
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnIniciarSesion.setOnClickListener(this);

        btnCrearSesion = findViewById(R.id.btnCrearSesion);
        btnCrearSesion.setOnClickListener(this);
        comprobarSesion();

    }
    public void comprobarSesion(){
        Intent intent;
        Object firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            intent = new Intent(this, NavigationDrawerActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        if(v.getId() == R.id.btnCrearSesion){
            intent = new Intent(this, registrarse.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btnIniciarSesion){
            intent = new Intent(this, IniciarSesionActivity.class);
            startActivity(intent);
        }
    }
}