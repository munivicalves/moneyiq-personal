package com.sentinelprime.backend.repository;

import com.sentinelprime.backend.model.CartaoCredito;
import com.sentinelprime.backend.model.FaturaCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FaturaCartaoRepository extends JpaRepository<FaturaCartao, Long> {
    List<FaturaCartao> findByCartaoCreditoOrderByCompetenciaDesc(CartaoCredito cartao);
    Optional<FaturaCartao> findByCartaoCreditoAndCompetencia(CartaoCredito cartao, String competencia);
    List<FaturaCartao> findByCartaoCreditoAndPagaFalse(CartaoCredito cartao);
}
