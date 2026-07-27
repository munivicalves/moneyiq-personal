package com.sentinelprime.backend.dto.response;

import com.sentinelprime.backend.model.DespesaFixa;
import com.sentinelprime.backend.model.enums.StatusDespesaFixa;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder
public class DespesaFixaResponse {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private Integer diaVencimento;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Boolean indeterminado;
    private StatusDespesaFixa status;

    public static DespesaFixaResponse from(DespesaFixa df) {
        return DespesaFixaResponse.builder()
                .id(df.getId())
                .descricao(df.getDescricao())
                .valor(df.getValor())
                .diaVencimento(df.getDiaVencimento())
                .dataInicio(df.getDataInicio())
                .dataFim(df.getDataFim())
                .indeterminado(df.getIndeterminado())
                .status(df.getStatus())
                .build();
    }
}
