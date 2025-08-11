package com.diego.estoquefarma.repository;

import com.diego.estoquefarma.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetorRepository extends JpaRepository<Setor,Long> {

    boolean existsByNomeIgnoreCase(String nome);

    List<Setor> findAllByNome(String nome);

    List<Setor> findAllByOrderByNomeAsc();
}
