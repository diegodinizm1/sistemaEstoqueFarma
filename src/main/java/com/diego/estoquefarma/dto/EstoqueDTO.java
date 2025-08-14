package com.diego.estoquefarma.dto;

import com.diego.estoquefarma.model.Estoque;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EstoqueDTO(Long id, Long idMedicamento, String nomeMedicamento, Long idLote, String numeroLote, LocalDate dataValidade, BigDecimal quantidadeDisponivel,String unidadeMedida) {

    public static EstoqueDTO converter(Estoque estoque) {
        return new EstoqueDTO(
                estoque.getId(),
                estoque.getLote().getMedicamento().getId(),
                estoque.getLote().getMedicamento().getNome(),
                estoque.getLote().getId(),
                estoque.getLote().getNumeroLote(),
                estoque.getLote().getDataValidade(),
                estoque.getQuantidadeDisponivel(),
                estoque.getLote().getMedicamento().getUnidadeMedida()
        );
    }
}
