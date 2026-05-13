package com.example.proyecto_appplaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Punto 1: Pantalla de Inicio de Sesión
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_google_login);
        ProgressBar progressBar = findViewById(R.id.progress_bar);

        progressBar.setVisibility(View.GONE);

        btnLogin.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            // Simulación de delay de autenticación
            v.postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, SelectionActivity.class);
                startActivity(intent);
                finish();
            }, 1500);
        });
    }
}
