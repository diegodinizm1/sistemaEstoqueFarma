package com.diego.estoquefarma.dto;

public record MovimentacaoMensalDTO(
        int ano,
        int mes,
        long totalEntradas,
        long totalSaidas
) {}