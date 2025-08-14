package com.diego.estoquefarma.repository;

import com.diego.estoquefarma.dto.ConsumoPorSetorDTO;
import com.diego.estoquefarma.dto.MovimentacaoMensalDTO;
import com.diego.estoquefarma.model.Movimentacao;
import com.diego.estoquefarma.model.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao,Long> {
    List<Movimentacao> findByLoteIdOrderByDataHoraMovimentacaoDesc(Long loteId);

    List<Movimentacao> findByTipoMovimentacaoAndDataHoraMovimentacaoBetween(
            TipoMovimentacao tipo,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    @Query("SELECT new com.diego.estoquefarma.dto.MovimentacaoMensalDTO(" +
            "YEAR(m.dataHoraMovimentacao) AS ano, " +
            "MONTH(m.dataHoraMovimentacao) AS mes, " +
            "SUM(CASE WHEN m.tipoMovimentacao = 'ENTRADA' THEN m.quantidade ELSE 0 END) AS totalEntradas, " +
            "SUM(CASE WHEN m.tipoMovimentacao = 'SAIDA_SETOR' THEN m.quantidade ELSE 0 END) AS totalSaidas) " +
            "FROM Movimentacao m " +
            "WHERE m.dataHoraMovimentacao >= :dataInicial " +
            "GROUP BY YEAR(m.dataHoraMovimentacao), MONTH(m.dataHoraMovimentacao) " +
            "ORDER BY ano ASC, mes ASC")
    List<MovimentacaoMensalDTO> findMovimentacaoMensalDesde(LocalDateTime dataInicial);

    @Query("SELECT new com.diego.estoquefarma.dto.ConsumoPorSetorDTO(m.setorSaida.nome, SUM(m.quantidade)) " +
            "FROM Movimentacao m " +
            "WHERE m.tipoMovimentacao = com.diego.estoquefarma.model.TipoMovimentacao.SAIDA_SETOR " +
            "AND m.dataHoraMovimentacao >= :dataInicial " +
            "GROUP BY m.setorSaida.nome")
    List<ConsumoPorSetorDTO> findConsumoPorSetorDesde(LocalDateTime dataInicial);


}
