    package com.moneyiqpersonal.backend.controller;

    import com.moneyiqpersonal.backend.dto.request.CartaoCreditoRequest;
    import com.moneyiqpersonal.backend.dto.request.DespesaCartaoRequest;
    import com.moneyiqpersonal.backend.dto.response.CartaoCreditoResponse;
    import com.moneyiqpersonal.backend.dto.response.DespesaCartaoResponse;
    import com.moneyiqpersonal.backend.dto.response.FaturaCartaoResponse;
    import com.moneyiqpersonal.backend.service.CartaoCreditoService;
    import jakarta.validation.Valid;
    import lombok.RequiredArgsConstructor;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/cartoes")
    @RequiredArgsConstructor
    public class    CartaoCreditoController {

        private final CartaoCreditoService cartaoCreditoService;

        @GetMapping
        public ResponseEntity<List<CartaoCreditoResponse>> listarCartoes() {
            return ResponseEntity.ok(cartaoCreditoService.listarCartoes());
        }

        @PostMapping
        public ResponseEntity<CartaoCreditoResponse> criarCartao(@Valid @RequestBody CartaoCreditoRequest req) {
            return ResponseEntity.status(HttpStatus.CREATED).body(cartaoCreditoService.criarCartao(req));
        }

        @PutMapping("/{id}")
        public ResponseEntity<CartaoCreditoResponse> atualizarCartao(@PathVariable Long id,
                                                                    @Valid @RequestBody CartaoCreditoRequest req) {
            return ResponseEntity.ok(cartaoCreditoService.atualizarCartao(id, req));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletarCartao(@PathVariable Long id) {

            cartaoCreditoService.deletarCartao(id);

            return ResponseEntity.noContent().build();
        }

        /** GET /api/cartoes/{id}/fatura?competencia=2026-03 */
        @GetMapping("/{id}/fatura")
        public ResponseEntity<FaturaCartaoResponse> getFatura(@PathVariable Long id,
                                                            @RequestParam String competencia) {
            return ResponseEntity.ok(cartaoCreditoService.getFatura(id, competencia));
        }

        @PostMapping("/{cartaoId}/despesas")
        public ResponseEntity<DespesaCartaoResponse> adicionarDespesa(@PathVariable Long cartaoId,
                                                                        @Valid @RequestBody DespesaCartaoRequest req) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cartaoCreditoService.adicionarDespesa(cartaoId, req));
        }

        @DeleteMapping("/{cartaoId}/despesas/{despesaId}")
        public ResponseEntity<Void> deletarDespesa(@PathVariable Long cartaoId,
                                                    @PathVariable Long despesaId) {
            cartaoCreditoService.deletarDespesa(cartaoId, despesaId);
            return ResponseEntity.noContent().build();
        }
    }
