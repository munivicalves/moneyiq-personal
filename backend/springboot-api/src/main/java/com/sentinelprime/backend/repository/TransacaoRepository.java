package com.sentinelprime.backend.repository;

import com.sentinelprime.backend.dto.response.CategoriaGastoResponse;
import com.sentinelprime.backend.model.Transacao;
import com.sentinelprime.backend.model.Usuario;
import com.sentinelprime.backend.model.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByUsuarioAndDataTransacaoBetweenOrderByDataTransacaoDesc(
            Usuario usuario, LocalDate inicio, LocalDate fim);

    List<Transacao> findByUsuarioAndTipoAndDataTransacaoBetweenOrderByDataTransacaoDesc(
            Usuario usuario, TipoTransacao tipo, LocalDate inicio, LocalDate fim);

        @Query("""
            SELECT t FROM Transacao t
            WHERE t.usuario = :usuario
              AND t.dataTransacao BETWEEN :inicio AND :fim
              AND (:descricao IS NULL OR LOWER(t.descricao) LIKE LOWER(CONCAT('%', :descricao, '%')))
            ORDER BY t.dataTransacao DESC
            """)
    List<Transacao> findContaCorrente(@Param("usuario") Usuario usuario,
                                      @Param("inicio") LocalDate inicio,
                                      @Param("fim") LocalDate fim,
                                      @Param("descricao") String descricao);

        @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t " +
           "WHERE t.usuario = :usuario AND t.tipo = :tipo " +
           "AND t.dataTransacao BETWEEN :inicio AND :fim")
    BigDecimal sumValorByUsuarioAndTipoAndPeriodo(@Param("usuario") Usuario usuario,
                                                  @Param("tipo") TipoTransacao tipo,
                                                  @Param("inicio") LocalDate inicio,
                                                  @Param("fim") LocalDate fim);

        @Query("""
        SELECT new com.sentinelprime.backend.dto.response.CategoriaGastoResponse(
        t.categoriaDespesa,
        SUM(t.valor)
        )
        FROM Transacao t
        WHERE t.usuario = :usuario
        AND t.tipo = 'DESPESA'
        AND t.dataTransacao BETWEEN :inicio AND :fim
        GROUP BY t.categoriaDespesa
        """)
        List<CategoriaGastoResponse> buscarDespesasAgrupadasPorCategoria(
                @Param("usuario") Usuario usuario,
                @Param("inicio") LocalDate inicio,
                @Param("fim") LocalDate fim
        );                       
        
        @Query("""
                SELECT COALESCE(SUM(t.valor), 0)
                FROM Transacao t
                WHERE t.usuario = :usuario
                AND t.tipo = 'DESPESA'
                AND (t.despesaFixa IS NULL OR t.despesaFixa = false)
                AND t.dataTransacao BETWEEN :inicio AND :fim
                """)
                BigDecimal somarDespesasVariaveis(
                        Usuario usuario,
                        LocalDate inicio,
                        LocalDate fim
                );

        @Query("""
                SELECT COALESCE(SUM(t.valor),0)
                FROM Transacao t
                WHERE t.usuario = :usuario
                AND t.tipo = 'DESPESA'
                AND t.despesaFixa = true
                AND t.dataTransacao BETWEEN :inicio AND :fim
                """)
                BigDecimal somarDespesasFixas(
                        Usuario usuario,
                        LocalDate inicio,
                        LocalDate fim
                );
}
