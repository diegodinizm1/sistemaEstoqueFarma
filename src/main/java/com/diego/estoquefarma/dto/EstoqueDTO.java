package com.diego.estoquefarma.dto;

import com.diego.estoquefarma.model.Estoque;
import com.diego.estoquefarma.model.Lote;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EstoqueDTO(Long idMedicamento, String nomeMedicamento, Long idLote, String numeroLote, LocalDate dataValidade, BigDecimal quantidadeDisponivel) {

    public static EstoqueDTO converter(Estoque estoque) {
        return new EstoqueDTO(
                estoque.getLote().getMedicamento().getId(),
                estoque.getLote().getMedicamento().getNome(),
                estoque.getLote().getId(),
                estoque.getLote().getNumeroLote(),
                estoque.getLote().getDataValidade(),
                estoque.getQuantidadeDisponivel()
        );
    }
}
