package com.diego.estoquefarma.repository;

import com.diego.estoquefarma.model.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicamentoRepository extends JpaRepository<Medicamento,Long> {

    List<Medicamento> findByNomeContainingIgnoreCase(String nome);
    List<Medicamento> findAllByAtivoTrue();
    List<Medicamento> findAllByAtivoFalse();
}
