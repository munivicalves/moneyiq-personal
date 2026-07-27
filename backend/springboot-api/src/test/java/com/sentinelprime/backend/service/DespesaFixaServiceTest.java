package com.sentinelprime.backend.service;

import com.sentinelprime.backend.dto.request.DespesaFixaRequest;
import com.sentinelprime.backend.dto.response.DespesaFixaResponse;
import com.sentinelprime.backend.exception.BusinessException;
import com.sentinelprime.backend.model.DespesaFixa;
import com.sentinelprime.backend.model.Usuario;
import com.sentinelprime.backend.model.enums.StatusDespesaFixa;
import com.sentinelprime.backend.repository.DespesaFixaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DespesaFixaService – TDD")
class DespesaFixaServiceTest {

    @Mock DespesaFixaRepository despesaFixaRepository;
    @Mock UsuarioService usuarioService;

    @InjectMocks DespesaFixaService despesaFixaService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = Usuario.builder().id(1L).email("joao@example.com").nome("João").build();
        when(usuarioService.getUsuarioAutenticado()).thenReturn(usuario);
    }

    @Test
    @DisplayName("Deve criar despesa fixa indeterminada com sucesso")
    void criar_indeterminado_success() {
        DespesaFixaRequest req = new DespesaFixaRequest();
        req.setDescricao("Aluguel");
        req.setValor(new BigDecimal("900.00"));
        req.setDiaVencimento(5);
        req.setDataInicio(LocalDate.of(2026, 1, 1));
        req.setIndeterminado(true);

        DespesaFixa saved = DespesaFixa.builder()
                .id(1L).usuario(usuario).descricao("Aluguel")
                .valor(new BigDecimal("900.00")).diaVencimento(5)
                .dataInicio(LocalDate.of(2026, 1, 1))
                .indeterminado(true).status(StatusDespesaFixa.ATIVA).build();

        when(despesaFixaRepository.save(any())).thenReturn(saved);

        DespesaFixaResponse response = despesaFixaService.criar(req);

        assertThat(response.getDescricao()).isEqualTo("Aluguel");
        assertThat(response.getIndeterminado()).isTrue();
        assertThat(response.getStatus()).isEqualTo(StatusDespesaFixa.ATIVA);
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando dataFim ausente e não indeterminado")
    void criar_semDataFim_naoIndeterminado() {
        DespesaFixaRequest req = new DespesaFixaRequest();
        req.setDescricao("Academia");
        req.setValor(new BigDecimal("120.00"));
        req.setDiaVencimento(10);
        req.setDataInicio(LocalDate.of(2026, 1, 1));
        req.setIndeterminado(false);
        // dataFim não informado

        assertThatThrownBy(() -> despesaFixaService.criar(req))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Data de fim é obrigatória");
    }

    @Test
    @DisplayName("Deve listar despesas fixas do usuário")
    void listar_success() {
        when(despesaFixaRepository.findByUsuarioOrderByDescricaoAsc(usuario))
                .thenReturn(List.of(
                        DespesaFixa.builder().id(1L).usuario(usuario).descricao("Aluguel")
                                .valor(new BigDecimal("900")).diaVencimento(5)
                                .dataInicio(LocalDate.now()).indeterminado(true)
                                .status(StatusDespesaFixa.ATIVA).build()
                ));

        List<DespesaFixaResponse> resultado = despesaFixaService.listar();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getDescricao()).isEqualTo("Aluguel");
    }
}
