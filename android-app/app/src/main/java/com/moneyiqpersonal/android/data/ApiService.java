package com.moneyiqpersonal.android.data;

import com.moneyiqpersonal.android.data.model.AuthResponse;
import com.moneyiqpersonal.android.data.model.DashboardResponse;
import com.moneyiqpersonal.android.data.model.LoginRequest;
import com.moneyiqpersonal.android.data.model.RegisterRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/** Endpoints da API REST do MoneyIQ Personal consumidos pelo app. */
public interface ApiService {

    @POST("auth/login")
    Call<AuthResponse> login(@Body LoginRequest body);

    @POST("auth/register")
    Call<AuthResponse> register(@Body RegisterRequest body);

    @GET("dashboard")
    Call<DashboardResponse> dashboard(@Query("competencia") String competencia);
}
