package com.sentinelprime.backend.service;

import com.sentinelprime.backend.dto.response.ContaCorrenteResponse;
import com.sentinelprime.backend.model.CartaoCredito;
import com.sentinelprime.backend.model.DespesaCartao;
import com.sentinelprime.backend.model.Transacao;
import com.sentinelprime.backend.model.Usuario;
import com.sentinelprime.backend.model.enums.OrigemContaCorrente;
import com.sentinelprime.backend.model.enums.TipoTransacao;
import com.sentinelprime.backend.repository.CartaoCreditoRepository;
import com.sentinelprime.backend.repository.DespesaCartaoRepository;
import com.sentinelprime.backend.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContaCorrenteService {

private final UsuarioService usuarioService;
private final TransacaoRepository transacaoRepository;
private final CartaoCreditoRepository cartaoRepository;
private final DespesaCartaoRepository despesaCartaoRepository;

@Transactional(readOnly = true)
public ContaCorrenteResponse consultar(
        LocalDate inicio,
        LocalDate fim,
        String busca
) {

    Usuario usuario = usuarioService.getUsuarioAutenticado();

    List<ContaCorrenteResponse.ItemContaCorrente> itens =
            new ArrayList<>();

    // RECEITAS E DESPESAS
    List<Transacao> transacoes =
            transacaoRepository.findContaCorrente(
                    usuario,
                    inicio,
                    fim,
                    busca
            );

    for (Transacao t : transacoes) {

        BigDecimal entrada = BigDecimal.ZERO;
        BigDecimal saida = BigDecimal.ZERO;

        OrigemContaCorrente origem;

        if (t.getTipo() == TipoTransacao.RECEITA) {

            entrada = t.getValor();
            origem = OrigemContaCorrente.RECEITA;

        } else {

            saida = t.getValor();
            origem = OrigemContaCorrente.DESPESA;
        }

        itens.add(
                ContaCorrenteResponse.ItemContaCorrente.builder()
                        .id(t.getId())
                        .data(t.getDataTransacao())
                        .descricao(t.getDescricao())
                        .origem(origem)
                        .entrada(entrada)
                        .saida(saida)
                        .saldo(BigDecimal.ZERO)
                        .build()
        );
    }

    // FATURAS DOS CARTÕES
    List<CartaoCredito> cartoes =
            cartaoRepository.findByUsuario(usuario);

    for (CartaoCredito cartao : cartoes) {

        List<DespesaCartao> despesas =
                despesaCartaoRepository.findByCartaoCredito(cartao);

        Map<String, BigDecimal> faturas =
                new HashMap<>();

        for (DespesaCartao despesa : despesas) {

            faturas.merge(
                    despesa.getCompetencia(),
                    despesa.getValor(),
                    BigDecimal::add
            );
        }

        for (Map.Entry<String, BigDecimal> entry : faturas.entrySet()) {

            String competencia = entry.getKey();

            String[] partes = competencia.split("-");

            int ano = Integer.parseInt(partes[0]);
            int mes = Integer.parseInt(partes[1]);

            LocalDate vencimento =
                    LocalDate.of(
                            ano,
                            mes,
                            cartao.getDiaVencimento()
                    );

            if (vencimento.isBefore(inicio)
                    || vencimento.isAfter(fim)) {
                continue;
            }

            String descricaoFatura =
                "Fatura " + cartao.getNome();

                if (busca != null &&
                        !busca.isBlank() &&
                        !descricaoFatura.toLowerCase().contains(busca.toLowerCase())) {
                        continue;
                }

            itens.add(
                    ContaCorrenteResponse.ItemContaCorrente.builder()
                            .id(null)
                            .data(vencimento)
                            .descricao("Fatura " + cartao.getNome())
                            .origem(OrigemContaCorrente.CARTAO)
                            .entrada(BigDecimal.ZERO)
                            .saida(entry.getValue())
                            .saldo(BigDecimal.ZERO)
                            .build()
            );
        }
    }

    // ORDENA POR DATA
    itens.sort(
            Comparator.comparing(
                    ContaCorrenteResponse.ItemContaCorrente::getData
            )
    );

    // CALCULA SALDO ACUMULADO
    BigDecimal saldo = BigDecimal.ZERO;

    for (ContaCorrenteResponse.ItemContaCorrente item : itens) {

        saldo = saldo
                .add(item.getEntrada())
                .subtract(item.getSaida());

        item.setSaldo(saldo);
    }

    return ContaCorrenteResponse.builder()
            .saldoPeriodo(saldo)
            .transacoes(itens)
            .build();
}

}
