package com.sentinelprime.backend.dto.response;

import com.sentinelprime.backend.model.enums.OrigemContaCorrente;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ContaCorrenteResponse {

    private BigDecimal saldoPeriodo;

    private List<ItemContaCorrente> transacoes;

    @Data
    @Builder
    public static class ItemContaCorrente {

        private Long id;

        private LocalDate data;

        private String descricao;

        private OrigemContaCorrente origem;

        private BigDecimal entrada;

        private BigDecimal saida;

        private BigDecimal saldo;
    }
}