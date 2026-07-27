package com.moneyiqpersonal.backend.controller;

import com.moneyiqpersonal.backend.dto.response.CategoriaGastoResponse;
import com.moneyiqpersonal.backend.dto.response.ContaCorrenteResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardDataScienceResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardHistoricoResponse;
import com.moneyiqpersonal.backend.dto.response.DashboardResponse;
import com.moneyiqpersonal.backend.dto.response.ReceitaResumoResponse;
import com.moneyiqpersonal.backend.service.ContaCorrenteService;
import com.moneyiqpersonal.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final ContaCorrenteService contaCorrenteService;

    /** GET /api/dashboard?competencia=2026-03 */
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            @RequestParam(defaultValue = "#{T(java.time.YearMonth).now().toString()}") String competencia) {
        return ResponseEntity.ok(dashboardService.getDashboard(competencia));
    }

    /** GET /api/dashboard/receitas?competencia=2026-03 */
    @GetMapping("/receitas")
    public ResponseEntity<ReceitaResumoResponse> getReceitas(
            @RequestParam(defaultValue = "#{T(java.time.YearMonth).now().toString()}") String competencia) {
        return ResponseEntity.ok(dashboardService.getResumoReceitas(competencia));
    }

    /**
     * GET /api/dashboard/conta-corrente
     *  ?inicio=2026-01-01&fim=2026-03-31&descricao=salario
     */
    @GetMapping("/conta-corrente")
    public ResponseEntity<ContaCorrenteResponse> getContaCorrente(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            @RequestParam(required = false) String descricao) {

        return ResponseEntity.ok(
                contaCorrenteService.consultar(
                        inicio,
                        fim,
                        descricao
                )
        );
    }

    @GetMapping("/dados")
    public ResponseEntity<DashboardDataScienceResponse> exportarDados(
            @RequestParam String competencia
    ) {
        return ResponseEntity.ok(
                dashboardService.exportarDados(competencia)
        );
    }

    @GetMapping("/dados-historico")
    public ResponseEntity<List<DashboardHistoricoResponse>> exportarHistorico() {

    return ResponseEntity.ok(
            dashboardService.exportarHistorico()
    );
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaGastoResponse>> categorias(
            @RequestParam String competencia
    ) {

        return ResponseEntity.ok(
                dashboardService.getGastosPorCategoria(competencia)
        );
    }

    
}
