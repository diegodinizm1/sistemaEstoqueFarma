package com.diego.estoquefarma.dto;

import java.math.BigDecimal;

public record ConsumoPorSetorDTO(
        String nomeSetor,
        BigDecimal totalConsumo
) {}