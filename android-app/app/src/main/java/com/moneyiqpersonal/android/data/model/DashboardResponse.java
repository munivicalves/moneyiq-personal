package com.moneyiqpersonal.android.data.model;

import java.math.BigDecimal;

/** Resposta de GET /api/dashboard?competencia=YYYY-MM. */
public class DashboardResponse {
    public String competencia;
    public BigDecimal totalReceitas;
    public BigDecimal totalDespesas;
    public BigDecimal totalFixas;
    public BigDecimal saldo;
}
