package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.MovimentacaoDTO;
import com.diego.estoquefarma.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaMovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacoesRepository;


    @Transactional(readOnly = true)
    public Page<MovimentacaoDTO> listarTodas(Pageable pageable) {
        return movimentacoesRepository.findAll(pageable).map(MovimentacaoDTO::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoDTO> listarPorLote(Long loteId) {
        return movimentacoesRepository.findByLoteIdOrderByDataHoraMovimentacaoDesc(loteId)
                .stream()
                .map(MovimentacaoDTO::fromEntity)
                .collect(Collectors.toList());
    }
}