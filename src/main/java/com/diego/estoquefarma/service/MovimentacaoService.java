package com.diego.estoquefarma.service;

import com.diego.estoquefarma.model.*;
import com.diego.estoquefarma.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class MovimentacaoService {
    @Autowired
    private MovimentacaoRepository movimentacoesRepository;

    public void registrar(Lote lote, Usuario usuario, TipoMovimentacao tipo, BigDecimal quantidade, Setor setor, String obs) {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setLote(lote);
        movimentacao.setUsuarioResponsavel(usuario);
        movimentacao.setTipoMovimentacao(tipo);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setDataHoraMovimentacao(LocalDateTime.now());
        movimentacao.setSetorSaida(setor);
        movimentacao.setObservacao(obs);
        movimentacoesRepository.save(movimentacao);
    }
}