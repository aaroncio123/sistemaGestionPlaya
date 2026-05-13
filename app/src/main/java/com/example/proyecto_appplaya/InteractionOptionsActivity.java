package com.example.proyecto_appplaya;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class InteractionOptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_options);

        Button btnBack = findViewById(R.id.btn_back);
        Button btnRate = findViewById(R.id.btn_rate_beach);
        Button btnExplore = findViewById(R.id.btn_explore_beach);

        btnBack.setOnClickListener(v -> finish());

        btnRate.setOnClickListener(v -> {
            Intent intent = new Intent(InteractionOptionsActivity.this, RatingActivity.class);
            startActivity(intent);
        });

        btnExplore.setOnClickListener(v -> {
            Intent intent = new Intent(InteractionOptionsActivity.this, ExplorationActivity.class);
            startActivity(intent);
        });
    }
}
