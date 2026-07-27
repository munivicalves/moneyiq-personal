package com.moneyiqpersonal.backend.dto.response;

import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaGastoResponse {

    private CategoriaDespesa categoria;
    private BigDecimal valor;
}