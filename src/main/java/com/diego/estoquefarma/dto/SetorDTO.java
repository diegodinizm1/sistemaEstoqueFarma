package com.diego.estoquefarma.dto;
import jakarta.validation.constraints.NotBlank;

public record SetorDTO(@NotBlank String nome) {}