package com.moneyiqpersonal.backend.repository;

import com.moneyiqpersonal.backend.model.CartaoCredito;
import com.moneyiqpersonal.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartaoCreditoRepository extends JpaRepository<CartaoCredito, Long> {
    List<CartaoCredito> findByUsuarioOrderByNomeAsc(Usuario usuario);
    List<CartaoCredito> findByUsuario(Usuario usuario);
    Optional<CartaoCredito> findByIdAndUsuario(Long id, Usuario usuario);
}
