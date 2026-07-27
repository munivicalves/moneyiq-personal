package com.sentinelprime.backend.dto.response;

import com.sentinelprime.backend.model.CartaoCredito;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CartaoCreditoResponse {
    private Long id;
    private String nome;
    private String bandeira;
    private Integer diaVencimento;

    public static CartaoCreditoResponse from(CartaoCredito c) {
        return CartaoCreditoResponse.builder()
                .id(c.getId())
                .nome(c.getNome())
                .bandeira(c.getBandeira())
                .diaVencimento(c.getDiaVencimento())
                .build();
    }
}
