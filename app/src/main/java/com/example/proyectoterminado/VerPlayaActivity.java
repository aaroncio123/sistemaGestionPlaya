package com.example.proyectoterminado;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoterminado.databinding.ActivityVerPlayaBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VerPlayaActivity extends AppCompatActivity {

    private ActivityVerPlayaBinding binding;
    private double playaLat, playaLng;
    
    // Dirección exacta para el emulador
    private final String MI_IP = "10.0.2.2";
    private String beachName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerPlayaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        beachName = getIntent().getStringExtra("BEACH_NAME");
        binding.txtNombrePlayaDetalle.setText(beachName);

        cargarDetallesServidor();

        binding.btnIrMapa.setOnClickListener(v -> {
            if (playaLat != 0) {
                Uri gmmIntentUri = Uri.parse("geo:" + playaLat + "," + playaLng + "?q=" + beachName);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        binding.btnRegresar.setOnClickListener(v -> finish());
    }

    private void cargarDetallesServidor() {
        String url = "http://" + MI_IP + "/playaGestion/get_detalles_playas.php?playa=" + beachName;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d("VOLLEY_OK", "Respuesta recibida: " + response.toString());
                    try {
                        // 1. Coordenadas
                        if (!response.isNull("datos")) {
                            JSONObject datos = response.getJSONObject("datos");
                            playaLat = datos.getDouble("latitud");
                            playaLng = datos.getDouble("longitud");
                        }

                        // 2. Comentarios
                        JSONArray coments = response.getJSONArray("comentarios");
                        binding.layoutComentarios.removeAllViews();
                        
                        if (coments.length() == 0) {
                            TextView tv = new TextView(this);
                            tv.setText("No hay comentarios todavía.");
                            binding.layoutComentarios.addView(tv);
                        } else {
                            for (int i = 0; i < coments.length(); i++) {
                                JSONObject c = coments.getJSONObject(i);
                                TextView tv = new TextView(this);
                                tv.setPadding(10, 20, 10, 20);
                                tv.setTextSize(16);
                                
                                String usuario = c.optString("nombre_completo", "Anónimo");
                                String calif = c.optString("calificacion_estrellas", "0");
                                String texto = c.optString("comentario", "");
                                String fotoUrl = c.optString("foto_url", "");

                                String contenido = usuario + " (" + calif + "★)\n\"" + texto + "\"";
                                tv.setText(contenido);
                                binding.layoutComentarios.addView(tv);

                                if (!fotoUrl.equals("nula") && !fotoUrl.isEmpty()) {
                                    ImageView iv = new ImageView(this);
                                    // Ajustar tamaño de la imagen
                                    android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                                            android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
                                            600 // Altura en píxeles (puedes ajustarlo)
                                    );
                                    params.setMargins(10, 0, 10, 30);
                                    iv.setLayoutParams(params);
                                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

                                    // Cargar la imagen real desde el servidor XAMPP
                                    String urlFoto = "http://" + MI_IP + "/playaGestion/fotos/" + fotoUrl;
                                    Glide.with(this).load(urlFoto).into(iv);
                                    
                                    binding.layoutComentarios.addView(iv);
                                }
                            }
                        }

                    } catch (JSONException e) { 
                        Log.e("VOLLEY_ERR", "Error parseo: " + e.getMessage());
                        e.printStackTrace(); 
                    }
                },
                error -> {
                    String msg = "Error de conexión";
                    if (error instanceof com.android.volley.TimeoutError) msg = "Tiempo de espera agotado";
                    else if (error.networkResponse != null) msg = "Error: " + error.networkResponse.statusCode;
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    Log.e("VOLLEY_ERR", "Fallo total: " + error.toString());
                }
        );
        
        // Damos 10 segundos de margen para conectar
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                10000, 
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }
}