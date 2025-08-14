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

    @GetMapping("/ativos")
    public ResponseEntity<List<Medicamento>> listarAtivos() {
        List<Medicamento> medicamentos = medicamentoService.listarMedicamentosAtivos();
        return ResponseEntity.ok(medicamentos);
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<Medicamento>> listarInativos() {
        List<Medicamento> medicamentos = medicamentoService.listarMedicamentosInativos();
        return ResponseEntity.ok(medicamentos);
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

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        medicamentoService.ativarMedicamento(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarMedicamento(@PathVariable Long id) {
        try {
            medicamentoService.inativarMedicamento(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}