package com.sentinelprime.backend.service;

import com.sentinelprime.backend.dto.request.DespesaFixaRequest;
import com.sentinelprime.backend.dto.response.DespesaFixaResponse;
import com.sentinelprime.backend.exception.BusinessException;
import com.sentinelprime.backend.exception.ResourceNotFoundException;
import com.sentinelprime.backend.model.DespesaFixa;
import com.sentinelprime.backend.model.Usuario;
import com.sentinelprime.backend.model.enums.StatusDespesaFixa;
import com.sentinelprime.backend.repository.DespesaFixaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DespesaFixaService {

    private final DespesaFixaRepository despesaFixaRepository;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public List<DespesaFixaResponse> listar() {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        return despesaFixaRepository.findByUsuarioOrderByDescricaoAsc(usuario)
                .stream().map(DespesaFixaResponse::from).toList();
    }

    @Transactional
    public DespesaFixaResponse criar(DespesaFixaRequest req) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        validar(req);
        DespesaFixa df = DespesaFixa.builder()
                .usuario(usuario)
                .descricao(req.getDescricao())
                .valor(req.getValor())
                .diaVencimento(req.getDiaVencimento())
                .dataInicio(req.getDataInicio())
                .dataFim(req.getIndeterminado() ? null : req.getDataFim())
                .indeterminado(req.getIndeterminado())
                .status(StatusDespesaFixa.ATIVA)
                .build();
        return DespesaFixaResponse.from(despesaFixaRepository.save(df));
    }

    @Transactional
    public DespesaFixaResponse atualizar(Long id, DespesaFixaRequest req) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        DespesaFixa df = buscarDoUsuario(id, usuario);
        validar(req);
        df.setDescricao(req.getDescricao());
        df.setValor(req.getValor());
        df.setDiaVencimento(req.getDiaVencimento());
        df.setDataInicio(req.getDataInicio());
        df.setIndeterminado(req.getIndeterminado());
        df.setDataFim(req.getIndeterminado() ? null : req.getDataFim());
        return DespesaFixaResponse.from(despesaFixaRepository.save(df));
    }

    @Transactional
    public void inativar(Long id) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        DespesaFixa df = buscarDoUsuario(id, usuario);
        df.setStatus(StatusDespesaFixa.INATIVA);
        despesaFixaRepository.save(df);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = usuarioService.getUsuarioAutenticado();
        DespesaFixa df = buscarDoUsuario(id, usuario);
        despesaFixaRepository.delete(df);
    }

    private DespesaFixa buscarDoUsuario(Long id, Usuario usuario) {
        DespesaFixa df = despesaFixaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Despesa fixa não encontrada: " + id));
        if (!df.getUsuario().getId().equals(usuario.getId())) {
            throw new ResourceNotFoundException("Despesa fixa não encontrada: " + id);
        }
        return df;
    }

    private void validar(DespesaFixaRequest req) {
        if (!req.getIndeterminado() && req.getDataFim() == null) {
            throw new BusinessException("Data de fim é obrigatória quando 'indeterminado' é false.");
        }
        if (!req.getIndeterminado() && req.getDataFim() != null
                && req.getDataFim().isBefore(req.getDataInicio())) {
            throw new BusinessException("Data de fim não pode ser anterior à data de início.");
        }
    }
}
