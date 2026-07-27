package com.moneyiqpersonal.backend.service;

import com.moneyiqpersonal.backend.dto.response.CategoriaGastoResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardDataScienceResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardHistoricoResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardResponse.FixasVariaveisItem;
import com.moneyiqpersonal.backend.dto.response.DashboardResponse.MesChartItem;
import com.moneyiqpersonal.backend.dto.response.ReceitaResumoResponse;
import com.moneyiqpersonal.backend.dto.response.TransacaoResponse;
import com.moneyiqpersonal.backend.model.Usuario;
import com.moneyiqpersonal.backend.model.enums.TipoReceita;
import com.moneyiqpersonal.backend.model.enums.TipoTransacao;
import com.moneyiqpersonal.backend.repository.DespesaFixaRepository;
import com.moneyiqpersonal.backend.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransacaoRepository transacaoRepository;
    private final DespesaFixaRepository despesaFixaRepository;
    private final UsuarioService usuarioService;

    /** competencia ex: "2026-03" */
    @Transactional(readOnly = true)
        public DashboardResponse getDashboard(String competencia) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        YearMonth ym = YearMonth.parse(competencia);

        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();

        BigDecimal receitas =
                sum(usuario, TipoTransacao.RECEITA, inicio, fim);

        BigDecimal despesasVar =
                transacaoRepository.somarDespesasVariaveis(
                        usuario,
                        inicio,
                        fim
                );

        BigDecimal fixas =
                transacaoRepository.somarDespesasFixas(
                        usuario,
                        inicio,
                        fim
                );

        BigDecimal totalDespesas =
                despesasVar.add(fixas);

        BigDecimal saldo =
                receitas.subtract(totalDespesas);

        List<MesChartItem> chart = new ArrayList<>();

        for (int i = 2; i >= 0; i--) {

                YearMonth m = ym.minusMonths(i);

                LocalDate s = m.atDay(1);
                LocalDate e = m.atEndOfMonth();

                BigDecimal receitasMes =
                        sum(usuario, TipoTransacao.RECEITA, s, e);

                BigDecimal despesasMes =
                        transacaoRepository.somarDespesasVariaveis(
                                usuario,
                                s,
                                e
                        );

                BigDecimal fixasMes =
                        transacaoRepository.somarDespesasFixas(
                                usuario,
                                s,
                                e
                        );

                chart.add(
                        MesChartItem.builder()
                                .mes(m.getMonth().name().substring(0, 3))
                                .receitas(receitasMes)
                                .despesas(despesasMes.add(fixasMes))
                                .build()
                );
        }

        System.out.println("FIXAS = " + fixas);
        System.out.println("VARIAVEIS = " + despesasVar);


        return DashboardResponse.builder()
                .competencia(competencia)
                .totalReceitas(receitas)
                .totalDespesas(totalDespesas)
                .totalFixas(fixas)
                .saldo(saldo)
                .chartData(chart)
                .fixasVsVariaveis(
                        FixasVariaveisItem.builder()
                                .fixas(fixas)
                                .variaveis(despesasVar)
                                .build()
                )
                .build();
        }

    @Transactional(readOnly = true)
    public ReceitaResumoResponse getResumoReceitas(String competencia) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        YearMonth ym = YearMonth.parse(competencia);
        LocalDate inicio = ym.atDay(1), fim = ym.atEndOfMonth();

        var transacoes = transacaoRepository
                .findByUsuarioAndTipoAndDataTransacaoBetweenOrderByDataTransacaoDesc(
                        usuario, TipoTransacao.RECEITA, inicio, fim);

        BigDecimal salario = transacoes.stream()
                .filter(t -> TipoReceita.SALARIO.equals(t.getTipoReceita()))
                .map(t -> t.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal extra = transacoes.stream()
                .filter(t -> TipoReceita.EXTRA.equals(t.getTipoReceita()))
                .map(t -> t.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal total = salario.add(extra);

        return ReceitaResumoResponse.builder()
                .competencia(competencia)
                .totalReceitas(total)
                .totalSalario(salario)
                .totalExtra(extra)
                .transacoes(transacoes.stream().map(TransacaoResponse::from).toList())
                .build();
    }


    private BigDecimal sum(Usuario u, TipoTransacao tipo, LocalDate inicio, LocalDate fim) {
        BigDecimal v = transacaoRepository.sumValorByUsuarioAndTipoAndPeriodo(u, tipo, inicio, fim);
        return v == null ? BigDecimal.ZERO : v;
    }

        public DashboardDataScienceResponse exportarDados(
                String competencia
        ) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        YearMonth ym = YearMonth.parse(competencia);

        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();

        BigDecimal receitas =
                sum(usuario, TipoTransacao.RECEITA, inicio, fim);

        BigDecimal despesas =
                sum(usuario, TipoTransacao.DESPESA, inicio, fim);

        BigDecimal fixas =
                despesaFixaRepository.sumFixasAtivas(
                        usuario,
                        inicio,
                        fim
                );

        BigDecimal saldo =
                receitas.subtract(despesas.add(fixas));

        return DashboardDataScienceResponse.builder()
                .competencia(competencia)
                .receitas(receitas)
                .despesas(despesas)
                .saldo(saldo)
                .fixas(fixas)
                .variaveis(despesas)
                .build();
        }

        public List<DashboardHistoricoResponse> exportarHistorico()     {
            Usuario usuario = usuarioService.getUsuarioAutenticado();

            List<DashboardHistoricoResponse> historico = new ArrayList<>();

            // Gerar histórico dos últimos 12 meses
            YearMonth mesAtual = YearMonth.now();
            for (int i = 11; i >= 0; i--) {
                YearMonth m = mesAtual.minusMonths(i);
                LocalDate inicio = m.atDay(1);
                LocalDate fim = m.atEndOfMonth();

                BigDecimal receitas =
                        sum(usuario, TipoTransacao.RECEITA, inicio, fim);

                BigDecimal despesas =
                        sum(usuario, TipoTransacao.DESPESA, inicio, fim);

                BigDecimal fixas =
                        despesaFixaRepository.sumFixasAtivas(
                                usuario,
                                inicio,
                                fim
                        );

                BigDecimal saldo =
                        receitas.subtract(despesas.add(fixas));

                historico.add(DashboardHistoricoResponse.builder()
                        .competencia(m.toString())
                        .receitas(receitas)
                        .despesas(despesas)
                        .saldo(saldo)
                        .build());
            }

            return historico;
        }

        public List<CategoriaGastoResponse> getGastosPorCategoria(
        String competencia
        ) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        YearMonth ym = YearMonth.parse(competencia);

        LocalDate inicio = ym.atDay(1);
        LocalDate fim = ym.atEndOfMonth();

        return transacaoRepository
                .buscarDespesasAgrupadasPorCategoria(
                        usuario,
                        inicio,
                        fim
                );
        }

}
