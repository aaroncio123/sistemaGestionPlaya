package com.example.proyectoterminado;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoterminado.databinding.SeleccionaPlayaBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SeleccionaPlayaActivity extends AppCompatActivity {

    private SeleccionaPlayaBinding binding;
    
    // Dirección exacta para el emulador
    private final String MI_IP = "10.0.2.2";
    private final String URL_API = "http://10.0.2.2/playaGestion/get_detalles_filtros.php";

    private ArrayList<String> listaDistritos = new ArrayList<>();
    private ArrayList<String> todasLasPlayas = new ArrayList<>(); // Formato: "Playa|Distrito"
    private ArrayList<String> playasFiltradas = new ArrayList<>();
    
    private ArrayAdapter<String> adapterDistritos;
    private ArrayAdapter<String> adapterPlayas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SeleccionaPlayaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapterDistritos = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaDistritos);
        adapterPlayas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, playasFiltradas);
        
        binding.spinnerDistritos.setAdapter(adapterDistritos);
        binding.spinnerPlayas.setAdapter(adapterPlayas);

        cargarDatosDesdeServer();

        // Lógica de filtrado
        binding.spinnerDistritos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filtrarPlayasPorDistrito(listaDistritos.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        binding.btnContinuar.setOnClickListener(v -> {
            String playa = (String) binding.spinnerPlayas.getSelectedItem();
            if (playa != null) {
                Intent intent = new Intent(this, SeleccionaAccionActivity.class);
                intent.putExtra("BEACH_NAME", playa);
                startActivity(intent);
            }
        });
    }

    private void cargarDatosDesdeServer() {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_API, null,
                response -> {
                    try {
                        // Cargar Distritos
                        JSONArray dists = response.getJSONArray("distritos");
                        listaDistritos.clear();
                        for (int i = 0; i < dists.length(); i++) {
                            listaDistritos.add(dists.getJSONObject(i).getString("nombre"));
                        }
                        adapterDistritos.notifyDataSetChanged();

                        // Cargar Playas (temp)
                        JSONArray plays = response.getJSONArray("playas");
                        todasLasPlayas.clear();
                        for (int i = 0; i < plays.length(); i++) {
                            JSONObject obj = plays.getJSONObject(i);
                            todasLasPlayas.add(obj.getString("playa") + "|" + obj.getString("distrito"));
                        }
                        
                        // Forzar primer filtrado
                        if (!listaDistritos.isEmpty()) filtrarPlayasPorDistrito(listaDistritos.get(0));

                    } catch (JSONException e) { e.printStackTrace(); }
                },
                error -> {
                    String message = "Error desconocido";
                    if (error instanceof com.android.volley.NoConnectionError) {
                        message = "Sin conexión al servidor. ¿Firewall activo? ¿IP correcta?";
                    } else if (error instanceof com.android.volley.TimeoutError) {
                        message = "Tiempo de espera agotado";
                    } else if (error.getMessage() != null) {
                        message = error.getMessage();
                    }
                    Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
                }
        );
        queue.add(request);
    }

    private void filtrarPlayasPorDistrito(String distritoSeleccionado) {
        playasFiltradas.clear();
        for (String item : todasLasPlayas) {
            String[] partes = item.split("\\|");
            if (partes[1].equals(distritoSeleccionado)) {
                playasFiltradas.add(partes[0]);
            }
        }
        adapterPlayas.notifyDataSetChanged();
    }
}