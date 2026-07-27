package com.moneyiqpersonal.backend.dto.request;

import com.moneyiqpersonal.backend.model.enums.TipoReceita;
import com.moneyiqpersonal.backend.model.enums.TipoTransacao;
import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransacaoRequest {
    @NotNull(message = "Tipo é obrigatório")
    private TipoTransacao tipo;

    /** Obrigatório quando tipo = RECEITA */
    private TipoReceita tipoReceita;

    /** Obrigatório quando tipo = DESPESA */
    private CategoriaDespesa categoriaDespesa;

    private Boolean despesaFixa;

    private LocalDate dataFimRecorrencia;

    private String idRecorrencia;

    @NotBlank(message = "Descrição é obrigatória")
    private String descricao;

    @NotNull @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Data é obrigatória")
    private LocalDate dataTransacao;

    @AssertTrue(message = "Tipo de receita é obrigatório para transações de RECEITA")
    public boolean isTipoReceitaValido() {
        return !TipoTransacao.RECEITA.equals(tipo) || tipoReceita != null;
    }

    @AssertTrue(message = "Categoria é obrigatória para transações de DESPESA")
    public boolean isCategoriaDespesaValida() {
        return !TipoTransacao.DESPESA.equals(tipo) || categoriaDespesa != null;
    }
}
