package com.moneyiqpersonal.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DespesaCartaoRequest {
    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull @DecimalMin("0.01")
    private BigDecimal valor;

    @NotNull(message = "Categoria é obrigatória")
    private CategoriaDespesa categoriaDespesa;

    @NotNull @Min(1)
    private Integer parcelaAtual;

    @NotNull @Min(1)
    private Integer totalParcelas;

    /** Formato: YYYY-MM */
    @NotBlank @Pattern(regexp = "\\d{4}-\\d{2}", message = "Competência deve ser no formato YYYY-MM")
    private String competencia;

    @NotNull
    private LocalDate dataCompra;
}
