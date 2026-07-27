package com.sentinelprime.backend.repository;

import com.sentinelprime.backend.model.CartaoCredito;
import com.sentinelprime.backend.model.DespesaCartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface DespesaCartaoRepository extends JpaRepository<DespesaCartao, Long> {

    List<DespesaCartao> findByCartaoCreditoAndCompetenciaOrderByDescricaoAsc(
            CartaoCredito cartao, String competencia);

    List<DespesaCartao> findByCartaoCredito(CartaoCredito cartao);

    Optional<DespesaCartao> findByIdAndCartaoCredito(Long id, CartaoCredito cartao);

    boolean existsByCartaoCreditoId(Long cartaoId);

    @Query("SELECT COALESCE(SUM(d.valor), 0) FROM DespesaCartao d " +
           "WHERE d.cartaoCredito = :cartao AND d.competencia = :competencia")
    BigDecimal sumFaturaPorCartaoECompetencia(@Param("cartao") CartaoCredito cartao,
                                              @Param("competencia") String competencia);
}
