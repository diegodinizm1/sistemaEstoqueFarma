package com.diego.estoquefarma.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record SaidaRequestDTO(
        @NotNull Long idMedicamento,
        @NotNull Long idSetor,
        @NotNull @Positive BigDecimal quantidade,
        @NotNull Long idUsuario
        ) {
}
