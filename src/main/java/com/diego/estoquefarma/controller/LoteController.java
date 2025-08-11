package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.model.Lote;
import com.diego.estoquefarma.service.ConsultaLoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lotes")
public class LoteController {

    @Autowired
    private ConsultaLoteService consultaLoteService;


    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<Lote>> listarPorProduto(@PathVariable Long produtoId) {
        List<Lote> lotes = consultaLoteService.listarPorProduto(produtoId);
        return ResponseEntity.ok(lotes);
    }
}