package com.moneyiqpersonal.backend.service;

import com.moneyiqpersonal.backend.dto.request.CartaoCreditoRequest;
import com.moneyiqpersonal.backend.dto.request.DespesaCartaoRequest;
import com.moneyiqpersonal.backend.dto.response.CartaoCreditoResponse;
import com.moneyiqpersonal.backend.dto.response.DespesaCartaoResponse;
import com.moneyiqpersonal.backend.dto.response.FaturaCartaoResponse;
import com.moneyiqpersonal.backend.exception.ResourceNotFoundException;
import com.moneyiqpersonal.backend.model.CartaoCredito;
import com.moneyiqpersonal.backend.model.DespesaCartao;
import com.moneyiqpersonal.backend.model.Usuario;
import com.moneyiqpersonal.backend.repository.CartaoCreditoRepository;
import com.moneyiqpersonal.backend.repository.DespesaCartaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartaoCreditoService {

    private final CartaoCreditoRepository cartaoRepository;
    private final DespesaCartaoRepository despesaCartaoRepository;
    private final UsuarioService usuarioService;

    // ─── CARTÕES ──────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CartaoCreditoResponse> listarCartoes() {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        return cartaoRepository.findByUsuarioOrderByNomeAsc(usuario)
                .stream().map(CartaoCreditoResponse::from).toList();
    }

    @Transactional
    public CartaoCreditoResponse criarCartao(CartaoCreditoRequest req) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        CartaoCredito cartao = CartaoCredito.builder()
                .usuario(usuario)
                .nome(req.getNome())
                .bandeira(req.getBandeira())
                .diaVencimento(req.getDiaVencimento())
                .build();
        return CartaoCreditoResponse.from(cartaoRepository.save(cartao));
    }

    @Transactional
    public CartaoCreditoResponse atualizarCartao(Long id, CartaoCreditoRequest req) {
        CartaoCredito cartao = buscarCartaoDoUsuario(id);
        cartao.setNome(req.getNome());
        cartao.setBandeira(req.getBandeira());
        cartao.setDiaVencimento(req.getDiaVencimento());
        return CartaoCreditoResponse.from(cartaoRepository.save(cartao));
    }

    @Transactional
    public void deletarCartao(Long id) {

        CartaoCredito cartao = buscarCartaoDoUsuario(id);

        boolean possuiDespesas =
                despesaCartaoRepository.existsByCartaoCreditoId(id);

        if (possuiDespesas) {
            throw new IllegalStateException(
                    "Não é possível excluir o cartão pois existem despesas vinculadas."
            );
        }

        cartaoRepository.delete(cartao);
    }

    // ─── FATURA / DESPESAS ────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public FaturaCartaoResponse getFatura(Long cartaoId, String competencia) {
        CartaoCredito cartao = buscarCartaoDoUsuario(cartaoId);
        List<DespesaCartao> despesas =
                despesaCartaoRepository.findByCartaoCreditoAndCompetenciaOrderByDescricaoAsc(cartao, competencia);
        var total = despesaCartaoRepository.sumFaturaPorCartaoECompetencia(cartao, competencia);
        return FaturaCartaoResponse.builder()
                .cartaoId(cartao.getId())
                .cartaoNome(cartao.getNome())
                .competencia(competencia)
                .totalFatura(total)
                .despesas(despesas.stream().map(DespesaCartaoResponse::from).toList())
                .build();
    }

    @Transactional
public DespesaCartaoResponse adicionarDespesa(
        Long cartaoId,
        DespesaCartaoRequest req
) {

    CartaoCredito cartao = buscarCartaoDoUsuario(cartaoId);



    DespesaCartao ultimaParcela = null;

    int totalParcelas = req.getTotalParcelas();
    long totalCentavos = req.getValor()
            .movePointRight(2)
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact();
    long centavosBase = totalCentavos / totalParcelas;
    long centavosRestantes = totalCentavos % totalParcelas;

    String[] partes = req.getCompetencia().split("-");

    int ano = Integer.parseInt(partes[0]);
    int mes = Integer.parseInt(partes[1]);

    for (int parcela = 1; parcela <= totalParcelas; parcela++) {

        String competenciaParcela =
                String.format("%04d-%02d", ano, mes);
        long centavosParcela = centavosBase + (parcela <= centavosRestantes ? 1 : 0);
        BigDecimal valorParcela = BigDecimal.valueOf(centavosParcela, 2);

        DespesaCartao despesa = DespesaCartao.builder()
        .cartaoCredito(cartao)
        .categoriaDespesa(req.getCategoriaDespesa())
        .descricao(req.getDescricao())
        .valor(valorParcela)
        .parcelaAtual(parcela)
        .totalParcelas(totalParcelas)
        .competencia(competenciaParcela)
        .dataCompra(req.getDataCompra())
        .build();

        ultimaParcela = despesaCartaoRepository.save(despesa);

        mes++;

        if (mes > 12) {
            mes = 1;
            ano++;
        }
    }

    return DespesaCartaoResponse.from(ultimaParcela);
}

    @Transactional
    public void deletarDespesa(Long cartaoId, Long despesaId) {
        CartaoCredito cartao = buscarCartaoDoUsuario(cartaoId);
        DespesaCartao despesa = despesaCartaoRepository.findByIdAndCartaoCredito(despesaId, cartao)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa não encontrada: " + despesaId));
        despesaCartaoRepository.delete(despesa);
    }

    private CartaoCredito buscarCartaoDoUsuario(Long id) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        return cartaoRepository.findByIdAndUsuario(id, usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Cartão não encontrado: " + id));
    }
}
