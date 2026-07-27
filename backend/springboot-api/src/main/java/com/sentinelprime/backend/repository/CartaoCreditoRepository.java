package com.sentinelprime.backend.repository;

import com.sentinelprime.backend.model.CartaoCredito;
import com.sentinelprime.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
    List<CartaoCredito> findByUsuarioOrderByNomeAsc(Usuario usuario);
    List<CartaoCredito> findByUsuario(Usuario usuario);
    Optional<CartaoCredito> findByIdAndUsuario(Long id, Usuario usuario);
}
