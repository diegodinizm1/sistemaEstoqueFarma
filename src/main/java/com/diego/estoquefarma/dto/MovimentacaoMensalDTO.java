package com.diego.estoquefarma.dto;

import java.math.BigDecimal; // Importe BigDecimal

public record MovimentacaoMensalDTO(
        Integer ano,
        Integer mes,
        BigDecimal totalEntradas,
        BigDecimal totalSaidas
) {}