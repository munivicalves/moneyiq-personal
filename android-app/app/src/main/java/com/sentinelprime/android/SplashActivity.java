package com.sentinelprime.android;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.sentinelprime.android.data.SessionManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Se já há sessão ativa, vai direto para a Home.
        if (new SessionManager(this).isLoggedIn()) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);

        MaterialButton loginButton = findViewById(R.id.btn_splash_login);
        MaterialButton registerButton = findViewById(R.id.btn_splash_register);

        loginButton.setOnClickListener(v -> open(LoginActivity.class));
        registerButton.setOnClickListener(v -> open(RegisterActivity.class));
    }

    private void open(Class<?> activityClass) {
        startActivity(new Intent(this, activityClass));
    }
}
