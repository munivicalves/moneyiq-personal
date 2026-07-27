package com.sentinelprime.android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.sentinelprime.android.data.ApiClient;
import com.sentinelprime.android.data.SessionManager;
import com.sentinelprime.android.data.model.DashboardResponse;
import com.sentinelprime.android.home.HomeSection;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private static final Locale PT_BR = new Locale("pt", "BR");

    private TextView homeSectionTitle;
    private TextView homeSectionDescription;
    private TextView valueReceitas;
    private TextView valueDespesas;
    private TextView valueSaldo;
    private Button tabDashboard;
    private Button tabReceitas;
    private Button tabDespesas;
    private Button tabConta;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        bindViews();
        bindActions();
        selectTab(R.id.tab_dashboard);
        carregarDashboard();
    }

    private void bindViews() {
        homeSectionTitle = findViewById(R.id.home_section_title);
        homeSectionDescription = findViewById(R.id.home_section_description);
        valueReceitas = findViewById(R.id.tv_value_receitas);
        valueDespesas = findViewById(R.id.tv_value_despesas);
        valueSaldo = findViewById(R.id.tv_value_saldo);
        tabDashboard = findViewById(R.id.tab_dashboard);
        tabReceitas = findViewById(R.id.tab_receitas);
        tabDespesas = findViewById(R.id.tab_despesas);
        tabConta = findViewById(R.id.tab_conta);
    }

    private void bindActions() {
        tabDashboard.setOnClickListener(v -> selectTab(R.id.tab_dashboard));
        tabReceitas.setOnClickListener(v -> selectTab(R.id.tab_receitas));
        tabDespesas.setOnClickListener(v -> selectTab(R.id.tab_despesas));
        tabConta.setOnClickListener(v -> selectTab(R.id.tab_conta));
    }

    /** Busca os totais do período atual na API e popula os cards de resumo. */
    private void carregarDashboard() {
        String competencia = YearMonth.now().toString(); // formato YYYY-MM
        ApiClient.getApi(this).dashboard(competencia)
                .enqueue(new Callback<DashboardResponse>() {
                    @Override
                    public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> resp) {
                        if (resp.isSuccessful() && resp.body() != null) {
                            preencherCards(resp.body());
                        } else if (resp.code() == 401 || resp.code() == 403) {
                            session.clear();
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(HomeActivity.this,
                                    "Não foi possível carregar o resumo", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<DashboardResponse> call, Throwable t) {
                        Toast.makeText(HomeActivity.this,
                                "Sem conexão com o servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void preencherCards(DashboardResponse d) {
        valueReceitas.setText(formatCurrency(d.totalReceitas));
        valueDespesas.setText(formatCurrency(d.totalDespesas));
        valueSaldo.setText(formatCurrency(d.saldo));
    }

    private String formatCurrency(BigDecimal value) {
        BigDecimal v = value == null ? BigDecimal.ZERO : value;
        return NumberFormat.getCurrencyInstance(PT_BR).format(v);
    }

    private void selectTab(@IdRes int tabId) {
        resetTabStyles();

        if (tabId == R.id.tab_dashboard) {
            applySection(HomeSection.DASHBOARD, tabDashboard);
        } else if (tabId == R.id.tab_receitas) {
            applySection(HomeSection.RECEITAS, tabReceitas);
        } else if (tabId == R.id.tab_despesas) {
            applySection(HomeSection.DESPESAS, tabDespesas);
        } else if (tabId == R.id.tab_conta) {
            applySection(HomeSection.CONTA, tabConta);
        }
    }

    private void applySection(HomeSection section, Button selectedButton) {
        highlightTab(selectedButton);
        homeSectionTitle.setText(section.getTitleRes());
        homeSectionDescription.setText(section.getDescriptionRes());
    }

    private void resetTabStyles() {
        Button[] tabs = {tabDashboard, tabReceitas, tabDespesas, tabConta};
        for (Button tab : tabs) {
            tab.setBackgroundResource(R.drawable.bg_tab_default);
            tab.setTextColor(getColor(R.color.navy_700));
        }
    }

    private void highlightTab(Button tab) {
        tab.setBackgroundResource(R.drawable.bg_tab_selected);
        tab.setTextColor(getColor(android.R.color.white));
    }
}
