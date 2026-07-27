package com.sentinelprime.backend.service;

import com.sentinelprime.backend.dto.request.CategoriaRequest;
import com.sentinelprime.backend.dto.response.CategoriaResponse;
import com.sentinelprime.backend.exception.BusinessException;
import com.sentinelprime.backend.exception.ResourceNotFoundException;
import com.sentinelprime.backend.model.Categoria;
import com.sentinelprime.backend.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAllByOrderByNomeAsc()
                .stream().map(CategoriaResponse::from).toList();
    }

    @Transactional
    public CategoriaResponse criar(CategoriaRequest req) {
        if (categoriaRepository.existsByNomeIgnoreCase(req.getNome())) {
            throw new BusinessException("Categoria já existe: " + req.getNome());
        }
        Categoria c = Categoria.builder()
                .nome(req.getNome())
                .icone(req.getIcone())
                .cor(req.getCor())
                .build();
        return CategoriaResponse.from(categoriaRepository.save(c));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest req) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
        c.setNome(req.getNome());
        c.setIcone(req.getIcone());
        c.setCor(req.getCor());
        return CategoriaResponse.from(categoriaRepository.save(c));
    }

    @Transactional
    public void deletar(Long id) {
        Categoria c = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada: " + id));
        categoriaRepository.delete(c);
    }
}
