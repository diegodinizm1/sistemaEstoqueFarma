package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.EntradaRequestDTO;
import com.diego.estoquefarma.dto.SaidaRequestDTO;
import com.diego.estoquefarma.service.OperacaoEstoqueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/operacoes")
public class OperacaoEstoqueController {

    @Autowired
    private OperacaoEstoqueService operacaoEstoqueService;

    @PostMapping("/entradas")
    public ResponseEntity<String> registrarEntrada(@RequestBody @Valid EntradaRequestDTO dados) {
        try {

            operacaoEstoqueService.registrarEntrada(
                    dados.idMedicamento(),
                    dados.numeroLote(),
                    dados.dataValidade(),
                    dados.quantidade(),
                    dados.idUsuario()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body("Entrada registrada com sucesso!");
        } catch (Exception e) {

            return ResponseEntity.badRequest().body("Erro ao registrar entrada: " + e.getMessage());
        }
    }

    @PostMapping("/saidas")
    public ResponseEntity<String> registrarSaida(@RequestBody @Valid SaidaRequestDTO dados) {
        try {
            operacaoEstoqueService.registrarSaidaParaSetor(
                    dados.idMedicamento(),
                    dados.idSetor(),
                    dados.quantidade(),
                    dados.idUsuario()
            );
            return ResponseEntity.ok("Saída registrada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao registrar saída: " + e.getMessage());
        }
    }
}