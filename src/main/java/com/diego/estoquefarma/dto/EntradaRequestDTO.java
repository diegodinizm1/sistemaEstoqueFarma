package com.diego.estoquefarma.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EntradaRequestDTO(
        @NotNull
        Long idMedicamento,
        @NotBlank(message = "O número do lote não deve ser vazio.")
        String numeroLote,
        @NotNull
        LocalDate dataValidade,
        @Positive
        BigDecimal quantidade,
        @NotNull
        Long idUsuario
) {
}
