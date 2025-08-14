package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.RelatorioSaidaPivoteadoDTO;
import com.diego.estoquefarma.dto.RespostaRelatorioPivoteado;
import com.diego.estoquefarma.model.Lote;
import com.diego.estoquefarma.model.Movimentacao;
import com.diego.estoquefarma.model.Setor;
import com.diego.estoquefarma.model.TipoMovimentacao;
import com.diego.estoquefarma.repository.MovimentacaoRepository;
import com.diego.estoquefarma.repository.SetorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RelatorioService {

    @Autowired private MovimentacaoRepository movimentacaoRepository;
    @Autowired private SetorRepository setorRepository;

    public RespostaRelatorioPivoteado gerarRelatorioSaidaDiariaPivoteado(LocalDate dataConsulta) {
        List<Setor> todosOsSetores = setorRepository.findAll();
        List<String> nomeSetores = todosOsSetores.stream().map(Setor::getNome).sorted().collect(Collectors.toList());

        var inicioDoDia = dataConsulta.atStartOfDay();
        var fimDoDia = dataConsulta.atTime(LocalTime.MAX);
        List<Movimentacao> saidasDeHoje = movimentacaoRepository
                .findByTipoMovimentacaoAndDataHoraMovimentacaoBetween(TipoMovimentacao.SAIDA_SETOR, inicioDoDia, fimDoDia);

        Map<Lote, Map<String, BigDecimal>> dadosAgrupados = saidasDeHoje.stream()
                .collect(Collectors.groupingBy(
                        Movimentacao::getLote,
                        Collectors.groupingBy(
                                mov -> mov.getSetorSaida().getNome(),
                                Collectors.reducing(BigDecimal.ZERO, Movimentacao::getQuantidade, BigDecimal::add)
                        )
                ));

        List<RelatorioSaidaPivoteadoDTO> dadosFormatados = dadosAgrupados.entrySet().stream()
                .map(entry -> new RelatorioSaidaPivoteadoDTO(
                        entry.getKey().getMedicamento().getNome(),
                        entry.getKey().getNumeroLote(),
                        entry.getValue()
                ))
                .collect(Collectors.toList());

        return new RespostaRelatorioPivoteado(nomeSetores, dadosFormatados);
    }
}