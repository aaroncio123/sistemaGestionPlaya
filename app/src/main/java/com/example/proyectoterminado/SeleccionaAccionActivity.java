package com.example.proyectoterminado;

import android.os.Bundle;
import android.widget.Toast;
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
            Toast.makeText(this, "Abriendo Calificar para: " + beachName, Toast.LENGTH_SHORT).show();
            // Aquí iría el Intent a la ventana de calificar
        });

        binding.btnVer.setOnClickListener(v -> {
            Toast.makeText(this, "Abriendo Detalles para: " + beachName, Toast.LENGTH_SHORT).show();
            // Aquí iría el Intent a la ventana de ver playa
        });
    }
}