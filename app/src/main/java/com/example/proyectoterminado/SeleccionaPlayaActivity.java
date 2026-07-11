package com.example.proyectoterminado;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class SeleccionaPlayaActivity extends AppCompatActivity implements SensorEventListener {

    private SeleccionaPlayaBinding binding;
    
    // Sensor de movimiento para innovación (Shake)
    private SensorManager sensorManager;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    
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

        actualizarFondoSegunHora();

        // Mostrar nombre del usuario (Personalización)
        String nombre = getIntent().getStringExtra("USER_NAME");
        if (nombre != null && !nombre.isEmpty()) {
            binding.textViewTitle.setText(getString(R.string.welcome_user, nombre));
        }

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

        binding.btnLogout.setOnClickListener(v -> cerrarSesion());

        // Panel de Demo para la Expo (Cambio de hora manual)
        binding.btnManana.setOnClickListener(v -> actualizarFondoManual(8, "Mañana"));
        binding.btnTarde.setOnClickListener(v -> actualizarFondoManual(15, "Tarde"));
        binding.btnNoche.setOnClickListener(v -> actualizarFondoManual(22, "Noche"));

        // PLAN B: Si no puedes agitar el emulador, haz clic en el logo
        binding.imgLogo.setOnClickListener(v -> {
            // Animación de pulso para que se vea pro
            v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction(() -> {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
            }).start();
            elegirPlayaAleatoria();
        });

        // Inicializar Sensores (Innovación: Shake)
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleration = 10f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;
    }

    private void actualizarFondoSegunHora() {
        actualizarFondo(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
    }

    private void actualizarFondoManual(int hora, String modo) {
        actualizarFondo(hora);
        Toast.makeText(this, "Simulando modo: " + modo, Toast.LENGTH_SHORT).show();
    }

    private void actualizarFondo(int hora) {
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

    private void cerrarSesion() {
        // Firebase Sign out
        FirebaseAuth.getInstance().signOut();

        // Google Sign out
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(task -> {
            Toast.makeText(SeleccionaPlayaActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SeleccionaPlayaActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // --- Métodos del Sensor para Innovación (Shake to Random Beach) ---
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        lastAcceleration = currentAcceleration;
        currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);
        float delta = currentAcceleration - lastAcceleration;
        acceleration = acceleration * 0.9f + delta;

        if (acceleration > 12) { // Si el movimiento es brusco
            elegirPlayaAleatoria();
        }
    }

    private void elegirPlayaAleatoria() {
        if (!playasFiltradas.isEmpty()) {
            // Vibración para feedback físico
            Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            if (v != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(200);
                }
            }

            int index = new Random().nextInt(playasFiltradas.size());
            binding.spinnerPlayas.setSelection(index);
            Toast.makeText(this, "¡Playa sorpresa elegida!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

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