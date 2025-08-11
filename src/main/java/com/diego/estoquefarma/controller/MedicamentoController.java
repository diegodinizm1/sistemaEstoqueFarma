package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.MedicamentoDTO;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.service.MedicamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    public List<Medicamento> getMedicamentos(){
        return medicamentoService.listarTodos();
    }


    @PostMapping
    public ResponseEntity<Medicamento> criarMedicamento(@RequestBody @Valid MedicamentoDTO dto) {
        Medicamento novoMedicamento = medicamentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoMedicamento);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> getMedicamentoPorId(@PathVariable Long id) {
        try {
            Medicamento medicamento = medicamentoService.buscarPorId(id);
            return ResponseEntity.ok(medicamento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Medicamento> atualizarMedicamento(@PathVariable Long id, @RequestBody @Valid MedicamentoDTO dto) {
        try {
            Medicamento medicamentoAtualizado = medicamentoService.atualizar(id, dto);
            return ResponseEntity.ok(medicamentoAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedicamento(@PathVariable Long id) {
        try {
            medicamentoService.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}