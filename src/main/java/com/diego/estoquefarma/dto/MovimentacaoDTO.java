package com.diego.estoquefarma.dto;

import com.diego.estoquefarma.model.Movimentacao;
import com.diego.estoquefarma.model.TipoMovimentacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoDTO(
        Long idMovimentacao,
        LocalDateTime dataHora,
        TipoMovimentacao tipo,
        BigDecimal quantidade,
        String unidadeMedida,
        String nomeProduto,
        String numeroLote,
        String nomeUsuario,
        String descricao,
        String nomeSetorDestino
) {
    public static MovimentacaoDTO fromEntity(Movimentacao mov) {
        return new MovimentacaoDTO(
                mov.getId(),
                mov.getDataHoraMovimentacao(),
                mov.getTipoMovimentacao(),
                mov.getQuantidade(),
                mov.getLote().getMedicamento().getUnidadeMedida(),
                mov.getLote().getMedicamento().getNome(),
                mov.getLote().getNumeroLote(),
                mov.getUsuarioResponsavel().getNomeCompleto(),
                mov.getObservacao(),

                mov.getSetorSaida() != null ? mov.getSetorSaida().getNome() : null
        );
    }
}