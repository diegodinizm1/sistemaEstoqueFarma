package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.AjusteContagemDTO;
import com.diego.estoquefarma.dto.DescarteDTO;
import com.diego.estoquefarma.dto.EstoqueDTO;
import com.diego.estoquefarma.service.ConsultaEstoqueService;
import com.diego.estoquefarma.service.EstoqueService;
import com.diego.estoquefarma.service.OperacaoEstoqueService;
import com.diego.estoquefarma.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private ConsultaEstoqueService consultaEstoqueService;
    @Autowired
    private EstoqueService estoqueService;
    @Autowired
    private OperacaoEstoqueService operacaoEstoqueService;
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<EstoqueDTO>> getEstoqueAtual() {
        List<EstoqueDTO> estoque = consultaEstoqueService.listarEstoqueAtual();
        return ResponseEntity.ok(estoque);
    }

    @PatchMapping("/ajustar-contagem")
    public ResponseEntity<Void> ajustarContagem(@RequestBody AjusteContagemDTO dto) {
        estoqueService.ajustarContagem(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/descartar")
    public ResponseEntity<Void> descartarLote(@RequestBody DescarteDTO dto) {
        Long idUsuarioLogado = usuarioService.getUsuarioLogado().getId();
        operacaoEstoqueService.descartarLote(dto.idEstoque(), idUsuarioLogado, dto.motivo());
        return ResponseEntity.ok().build();
    }

}
