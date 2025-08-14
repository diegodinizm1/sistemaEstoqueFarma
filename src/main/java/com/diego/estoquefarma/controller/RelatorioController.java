package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.RespostaRelatorioPivoteado;
import com.diego.estoquefarma.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/saida-diaria")
    public ResponseEntity<RespostaRelatorioPivoteado> getRelatorioSaidaDiaria(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        LocalDate dataConsulta = (data != null) ? data : LocalDate.now();

        RespostaRelatorioPivoteado relatorio = relatorioService.gerarRelatorioSaidaDiariaPivoteado(dataConsulta);
        return ResponseEntity.ok(relatorio);
    }
}