package com.diego.estoquefarma.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank String login, @NotBlank String senha) {}