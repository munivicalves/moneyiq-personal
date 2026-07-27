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
import com.sentinelprime.android.data.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton submitButton;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new SessionManager(this);

        nameInput = findViewById(R.id.et_register_name);
        emailInput = findViewById(R.id.et_register_email);
        passwordInput = findViewById(R.id.et_register_password);
        submitButton = findViewById(R.id.btn_register_submit);
        MaterialButton backButton = findViewById(R.id.btn_register_back);

        submitButton.setOnClickListener(v -> doRegister());
        backButton.setOnClickListener(v -> finish());
    }

    private void doRegister() {
        String nome = textOf(nameInput);
        String email = textOf(emailInput);
        String senha = textOf(passwordInput);

        if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
            Toast.makeText(this, "Preencha nome, e-mail e senha", Toast.LENGTH_SHORT).show();
            return;
        }
        if (senha.length() < 6) {
            Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_LONG).show();
            return;
        }

        setLoading(true);
        ApiClient.getApi(this).register(new RegisterRequest(nome, email, senha))
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> resp) {
                        setLoading(false);
                        if (resp.isSuccessful() && resp.body() != null && resp.body().token != null) {
                            AuthResponse body = resp.body();
                            session.save(body.token, body.nome, body.email);
                            startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                            finishAffinity();
                        } else if (resp.code() == 409 || resp.code() == 400) {
                            Toast.makeText(RegisterActivity.this,
                                    "E-mail já cadastrado ou dados inválidos", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Falha no cadastro (código " + resp.code() + ")", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(RegisterActivity.this,
                                "Sem conexão com o servidor", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoading(boolean loading) {
        submitButton.setEnabled(!loading);
        submitButton.setText(loading ? "Enviando..." : getString(R.string.action_create_account));
    }

    private String textOf(TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }
}
