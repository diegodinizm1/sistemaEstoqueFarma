package com.diego.estoquefarma.controller;

import com.diego.estoquefarma.dto.ConsumoPorSetorDTO;
import com.diego.estoquefarma.dto.DashboardStatsDTO;
import com.diego.estoquefarma.dto.EstoqueValidadeDTO;
import com.diego.estoquefarma.dto.MovimentacaoMensalDTO;
import com.diego.estoquefarma.repository.EstoqueRepository;
import com.diego.estoquefarma.repository.MedicamentoRepository;
import com.diego.estoquefarma.repository.MovimentacaoRepository;
import com.diego.estoquefarma.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class DashboardController {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private SetorRepository setorRepository;

    @GetMapping("/consultas/dashboard-stats")
    public DashboardStatsDTO getDashboardStats() {
        long totalProdutosCadastrados = medicamentoRepository.count();

        long totalLotesAtivos = estoqueRepository.countByQuantidadeDisponivelGreaterThan(0);

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusMonths(1);
        long itensProximoVencimento = estoqueRepository.countByLote_DataValidadeBetween(hoje, dataLimite);

        long totalSetoresCadastrados = setorRepository.count();

        return new DashboardStatsDTO(
                totalProdutosCadastrados,
                totalLotesAtivos,
                itensProximoVencimento,
                totalSetoresCadastrados
        );
    }

    @GetMapping("/consultas/estoque/validade-stats")
    public EstoqueValidadeDTO getEstoqueValidadeStats() {
        LocalDate hoje = LocalDate.now();
        LocalDate daquiUmMes = hoje.plusMonths(1);

        long validos = estoqueRepository.countByLote_DataValidadeAfter(daquiUmMes);
        long proximos = estoqueRepository.countByLote_DataValidadeBetween(hoje, daquiUmMes);
        long vencidos = estoqueRepository.countByLote_DataValidadeBefore(hoje);

        return new EstoqueValidadeDTO(validos, proximos, vencidos);
    }


    @GetMapping("/consultas/movimentacoes/mensal-stats")
    public List<MovimentacaoMensalDTO> getMovimentacoesMensais() {
        LocalDateTime dataInicial = LocalDateTime.now().minusMonths(6); // Últimos 6 meses
        return movimentacaoRepository.findMovimentacaoMensalDesde(dataInicial);
    }

    @GetMapping("consultas/consumo-por-setor")
    public List<ConsumoPorSetorDTO> getConsumoPorSetor() {
        LocalDateTime dataInicial = LocalDateTime.now().minusMonths(1); // Último mês
        return movimentacaoRepository.findConsumoPorSetorDesde(dataInicial);
    }
}