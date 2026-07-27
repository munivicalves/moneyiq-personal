package com.sentinelprime.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.sentinelprime.android.data.ApiClient;
import com.sentinelprime.android.data.SessionManager;
import com.sentinelprime.android.data.model.AuthResponse;
import com.sentinelprime.android.data.model.LoginRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton submitButton;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = new SessionManager(this);

        emailInput = findViewById(R.id.et_login_email);
        passwordInput = findViewById(R.id.et_login_password);
        submitButton = findViewById(R.id.btn_login_submit);
        MaterialButton registerButton = findViewById(R.id.btn_login_register);

        submitButton.setOnClickListener(v -> doLogin());
        registerButton.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void doLogin() {
        String email = textOf(emailInput);
        String senha = textOf(passwordInput);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Informe e-mail e senha", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);
        ApiClient.getApi(this).login(new LoginRequest(email, senha))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> resp) {
                        setLoading(false);
                        if (resp.isSuccessful() && resp.body() != null && resp.body().token != null) {
                            AuthResponse body = resp.body();
                            session.save(body.token, body.nome, body.email);
                            goHome();
                        } else if (resp.code() == 401 || resp.code() == 400) {
                            Toast.makeText(LoginActivity.this,
                                    "E-mail ou senha inválidos", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(LoginActivity.this,
                                    "Falha no login (código " + resp.code() + ")", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(LoginActivity.this,
                                "Sem conexão com o servidor", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void goHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void setLoading(boolean loading) {
        submitButton.setEnabled(!loading);
        submitButton.setText(loading ? "Entrando..." : getString(R.string.action_login));
    }

    private String textOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
