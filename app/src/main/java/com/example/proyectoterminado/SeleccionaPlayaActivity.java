package com.example.proyectoterminado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoterminado.databinding.SeleccionaPlayaBinding;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SeleccionaPlayaActivity extends AppCompatActivity {

    private SeleccionaPlayaBinding binding;
    private ArrayList<String> listaPlayas;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SeleccionaPlayaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaPlayas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaPlayas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerPlayas.setAdapter(adapter);

        // Cargar datos desde el "Workbench" (vía API intermedia)
        obtenerPlayasDeAPI();

        binding.btnContinuar.setOnClickListener(v -> {
            String playaSeleccionada = (String) binding.spinnerPlayas.getSelectedItem();
            if (playaSeleccionada != null) {
                Intent intent = new Intent(this, SeleccionaAccionActivity.class);
                intent.putExtra("BEACH_NAME", playaSeleccionada);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Por favor, selecciona una playa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerPlayasDeAPI() {
        // NOTA: Android no puede conectar directamente a MySQL Workbench. 
        // Se requiere una API (PHP/NodeJS/Python) que sirva los datos en JSON.
        // Usaremos una URL de ejemplo. Reemplázala por tu endpoint real.
        String url = "https://tu-servidor-api.com/get_playas.php";

        // MOCK: Para que puedas probarlo ahora mismo, añadiremos datos manuales si la red falla
        listaPlayas.add("Playa de las Canteras");
        listaPlayas.add("Playa del Inglés");
        listaPlayas.add("Playa de Maspalomas");
        adapter.notifyDataSetChanged();

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        listaPlayas.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            listaPlayas.add(obj.getString("nombre"));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
        );
        queue.add(jsonArrayRequest);

    }
}