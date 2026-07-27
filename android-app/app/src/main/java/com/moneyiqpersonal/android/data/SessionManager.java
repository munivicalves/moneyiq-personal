package com.moneyiqpersonal.android.data;

import android.content.Context;
import android.content.SharedPreferences;

/** Guarda o token JWT e os dados do usuário autenticado em SharedPreferences. */
public class SessionManager {

    private static final String PREFS = "moneyiq_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_NOME = "nome";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String token, String nome, String email) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_NOME, nome)
                .putString(KEY_EMAIL, email)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getNome() {
        return prefs.getString(KEY_NOME, null);
    }

    public boolean isLoggedIn() {
        String token = getToken();
        return token != null && !token.isEmpty();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
