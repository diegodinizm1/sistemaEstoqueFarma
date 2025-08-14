package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.DashboardStatsDTO;
import com.diego.estoquefarma.dto.EstoqueDTO;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.repository.EstoqueRepository;
import com.diego.estoquefarma.repository.MedicamentoRepository;
import com.diego.estoquefarma.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsultaEstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;
    @Autowired private MedicamentoRepository medicamentoRepository;
    @Autowired private SetorRepository setorRepository;

    @Transactional(readOnly = true)
    public List<EstoqueDTO> listarEstoqueAtual() {
        List<com.diego.estoquefarma.model.Estoque> estoqueEmEntidades =
                estoqueRepository.findByQuantidadeDisponivelGreaterThanOrderByLoteDataValidadeAsc(BigDecimal.ZERO);

        return estoqueEmEntidades.stream()
                .map(EstoqueDTO::converter)
                .collect(Collectors.toList());
    }

    public DashboardStatsDTO getDashboardStats() {
        long totalProdutos = medicamentoRepository.count();
        long totalSetores = setorRepository.count();
        long lotesAtivos = estoqueRepository.countByQuantidadeDisponivelGreaterThan(0);

        LocalDate hoje = LocalDate.now();
        LocalDate dataLimite = hoje.plusDays(30);
        long proximoVencimento = estoqueRepository.countByLote_DataValidadeBetween(hoje, dataLimite);

        return new DashboardStatsDTO(totalProdutos, lotesAtivos, proximoVencimento, totalSetores);
    }
}