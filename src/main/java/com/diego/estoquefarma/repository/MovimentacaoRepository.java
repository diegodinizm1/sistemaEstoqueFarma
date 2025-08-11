package com.diego.estoquefarma.repository;

import com.diego.estoquefarma.model.Movimentacao;
import com.diego.estoquefarma.model.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao,Long> {
    List<Movimentacao> findByLoteIdOrderByDataHoraMovimentacaoDesc(Long loteId);

    List<Movimentacao> findByTipoMovimentacaoAndDataHoraMovimentacaoBetween(
            TipoMovimentacao tipo,
            LocalDateTime inicio,
            LocalDateTime fim
    );


}
