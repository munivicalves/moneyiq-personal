package com.moneyiqpersonal.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DespesaFixaRequest {
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull @DecimalMin("0.01")
    private BigDecimal valor;

    @NotNull @Min(1) @Max(31)
    private Integer diaVencimento;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    private LocalDate dataFim;

    @NotNull
    private Boolean indeterminado;
}
