package com.diego.estoquefarma.repository;

import com.diego.estoquefarma.model.Estoque;
import com.diego.estoquefarma.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EstoqueRepository extends JpaRepository<Estoque,Long> {
    List<Estoque> findByLote_MedicamentoAndQuantidadeDisponivelGreaterThanOrderByLote_DataValidadeAsc(
            Medicamento medicamento,
            BigDecimal quantidadeMinima
    );

    List<Estoque> findByQuantidadeDisponivelGreaterThan(BigDecimal valor);

    long countByQuantidadeDisponivelGreaterThan(int zero);

    @Query("SELECT COUNT(e) FROM Estoque e WHERE e.lote.dataValidade BETWEEN :hoje AND :dataLimite AND e.quantidadeDisponivel > 0")
    long countByLote_DataValidadeBetween(LocalDate hoje, LocalDate dataLimite);
}