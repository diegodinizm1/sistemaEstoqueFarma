package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.RespostaRelatorioPivoteado;
import com.diego.estoquefarma.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/saida-diaria")
    public ResponseEntity<RespostaRelatorioPivoteado> getRelatorioSaidaDiaria() {
        RespostaRelatorioPivoteado relatorio = relatorioService.gerarRelatorioSaidaDiariaPivoteado();
        return ResponseEntity.ok(relatorio);
    }
}