package com.diego.estoquefarma.dto;

public record EstoqueValidadeDTO(
        long lotesValidos,
        long lotesProximos,
        long lotesVencidos
) {}