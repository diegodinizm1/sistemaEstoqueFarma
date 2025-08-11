package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.EstoqueDTO;
import com.diego.estoquefarma.service.ConsultaEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private ConsultaEstoqueService consultaEstoqueService;

    @GetMapping
    public ResponseEntity<List<EstoqueDTO>> getEstoqueAtual() {
        List<EstoqueDTO> estoque = consultaEstoqueService.listarEstoqueAtual();
        return ResponseEntity.ok(estoque);
    }
}
