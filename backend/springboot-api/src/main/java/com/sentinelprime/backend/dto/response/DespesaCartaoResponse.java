package com.sentinelprime.backend.dto.response;

import com.sentinelprime.backend.model.DespesaCartao;
import com.sentinelprime.backend.model.enums.CategoriaDespesa;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @Builder
public class DespesaCartaoResponse {
    private Long id;
    private String descricao;
    private BigDecimal valor;
    private CategoriaDespesa categoriaDespesa;
    private Integer parcelaAtual;
    private Integer totalParcelas;
    private String parcela;
    private String competencia;
    private LocalDateTime dataCriacao;
    private LocalDate dataCompra;

    public static DespesaCartaoResponse from(DespesaCartao d) {
        return DespesaCartaoResponse.builder()
                .id(d.getId())
                .descricao(d.getDescricao())
                .valor(d.getValor())
                .categoriaDespesa(d.getCategoriaDespesa())
                .parcelaAtual(d.getParcelaAtual())
                .totalParcelas(d.getTotalParcelas())
                .parcela(d.getParcelaAtual() + "/" + d.getTotalParcelas())
                .competencia(d.getCompetencia())
                .dataCriacao(d.getDataCriacao())
                .dataCompra(d.getDataCompra())
                .build();
    }
}
