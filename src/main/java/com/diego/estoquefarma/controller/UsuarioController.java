package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.UsuarioCreateDTO;
import com.diego.estoquefarma.dto.UsuarioResponseDTO;
import com.diego.estoquefarma.model.Usuario;
import com.diego.estoquefarma.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@RequestBody @Valid UsuarioCreateDTO dto) {
        Usuario novoUsuario = usuarioService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponseDTO.fromEntity(novoUsuario));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> getUsuarioLogado(@AuthenticationPrincipal Usuario usuarioLogado) {
        return ResponseEntity.ok(UsuarioResponseDTO.fromEntity(usuarioLogado));
    }
}