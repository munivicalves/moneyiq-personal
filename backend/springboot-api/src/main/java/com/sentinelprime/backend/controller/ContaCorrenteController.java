package com.sentinelprime.backend.controller;

import com.sentinelprime.backend.dto.response.ContaCorrenteResponse;
import com.sentinelprime.backend.service.ContaCorrenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/conta-corrente")
@RequiredArgsConstructor
public class ContaCorrenteController {

    private final ContaCorrenteService contaCorrenteService;

    @GetMapping
    public ResponseEntity<ContaCorrenteResponse> consultar(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fim,
            @RequestParam(required = false) String busca
    ) {

        return ResponseEntity.ok(
                contaCorrenteService.consultar(
                        inicio,
                        fim,
                        busca
                )
        );
    }
}