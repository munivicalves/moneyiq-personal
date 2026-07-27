package com.moneyiqpersonal.android.data.model;

/** Corpo de POST /api/auth/register. */
public class RegisterRequest {
    public final String nome;
    public final String email;
    public final String senha;

    public RegisterRequest(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}
