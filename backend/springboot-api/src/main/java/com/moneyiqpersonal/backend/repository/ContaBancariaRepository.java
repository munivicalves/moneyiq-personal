package com.moneyiqpersonal.backend.repository;

import com.moneyiqpersonal.backend.model.ContaBancaria;
import com.moneyiqpersonal.backend.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long> {
    List<ContaBancaria> findByUsuarioOrderByNomeAsc(Usuario usuario);
    List<ContaBancaria> findByUsuarioAndAtivaTrue(Usuario usuario);
    Optional<ContaBancaria> findByIdAndUsuario(Long id, Usuario usuario);
}
