package com.sentinelprime.backend.model;

import com.sentinelprime.backend.model.enums.StatusDespesaFixa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "despesa_fixa")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DespesaFixa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despesa_fixa")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "descricao", length = 300, nullable = false)
    private String descricao;

    @Column(name = "valor", nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    /** Dia do mês em que vence (1–31) */
    @Column(name = "dia_vencimento", nullable = false)
    private Integer diaVencimento;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    /** Nulo quando indeterminado = true */
    @Column(name = "data_fim")
    private LocalDate dataFim;

    /** Se true, a despesa não tem data de encerramento prevista */
    @Column(name = "indeterminado", nullable = false)
    @Builder.Default
    private Boolean indeterminado = Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 10)
    @Builder.Default
    private StatusDespesaFixa status = StatusDespesaFixa.ATIVA;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (status == null) status = StatusDespesaFixa.ATIVA;
        if (indeterminado == null) indeterminado = Boolean.TRUE;
    }
}
