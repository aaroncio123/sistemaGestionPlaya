package com.example.proyectoterminado;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.proyectoterminado.databinding.SeleccionaAccionBinding;
import java.util.Calendar;

public class SeleccionaAccionActivity extends AppCompatActivity {

    private SeleccionaAccionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SeleccionaAccionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        actualizarFondoSegunHora();

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

    private void actualizarFondoSegunHora() {
        int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int fondoRes;

        if (hora >= 6 && hora < 12) {
            fondoRes = R.drawable.amanecer;
        } else if (hora >= 12 && hora < 19) {
            fondoRes = R.drawable.atardecer;
        } else {
            fondoRes = R.drawable.anochecer;
        }

        binding.getRoot().setBackgroundResource(fondoRes);
    }
}
