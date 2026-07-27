package com.sentinelprime.backend.controller;

import com.sentinelprime.backend.dto.request.DespesaFixaRequest;
import com.sentinelprime.backend.dto.response.DespesaFixaResponse;
import com.sentinelprime.backend.service.DespesaFixaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas-fixas")
@RequiredArgsConstructor
public class DespesaFixaController {

    private final DespesaFixaService despesaFixaService;

    @GetMapping
    public ResponseEntity<List<DespesaFixaResponse>> listar() {
        return ResponseEntity.ok(despesaFixaService.listar());
    }

    @PostMapping
    public ResponseEntity<DespesaFixaResponse> criar(@Valid @RequestBody DespesaFixaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaFixaService.criar(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DespesaFixaResponse> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody DespesaFixaRequest req) {
        return ResponseEntity.ok(despesaFixaService.atualizar(id, req));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        despesaFixaService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        despesaFixaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
