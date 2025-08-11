package com.diego.estoquefarma.dto;

public record DashboardStatsDTO(
        long totalProdutosCadastrados,
        long totalLotesAtivos,
        long itensProximoVencimento,
        long totalSetoresCadastrados
) {}