package com.moneyiqpersonal.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Fatura de cartão materializada (uma por cartão/competência). Convive com o
 * read-model {@code FaturaCartaoResponse}, que calcula a fatura sob demanda a
 * partir das {@link DespesaCartao}.
 */
@Entity
@Table(
    name = "fatura_cartao",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_fatura_cartao_competencia",
        columnNames = {"id_cartao", "competencia"}
    )
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class FaturaCartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fatura")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cartao", nullable = false)
    private CartaoCredito cartaoCredito;

    /** Competência da fatura (ex.: 2026-06) */
    @Column(name = "competencia", length = 7, nullable = false)
    private String competencia;

    @Column(name = "valor_total", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Column(name = "paga", nullable = false)
    @Builder.Default
    private Boolean paga = Boolean.FALSE;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "fatura", fetch = FetchType.LAZY)
    @Builder.Default
    private List<DespesaCartao> despesas = new ArrayList<>();

    @PrePersist
    protected void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (valorTotal == null) valorTotal = BigDecimal.ZERO;
        if (paga == null) paga = Boolean.FALSE;
    }
}
