package com.sentinelprime.backend.repository;

import com.sentinelprime.backend.model.DespesaFixa;
import com.sentinelprime.backend.model.Usuario;
import com.sentinelprime.backend.model.enums.StatusDespesaFixa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DespesaFixaRepository extends JpaRepository<DespesaFixa, Long> {

    List<DespesaFixa> findByUsuarioOrderByDescricaoAsc(Usuario usuario);

    @Query("""
            SELECT COALESCE(SUM(df.valor), 0) FROM DespesaFixa df
            WHERE df.usuario = :usuario
              AND df.status = 'ATIVA'
              AND df.dataInicio <= :fim
              AND (df.indeterminado = true OR df.dataFim >= :inicio)
            """)
    BigDecimal sumFixasAtivas(@Param("usuario") Usuario usuario,
                              @Param("inicio") LocalDate inicio,
                              @Param("fim") LocalDate fim);

    @Query("""
            SELECT df FROM DespesaFixa df
            WHERE df.usuario = :usuario
              AND df.status = 'ATIVA'
              AND df.dataInicio <= :fim
              AND (df.indeterminado = true OR df.dataFim >= :inicio)
            """)
    List<DespesaFixa> findAtivasNoPeriodo(@Param("usuario") Usuario usuario,
                                          @Param("inicio") LocalDate inicio,
                                          @Param("fim") LocalDate fim);
}
