package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.SetorDTO;
import com.diego.estoquefarma.model.Setor;
import com.diego.estoquefarma.service.SetorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setores")
public class SetorController {

    @Autowired
    private SetorService setorService;

    @PostMapping
    public ResponseEntity<Setor> criarSetor(@RequestBody @Valid SetorDTO dto) {
        Setor novoSetor = setorService.criarSetor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoSetor);
    }

    @GetMapping
    public ResponseEntity<List<Setor>> listarSetores() {
        return ResponseEntity.ok(setorService.listarTodos());
    }

    @GetMapping("/alfabetico")
    public ResponseEntity<List<Setor>> listarSetoresEmOrdemAlfabetica() {
        return ResponseEntity.ok(setorService.listarPorOrdemAlfabetica());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Setor> getSetorPorId(@PathVariable Long id) {
        return ResponseEntity.ok(setorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Setor> atualizarSetor(@PathVariable Long id, @RequestBody @Valid SetorDTO dto) {
        Setor setorAtualizado = setorService.atualizar(id, dto);
        return ResponseEntity.ok(setorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarSetor(@PathVariable Long id) {
        setorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}