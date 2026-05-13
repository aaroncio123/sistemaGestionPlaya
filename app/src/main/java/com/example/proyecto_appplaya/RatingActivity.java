package com.example.proyecto_appplaya;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RatingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        RatingBar ratingBar = findViewById(R.id.rating_bar);
        EditText etComment = findViewById(R.id.et_comment);
        Button btnUploadPhoto = findViewById(R.id.btn_upload_photo);
        Button btnSubmit = findViewById(R.id.btn_submit_rating);

        btnUploadPhoto.setOnClickListener(v -> {
            Toast.makeText(this, "Funcionalidad de subir foto", Toast.LENGTH_SHORT).show();
        });

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String comment = etComment.getText().toString();
            
            if (rating == 0) {
                Toast.makeText(this, "Por favor, califica la playa", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Calificación enviada: " + rating + " estrellas", Toast.LENGTH_SHORT).show();
                finish(); // Regresar a la pantalla anterior o ir a confirmación? El flujo dice que exploración lleva a confirmación.
            }
        });
    }
}
