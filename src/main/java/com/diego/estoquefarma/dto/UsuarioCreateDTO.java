package com.diego.estoquefarma.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioCreateDTO(
        @NotBlank String login,
        @NotBlank String senha,
        @NotBlank String nomeCompleto
) {}