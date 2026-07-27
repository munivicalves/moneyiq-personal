package com.moneyiqpersonal.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


    @Data
    @Builder
    public class DashboardHistoricoResponse {

        private String competencia;

        private BigDecimal receitas;

        private BigDecimal despesas;

        private BigDecimal saldo;
    }
