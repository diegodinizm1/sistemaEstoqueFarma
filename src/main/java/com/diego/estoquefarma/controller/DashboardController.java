package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.DashboardStatsDTO;
import com.diego.estoquefarma.repository.EstoqueRepository;
import com.diego.estoquefarma.repository.MedicamentoRepository;
import com.diego.estoquefarma.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
public class DashboardController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private SetorRepository setorRepository;

    @GetMapping("/consultas/dashboard-stats")
    public DashboardStatsDTO getDashboardStats() {
        // Coleta o total de medicamentos cadastrados
        long totalProdutosCadastrados = medicamentoRepository.count();

        // Coleta o total de lotes ativos (quantidade > 0)
        long totalLotesAtivos = estoqueRepository.countByQuantidadeDisponivelGreaterThan(0);

        // Coleta o total de itens próximos do vencimento (nos próximos 90 dias)
        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusMonths(1);
        long itensProximoVencimento = estoqueRepository.countByLote_DataValidadeBetween(hoje, dataLimite);

        // Coleta o total de setores cadastrados
        long totalSetoresCadastrados = setorRepository.count();

        return new DashboardStatsDTO(
                totalProdutosCadastrados,
                totalLotesAtivos,
                itensProximoVencimento,
                totalSetoresCadastrados
        );
    }
}