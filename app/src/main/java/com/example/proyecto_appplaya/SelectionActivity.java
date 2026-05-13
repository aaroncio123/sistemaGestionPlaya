package com.example.proyecto_appplaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Punto 2: Selección de Ubicación
        setContentView(R.layout.activity_selection);

        AutoCompleteTextView autoDistrito = findViewById(R.id.autoComplete_distrito);
        AutoCompleteTextView autoPlaya = findViewById(R.id.autoComplete_playa);
        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnGoOptions = findViewById(R.id.btn_go_options);

        // Datos de ejemplo para los menús desplegables de Material Design
        String[] distritos = {"Miraflores", "Barranco", "Chorrillos", "Lurín", "Punta Hermosa"};
        String[] playas = {"Playa Waikiki", "Playa Los Delfines", "Playa Agua Dulce", "Playa El Silencio", "Playa Punta Rocas"};

        ArrayAdapter<String> adapterDistrito = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, distritos);
        autoDistrito.setAdapter(adapterDistrito);

        ArrayAdapter<String> adapterPlaya = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, playas);
        autoPlaya.setAdapter(adapterPlaya);

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnGoOptions.setOnClickListener(v -> {
            Intent intent = new Intent(SelectionActivity.this, InteractionOptionsActivity.class);
            startActivity(intent);
        });
    }
}
