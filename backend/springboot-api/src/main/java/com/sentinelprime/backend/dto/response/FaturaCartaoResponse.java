package com.sentinelprime.backend.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data @Builder
public class FaturaCartaoResponse {
    private Long cartaoId;
    private String cartaoNome;
    private String competencia;
    private BigDecimal totalFatura;
    private List<DespesaCartaoResponse> despesas;
}
