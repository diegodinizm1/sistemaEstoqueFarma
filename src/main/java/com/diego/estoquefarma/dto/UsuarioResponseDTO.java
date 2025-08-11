package com.diego.estoquefarma.dto;

import com.diego.estoquefarma.model.Usuario;

public record UsuarioResponseDTO(Long id, String login, String nomeCompleto, boolean ativo) {
    public static UsuarioResponseDTO fromEntity(Usuario usuario) {
        return new UsuarioResponseDTO(usuario.getId(), usuario.getUsername(), usuario.getNomeCompleto(), usuario.isEnabled());
    }
}