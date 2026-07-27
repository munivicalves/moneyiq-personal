package com.sentinelprime.backend.dto.response;

import com.sentinelprime.backend.model.enums.CategoriaDespesa;
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