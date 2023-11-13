package com.leanova.asistenciaapp.ui.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.leanova.asistenciaapp.R;

import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    private EditText etMail_L, etPass_L;
    private TextView tvAviso_L;
    private Button btIngresar_L;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(LoginViewModel.class);

        armarVista();
        tryToLogin();

        loginViewModel.getAviso().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvAviso_L.setText(s);
            }
        });
    }

    private void armarVista() {
        this.etMail_L = findViewById(R.id.etMail_L);
        this.etPass_L = findViewById(R.id.etPass_L);
        this.tvAviso_L = findViewById(R.id.tvAviso_L);
        tvAviso_L.setText("");

        this.btIngresar_L = findViewById(R.id.btIngresar_L);
        btIngresar_L.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = etMail_L.getText().toString();
                String pass = etPass_L.getText().toString();

                loginViewModel.iniciarSesion(mail, pass);
            }
        });
    }

    private void tryToLogin() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Usá tu huella para continuar")
                .setNegativeButtonText("Cancel")
                .build();
        getPrompt().authenticate(promptInfo);
    }

    private BiometricPrompt getPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                loginViewModel.tryToLogin();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(LoginActivity.this, "Error en verificar", Toast.LENGTH_SHORT).show();
            }
        };

        BiometricPrompt biometricPrompt =new BiometricPrompt(this, executor, callback);

        return biometricPrompt;
    }
}