package com.moneyiqpersonal.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class ReceitaResumoResponse {
    private String competencia;
    private BigDecimal totalReceitas;
    private BigDecimal totalSalario;
    private BigDecimal totalExtra;
    private List<TransacaoResponse> transacoes;
}
