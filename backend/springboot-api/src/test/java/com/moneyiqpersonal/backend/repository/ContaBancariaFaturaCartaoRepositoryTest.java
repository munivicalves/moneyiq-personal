package com.moneyiqpersonal.backend.repository;

import com.moneyiqpersonal.backend.model.*;
import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;
import com.moneyiqpersonal.backend.model.enums.TipoConta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("ContaBancaria & FaturaCartao – persistência JPA")
class ContaBancariaFaturaCartaoRepositoryTest {

    @Autowired UsuarioRepository usuarioRepository;
    @Autowired ContaBancariaRepository contaBancariaRepository;
    @Autowired CartaoCreditoRepository cartaoCreditoRepository;
    @Autowired FaturaCartaoRepository faturaCartaoRepository;
    @Autowired DespesaCartaoRepository despesaCartaoRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = usuarioRepository.save(
                Usuario.builder()
                        .nome("Maria")
                        .email("maria@example.com")
                        .senhaHash("hash")
                        .build());
    }

    @Test
    @DisplayName("Deve persistir ContaBancaria com defaults de @PrePersist")
    void persisteContaBancaria() {
        ContaBancaria conta = contaBancariaRepository.save(
                ContaBancaria.builder()
                        .usuario(usuario)
                        .nome("Conta Principal")
                        .banco("Banco do Brasil")
                        .build());

        assertThat(conta.getId()).isNotNull();
        assertThat(conta.getTipoConta()).isEqualTo(TipoConta.CORRENTE);
        assertThat(conta.getSaldoInicial()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(conta.getAtiva()).isTrue();
        assertThat(conta.getDataCriacao()).isNotNull();

        assertThat(contaBancariaRepository.findByUsuarioAndAtivaTrue(usuario)).hasSize(1);
        assertThat(contaBancariaRepository.findByUsuarioOrderByNomeAsc(usuario)).hasSize(1);
    }

    @Test
    @DisplayName("Deve persistir FaturaCartao e vincular DespesaCartao (1:N)")
    void persisteFaturaComDespesas() {
        CartaoCredito cartao = cartaoCreditoRepository.save(
                CartaoCredito.builder()
                        .usuario(usuario)
                        .nome("Nubank")
                        .diaVencimento(10)
                        .build());

        FaturaCartao fatura = faturaCartaoRepository.save(
                FaturaCartao.builder()
                        .cartaoCredito(cartao)
                        .competencia("2026-06")
                        .valorTotal(new BigDecimal("250.00"))
                        .dataVencimento(LocalDate.of(2026, 6, 10))
                        .build());

        despesaCartaoRepository.save(
                DespesaCartao.builder()
                        .cartaoCredito(cartao)
                        .fatura(fatura)
                        .categoriaDespesa(CategoriaDespesa.ALIMENTACAO)
                        .descricao("Mercado")
                        .valor(new BigDecimal("250.00"))
                        .competencia("2026-06")
                        .dataCompra(LocalDate.of(2026, 6, 1))
                        .build());

        assertThat(fatura.getId()).isNotNull();
        assertThat(fatura.getPaga()).isFalse();
        assertThat(fatura.getDataCriacao()).isNotNull();

        assertThat(faturaCartaoRepository.findByCartaoCreditoAndCompetencia(cartao, "2026-06"))
                .isPresent();
        assertThat(faturaCartaoRepository.findByCartaoCreditoAndPagaFalse(cartao)).hasSize(1);

        DespesaCartao persistida = despesaCartaoRepository.findByCartaoCredito(cartao).get(0);
        assertThat(persistida.getFatura()).isNotNull();
        assertThat(persistida.getFatura().getId()).isEqualTo(fatura.getId());
    }
}
