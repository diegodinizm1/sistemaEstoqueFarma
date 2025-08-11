package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.MovimentacaoDTO;
import com.diego.estoquefarma.repository.MovimentacaoRepository;
import com.diego.estoquefarma.service.ConsultaMovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    private ConsultaMovimentacaoService consultaMovimentacaoService;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;


    @GetMapping("/all")
    public ResponseEntity<List<MovimentacaoDTO>> listarTodasSemPaginacao() {
        List<MovimentacaoDTO> movimentacoes = movimentacaoRepository.findAll().stream()
                .map(MovimentacaoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(movimentacoes);
    }
    @GetMapping
    public ResponseEntity<Page<MovimentacaoDTO>> listarTodas(Pageable pageable) {
        Page<MovimentacaoDTO> movimentacoes = consultaMovimentacaoService.listarTodas(pageable);
        return ResponseEntity.ok(movimentacoes);
    }


    @GetMapping("/lote/{loteId}")
    public ResponseEntity<List<MovimentacaoDTO>> listarPorLote(@PathVariable Long loteId) {
        List<MovimentacaoDTO> movimentacoes = consultaMovimentacaoService.listarPorLote(loteId);
        return ResponseEntity.ok(movimentacoes);
    }
}