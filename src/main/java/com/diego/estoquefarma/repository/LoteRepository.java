package com.diego.estoquefarma.repository;

import com.diego.estoquefarma.model.Lote;
import com.diego.estoquefarma.model.Medicamento;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote,Long> {
    Optional<Lote> findByMedicamentoAndNumeroLote(Medicamento medicamento, String numeroLote);
    List<Lote> findByMedicamento(Medicamento medicamento);
    List<Lote> findByMedicamentoOrderByDataValidadeAsc(Medicamento medicamento);
}
