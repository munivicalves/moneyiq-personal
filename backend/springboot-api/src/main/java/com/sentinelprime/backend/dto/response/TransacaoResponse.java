package com.sentinelprime.backend.dto.response;

import com.sentinelprime.backend.model.Transacao;
import com.sentinelprime.backend.model.enums.TipoReceita;
import com.sentinelprime.backend.model.enums.TipoTransacao;
import com.sentinelprime.backend.model.enums.CategoriaDespesa;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @Builder
public class TransacaoResponse {
    private Long id;
    private TipoTransacao tipo;
    private TipoReceita tipoReceita;
    private CategoriaDespesa categoriaDespesa;
    private Boolean despesaFixa;
    private LocalDate dataFimRecorrencia;
    private String idRecorrencia;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataTransacao;

    public static TransacaoResponse from(Transacao t) {
        return TransacaoResponse.builder()
        .id(t.getId())
        .tipo(t.getTipo())
        .tipoReceita(t.getTipoReceita())
        .categoriaDespesa(t.getCategoriaDespesa())
        .despesaFixa(t.getDespesaFixa())
        .dataFimRecorrencia(t.getDataFimRecorrencia())
        .idRecorrencia(t.getIdRecorrencia())
        .descricao(t.getDescricao())
        .valor(t.getValor())
        .dataTransacao(t.getDataTransacao())
        .build();
    }
}
