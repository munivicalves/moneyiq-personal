package com.moneyiqpersonal.backend.service;

import com.moneyiqpersonal.backend.dto.request.TransacaoRequest;
import com.moneyiqpersonal.backend.dto.response.TransacaoResponse;
import com.moneyiqpersonal.backend.exception.BusinessException;
import com.moneyiqpersonal.backend.exception.ResourceNotFoundException;
import com.moneyiqpersonal.backend.model.Transacao;
import com.moneyiqpersonal.backend.model.Usuario;
import com.moneyiqpersonal.backend.model.enums.TipoTransacao;
import com.moneyiqpersonal.backend.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final UsuarioService usuarioService;

    @Transactional
    public TransacaoResponse criar(TransacaoRequest req) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        validar(req);

        // DESPESA FIXA
        if (
                TipoTransacao.DESPESA.equals(req.getTipo()) &&
                Boolean.TRUE.equals(req.getDespesaFixa()) &&
                req.getDataFimRecorrencia() != null
        ) {

            String idRecorrencia = UUID.randomUUID().toString();

            LocalDate dataAtual = req.getDataTransacao();

            Transacao ultimaTransacao = null;

            while (!dataAtual.isAfter(req.getDataFimRecorrencia())) {

                Transacao t = Transacao.builder()
                        .usuario(usuario)
                        .tipo(req.getTipo())
                        .tipoReceita(req.getTipoReceita())
                        .categoriaDespesa(req.getCategoriaDespesa())
                        .despesaFixa(true)
                        .dataFimRecorrencia(req.getDataFimRecorrencia())
                        .idRecorrencia(idRecorrencia)
                        .descricao(req.getDescricao())
                        .valor(req.getValor())
                        .dataTransacao(dataAtual)
                        .build();

                ultimaTransacao = transacaoRepository.save(t);

                dataAtual = dataAtual.plusMonths(1);
            }

            return TransacaoResponse.from(ultimaTransacao);
        }

        // TRANSAÇÃO NORMAL
        Transacao t = Transacao.builder()
                .usuario(usuario)
                .tipo(req.getTipo())
                .tipoReceita(req.getTipoReceita())
                .categoriaDespesa(req.getCategoriaDespesa())
                .despesaFixa(req.getDespesaFixa())
                .dataFimRecorrencia(req.getDataFimRecorrencia())
                .idRecorrencia(null)
                .descricao(req.getDescricao())
                .valor(req.getValor())
                .dataTransacao(req.getDataTransacao())
                .build();

        return TransacaoResponse.from(transacaoRepository.save(t));
    }

    @Transactional
    public TransacaoResponse atualizar(Long id, TransacaoRequest req) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        Transacao t = buscarDoUsuario(id, usuario);

        validar(req);

        t.setTipo(req.getTipo());
        t.setTipoReceita(req.getTipoReceita());
        t.setCategoriaDespesa(req.getCategoriaDespesa());
        t.setDespesaFixa(req.getDespesaFixa());
        t.setDataFimRecorrencia(req.getDataFimRecorrencia());
        t.setDescricao(req.getDescricao());
        t.setValor(req.getValor());
        t.setDataTransacao(req.getDataTransacao());

        return TransacaoResponse.from(transacaoRepository.save(t));
    }

    @Transactional
    public void deletar(Long id) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        Transacao t = buscarDoUsuario(id, usuario);

        transacaoRepository.delete(t);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponse> listarPorTipoEPeriodo(
            TipoTransacao tipo,
            LocalDate inicio,
            LocalDate fim
    ) {

        Usuario usuario = usuarioService.getUsuarioAutenticado();

        return transacaoRepository
                .findByUsuarioAndTipoAndDataTransacaoBetweenOrderByDataTransacaoDesc(
                        usuario,
                        tipo,
                        inicio,
                        fim
                )
                .stream()
                .map(TransacaoResponse::from)
                .toList();
    }

    private Transacao buscarDoUsuario(Long id, Usuario usuario) {

        Transacao t = transacaoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Transação não encontrada: " + id
                        )
                );

        if (!t.getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException(
                    "Transação não encontrada: " + id
            );
        }

        return t;
    }

    private void validar(TransacaoRequest req) {

        if (
                TipoTransacao.RECEITA.equals(req.getTipo()) &&
                req.getTipoReceita() == null
        ) {
            throw new BusinessException(
                    "Tipo de receita é obrigatório para transações de RECEITA."
            );
        }

        if (
                TipoTransacao.DESPESA.equals(req.getTipo()) &&
                req.getCategoriaDespesa() == null
        ) {
            throw new BusinessException(
                    "Categoria é obrigatória para transações de DESPESA."
            );
        }
    }
}
