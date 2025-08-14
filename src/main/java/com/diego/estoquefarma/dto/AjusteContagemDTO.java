package com.diego.estoquefarma.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AjusteContagemDTO(
        @NotNull Long idEstoque,
        @NotNull @Min(0) int novaQuantidade,
        @NotBlank String observacao
) {}