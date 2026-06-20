package com.example.proyectoterminado;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectoterminado.databinding.SeleccionaAccionBinding;

public class SeleccionaAccionActivity extends AppCompatActivity {

    private SeleccionaAccionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SeleccionaAccionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String beachName = getIntent().getStringExtra("BEACH_NAME");
        if (beachName != null) {
            binding.txtBeachName.setText(beachName);
        }

        binding.btnCalificar.setOnClickListener(v -> {
            Intent intent = new Intent(this, CalificarActivity.class);
            intent.putExtra("BEACH_NAME", beachName);
            startActivity(intent);
        });

        binding.btnVer.setOnClickListener(v -> {
            Intent intent = new Intent(this, VerPlayaActivity.class);
            intent.putExtra("BEACH_NAME", beachName);
            startActivity(intent);
        });
    }
}