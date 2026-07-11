package com.example.proyectoterminado;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.proyectoterminado.databinding.ActivityCalificarBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class CalificarActivity extends AppCompatActivity {

    private ActivityCalificarBinding binding;
    private ActivityResultLauncher<String> getContentLauncher;
    private String imagenBase64 = ""; // Aquí guardaremos la foto convertida a texto
    
    private final String URL_API = "http://10.0.2.2/playaGestion/guardar_reporte.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalificarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getContentLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(uri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            imagenBase64 = convertirBitmapABase64(bitmap);
                            Toast.makeText(this, "Foto lista para enviar", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

        String beachName = getIntent().getStringExtra("BEACH_NAME");

        binding.btnSubirFoto.setOnClickListener(v -> getContentLauncher.launch("image/*"));

        binding.btnEnviarCalificacion.setOnClickListener(v -> enviarDatosServidor(beachName));
    }

    private String convertirBitmapABase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void enviarDatosServidor(String playa) {
        String comentario = binding.editComentario.getText().toString();
        if (comentario.isEmpty()) {
            Toast.makeText(this, "Escribe un comentario", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, URL_API,
                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show();
                    finish();
                },
                //Se asignan validaciones para determinar si la caida es por estos motivos
                error -> {
                    String errorMessage = "Error: ";
                    if (error instanceof com.android.volley.TimeoutError) {
                        errorMessage += "Tiempo de espera agotado";
                    } else if (error instanceof com.android.volley.NoConnectionError) {
                        errorMessage += "No hay conexión con el servidor";
                    } else if (error.networkResponse != null) {
                        errorMessage += "Error del servidor: " + error.networkResponse.statusCode;
                    } else {
                        errorMessage += error.getMessage();
                    }
                    android.util.Log.e("API_ERROR", "Detalle: " + errorMessage);
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                
                // Obtener datos del usuario de Firebase
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String nombreUsuario = "Invitado";
                String correoUsuario = "anonimo@playas.com";
                
                if (user != null) {
                    if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                        nombreUsuario = user.getDisplayName();
                    }
                    if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                        correoUsuario = user.getEmail();
                    }
                }

                params.put("playa", playa);
                params.put("usuario_nombre", nombreUsuario);
                params.put("usuario_correo", correoUsuario);
                params.put("estrellas", String.valueOf(binding.ratingBar.getRating()));
                params.put("limpieza", binding.spinnerLimpieza.getSelectedItem().toString());
                params.put("afluencia", binding.spinnerAfluencia.getSelectedItem().toString());
                params.put("clima", binding.spinnerClima.getSelectedItem().toString());
                params.put("seguridad", binding.spinnerSeguridad.getSelectedItem().toString());
                params.put("comentario", comentario);
                params.put("foto", imagenBase64);
                return params;
            }
        };

        // Aumentar el tiempo de espera a 30 segundos
        postRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                30000,
                com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(postRequest);
    }
}