package com.moneyiqpersonal.backend.model;

import com.moneyiqpersonal.backend.model.enums.TipoReceita;
import com.moneyiqpersonal.backend.model.enums.TipoTransacao;
import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transacao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 10)
    private TipoTransacao tipo;

    /** Subcategoria aplicável apenas a receitas: SALARIO, EXTRA, OUTROS */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_receita", length = 10)
    private TipoReceita tipoReceita;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_despesa", length = 30)
    private CategoriaDespesa categoriaDespesa;

    @Column(name = "despesa_fixa")
    private Boolean despesaFixa;

    @Column(name = "data_fim_recorrencia")
    private LocalDate dataFimRecorrencia;

    @Column(name = "id_recorrencia", length = 50)
    private String idRecorrencia;

    @Column(name = "descricao", length = 300)
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_transacao", nullable = false)
    private LocalDate dataTransacao;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }

    public boolean isReceita() {
        return TipoTransacao.RECEITA.equals(tipo);
    }

    public boolean isDespesa() {
        return TipoTransacao.DESPESA.equals(tipo);
    }
}
