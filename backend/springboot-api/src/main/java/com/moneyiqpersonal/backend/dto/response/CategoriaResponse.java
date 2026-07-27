package com.moneyiqpersonal.backend.dto.response;

import com.moneyiqpersonal.backend.model.Categoria;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CategoriaResponse {
    private Long id;
    private String nome;
    private String icone;
    private String cor;

    public static CategoriaResponse from(Categoria c) {
        return CategoriaResponse.builder()
                .id(c.getId())
                .nome(c.getNome())
                .icone(c.getIcone())
                .cor(c.getCor())
                .build();
    }
}
