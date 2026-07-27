package com.moneyiqpersonal.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;

@Entity
@Table(name = "despesa_cartao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DespesaCartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despesa_cartao")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cartao", nullable = false)
    private CartaoCredito cartaoCredito;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fatura")
    private FaturaCartao fatura;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_despesa", nullable = false)
    private CategoriaDespesa categoriaDespesa;

    @Column(name = "descricao", length = 300, nullable = false)
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    /** Parcela atual (ex: 1 de "1/10") */
    @Column(name = "parcela_atual", nullable = false)
    @Builder.Default
    private Integer parcelaAtual = 1;

    /** Total de parcelas (ex: 10 de "1/10") */
    @Column(name = "total_parcelas", nullable = false)
    @Builder.Default
    private Integer totalParcelas = 1;

        /** Competência da fatura (2026-06) */
    @Column(name = "competencia", length = 7, nullable = false)
    private String competencia;

    /** Data real da compra */
    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }
}
