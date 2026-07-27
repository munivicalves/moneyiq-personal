package com.moneyiqpersonal.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class DashboardResponse {
    private String competencia;
    private BigDecimal totalReceitas;
    private BigDecimal totalDespesas;
    private BigDecimal totalFixas;
    private BigDecimal saldo;
    private List<MesChartItem> chartData;
    private FixasVariaveisItem fixasVsVariaveis;

    @Data @Builder
    public static class MesChartItem {
        private String mes;
        private BigDecimal receitas;
        private BigDecimal despesas;
    }

    @Data @Builder
    public static class FixasVariaveisItem {
        private BigDecimal fixas;
        private BigDecimal variaveis;
    }
}
