package com.moneyiqpersonal.android.data;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fornece a instância do {@link ApiService} apontando para o backend do
 * MoneyIQ Personal, injetando o token JWT (quando houver) no header Authorization.
 *
 * Produção (Render): https://moneyiq-personal-api.onrender.com/api/
 * Emulador apontando ao backend local: http://10.0.2.2:8080/api/
 */
public final class ApiClient {

    private static final String BASE_URL = "https://moneyiq-personal-api.onrender.com/api/";

    private static volatile ApiService service;

    private ApiClient() {
    }

    public static ApiService getApi(Context context) {
        if (service == null) {
            synchronized (ApiClient.class) {
                if (service == null) {
                    service = build(context.getApplicationContext());
                }
            }
        }
        return service;
    }

    private static ApiService build(Context appContext) {
        final SessionManager session = new SessionManager(appContext);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    String token = session.getToken();
                    if (token != null && !token.isEmpty()) {
                        return chain.proceed(
                                original.newBuilder()
                                        .header("Authorization", "Bearer " + token)
                                        .build());
                    }
                    return chain.proceed(original);
                })
                // O plano free do Render hiberna; o cold start pode demorar.
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService.class);
    }
}
