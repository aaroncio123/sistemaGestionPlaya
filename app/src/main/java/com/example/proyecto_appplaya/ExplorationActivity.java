package com.example.proyecto_appplaya;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Arrays;
import java.util.List;

public class ExplorationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exploration);

        RecyclerView rvComments = findViewById(R.id.rv_comments);
        Button btnGo = findViewById(R.id.btn_go);
        Button btnSearchAnother = findViewById(R.id.btn_search_another);

        // Configuración del RecyclerView con datos de ejemplo
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        List<String> dummyComments = Arrays.asList(
            "¡La mejor playa de la zona!",
            "Muy limpia y tranquila por la mañana.",
            "El agua está perfecta, pero hay mucha gente el fin de semana.",
            "Ideal para hacer surf."
        );
        rvComments.setAdapter(new SimpleCommentAdapter(dummyComments));
        
        btnGo.setOnClickListener(v -> {
            Intent intent = new Intent(ExplorationActivity.this, ConfirmationActivity.class);
            startActivity(intent);
        });

        btnSearchAnother.setOnClickListener(v -> finish());
    }

    // Adaptador simple interno para mostrar los comentarios
    private static class SimpleCommentAdapter extends RecyclerView.Adapter<SimpleCommentAdapter.ViewHolder> {
        private final List<String> comments;

        SimpleCommentAdapter(List<String> comments) { this.comments = comments; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(comments.get(position));
        }

        @Override
        public int getItemCount() { return comments.size(); }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;
            ViewHolder(View v) { super(v); textView = v.findViewById(R.id.tv_comment_text); }
        }
    }
}
