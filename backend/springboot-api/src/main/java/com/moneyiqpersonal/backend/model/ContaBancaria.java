package com.moneyiqpersonal.backend.model;

import com.moneyiqpersonal.backend.model.enums.TipoConta;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "conta_bancaria")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ContaBancaria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conta")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Column(name = "banco", length = 100)
    private String banco;

    @Column(name = "agencia", length = 20)
    private String agencia;

    @Column(name = "numero_conta", length = 30)
    private String numeroConta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", nullable = false, length = 10)
    @Builder.Default
    private TipoConta tipoConta = TipoConta.CORRENTE;

    @Column(name = "saldo_inicial", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal saldoInicial = BigDecimal.ZERO;

    @Column(name = "ativa", nullable = false)
    @Builder.Default
    private Boolean ativa = Boolean.TRUE;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        if (tipoConta == null) tipoConta = TipoConta.CORRENTE;
        if (saldoInicial == null) saldoInicial = BigDecimal.ZERO;
        if (ativa == null) ativa = Boolean.TRUE;
    }
}
