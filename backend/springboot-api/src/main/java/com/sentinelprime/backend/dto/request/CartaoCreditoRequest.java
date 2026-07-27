package com.sentinelprime.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CartaoCreditoRequest {
    @NotBlank(message = "Nome do cartão é obrigatório")
    @Size(max = 100)
    private String nome;

    @Size(max = 50)
    private String bandeira;

    private Integer diaVencimento;
}
