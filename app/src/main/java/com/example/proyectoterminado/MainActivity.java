package com.example.proyectoterminado;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.proyectoterminado.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "GoogleSignIn";

    @Override
    protected void onStart() {
        super.onStart();
        // Al iniciar, verificamos si el usuario ya está logueado en Firebase
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            irASeleccionarPlaya();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this); // Comentado para evitar pantalla negra en APIs nuevas

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Usamos el ID de cliente web automático generado desde google-services.json
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.button.setOnClickListener(view -> signIn());

        binding.btnInvitado.setOnClickListener(v -> irASeleccionarPlaya());

        // BOTÓN DE EMERGENCIA: Mantén pulsado el logo
        binding.main.setOnLongClickListener(v -> {
            irASeleccionarPlaya();
            return true;
        });
    }

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d(TAG, "Resultado del launcher recibido. Código: " + result.getResultCode());
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "Google Sign-In exitoso: " + account.getEmail());
                        Toast.makeText(this, "Google OK, autenticando en Firebase...", Toast.LENGTH_SHORT).show();
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        Log.e(TAG, "Error en Google Sign-In (ApiException). Código: " + e.getStatusCode(), e);
                        String mensaje = "Error Google: " + e.getStatusCode();
                        if (e.getStatusCode() == 10) mensaje += " (ID Cliente o SHA-1 mal)";
                        if (e.getStatusCode() == 12500) mensaje += " (Falta google-services.json)";
                        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.w(TAG, "Inicio cancelado o error. Código: " + result.getResultCode());
                    Toast.makeText(this, "Inicio cancelado (Código: " + result.getResultCode() + ")", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private void signIn() {
        Log.d(TAG, "Iniciando proceso de signIn()");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInLauncher.launch(signInIntent);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        Log.d(TAG, "Autenticando con Firebase usando ID Token...");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Autenticación Firebase EXITOSA");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Log.e(TAG, "Autenticación Firebase FALLIDA", task.getException());
                        Toast.makeText(MainActivity.this, "Fallo en Firebase: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            irASeleccionarPlaya();
        }
    }

    private void irASeleccionarPlaya() {
        Intent intent = new Intent(MainActivity.this, SeleccionaPlayaActivity.class);
        startActivity(intent);
        finish(); // Cerramos el login para que no se pueda volver atrás
    }
}