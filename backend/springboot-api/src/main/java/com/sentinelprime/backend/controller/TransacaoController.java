package com.sentinelprime.backend.controller;

import com.sentinelprime.backend.dto.request.TransacaoRequest;
import com.sentinelprime.backend.dto.response.TransacaoResponse;
import com.sentinelprime.backend.model.enums.TipoTransacao;
import com.sentinelprime.backend.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    /** GET /api/transacoes?tipo=RECEITA&inicio=2026-01-01&fim=2026-01-31 */
    @GetMapping
    public ResponseEntity<List<TransacaoResponse>> listar(
            @RequestParam TipoTransacao tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(transacaoService.listarPorTipoEPeriodo(tipo, inicio, fim));
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(@Valid @RequestBody TransacaoRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transacaoService.criar(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponse> atualizar(@PathVariable Long id,
                                                        @Valid @RequestBody TransacaoRequest req) {
        return ResponseEntity.ok(transacaoService.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
