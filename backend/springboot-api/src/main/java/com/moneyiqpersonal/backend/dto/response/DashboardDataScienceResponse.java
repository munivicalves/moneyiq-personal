package com.moneyiqpersonal.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class DashboardDataScienceResponse {

    private String competencia;

    private BigDecimal receitas;

    private BigDecimal despesas;

    private BigDecimal saldo;

    private BigDecimal fixas;

    private BigDecimal variaveis;

    
}