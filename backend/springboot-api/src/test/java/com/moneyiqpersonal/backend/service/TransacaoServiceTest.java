package com.moneyiqpersonal.backend.service;

import com.moneyiqpersonal.backend.dto.request.TransacaoRequest;
import com.moneyiqpersonal.backend.dto.response.TransacaoResponse;
import com.moneyiqpersonal.backend.exception.BusinessException;
import com.moneyiqpersonal.backend.model.Transacao;
import com.moneyiqpersonal.backend.model.Usuario;
import com.moneyiqpersonal.backend.model.enums.CategoriaDespesa;
import com.moneyiqpersonal.backend.model.enums.TipoReceita;
import com.moneyiqpersonal.backend.model.enums.TipoTransacao;
import com.moneyiqpersonal.backend.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransacaoService – TDD")
class TransacaoServiceTest {

    @Mock TransacaoRepository transacaoRepository;
    @Mock UsuarioService usuarioService;

    @InjectMocks TransacaoService transacaoService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id(1L).email("joao@example.com").nome("João").build();
        when(usuarioService.getUsuarioAutenticado()).thenReturn(usuario);
    }

    @Test
    @DisplayName("Deve criar receita com tipo SALARIO")
    void criar_receita_success() {
        TransacaoRequest req = new TransacaoRequest();
        req.setTipo(TipoTransacao.RECEITA);
        req.setTipoReceita(TipoReceita.SALARIO);
        req.setDescricao("Salário Março");
        req.setValor(new BigDecimal("3500.00"));
        req.setDataTransacao(LocalDate.of(2026, 3, 5));

        Transacao saved = Transacao.builder()
                .id(1L).usuario(usuario).tipo(TipoTransacao.RECEITA)
                .tipoReceita(TipoReceita.SALARIO).descricao("Salário Março")
                .valor(new BigDecimal("3500.00")).dataTransacao(LocalDate.of(2026, 3, 5)).build();

        when(transacaoRepository.save(any())).thenReturn(saved);

        TransacaoResponse response = transacaoService.criar(req);

        assertThat(response.getTipo()).isEqualTo(TipoTransacao.RECEITA);
        assertThat(response.getTipoReceita()).isEqualTo(TipoReceita.SALARIO);
        assertThat(response.getValor()).isEqualByComparingTo("3500.00");
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando RECEITA sem tipoReceita")
    void criar_receita_semTipoReceita() {
        TransacaoRequest req = new TransacaoRequest();
        req.setTipo(TipoTransacao.RECEITA);
        req.setDescricao("Salário");
        req.setValor(new BigDecimal("3500.00"));
        req.setDataTransacao(LocalDate.now());
        // tipoReceita não informado

        assertThatThrownBy(() -> transacaoService.criar(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Tipo de receita é obrigatório");
    }

    @Test
    @DisplayName("Deve criar despesa com sucesso")
    void criar_despesa_success() {
        TransacaoRequest req = new TransacaoRequest();
        req.setTipo(TipoTransacao.DESPESA);
        req.setCategoriaDespesa(CategoriaDespesa.ALIMENTACAO);
        req.setDescricao("Supermercado");
        req.setValor(new BigDecimal("320.00"));
        req.setDataTransacao(LocalDate.of(2026, 3, 9));

        Transacao saved = Transacao.builder()
                .id(2L).usuario(usuario).tipo(TipoTransacao.DESPESA)
                .categoriaDespesa(CategoriaDespesa.ALIMENTACAO)
                .descricao("Supermercado").valor(new BigDecimal("320.00"))
                .dataTransacao(LocalDate.of(2026, 3, 9)).build();

        when(transacaoRepository.save(any())).thenReturn(saved);

        TransacaoResponse response = transacaoService.criar(req);

        assertThat(response.getTipo()).isEqualTo(TipoTransacao.DESPESA);
        assertThat(response.getValor()).isEqualByComparingTo("320.00");
    }
}
