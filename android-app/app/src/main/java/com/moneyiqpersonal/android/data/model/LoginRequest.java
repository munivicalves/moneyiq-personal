package com.moneyiqpersonal.android.data.model;

/** Corpo de POST /api/auth/login. */
public class LoginRequest {
    public final String email;
    public final String senha;

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}
