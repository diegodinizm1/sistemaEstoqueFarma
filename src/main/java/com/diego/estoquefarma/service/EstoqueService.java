package com.diego.estoquefarma.service;

import com.diego.estoquefarma.model.Estoque;
import com.diego.estoquefarma.model.Lote;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    public void incrementar(Lote lote, BigDecimal quantidade) {
        Estoque estoque = estoqueRepository.findById(lote.getId())
                .orElseGet(() -> {
                    Estoque novoEstoque = new Estoque();
                    novoEstoque.setLote(lote);
                    novoEstoque.setQuantidadeDisponivel(BigDecimal.ZERO);
                    return novoEstoque;
                });
        estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel().add(quantidade));
        estoqueRepository.save(estoque);
    }

    public void decrementar(Lote lote, BigDecimal quantidade) {
        Estoque estoque = estoqueRepository.findById(lote.getId())
                .orElseThrow(() -> new IllegalStateException("Estoque não encontrado para o lote ID: " + lote.getId()));

        estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel().subtract(quantidade));
        estoqueRepository.save(estoque);
    }

    public List<Estoque> findLotesDisponiveisParaProduto(Medicamento medicamento) {
        return estoqueRepository.findByLote_MedicamentoAndQuantidadeDisponivelGreaterThanOrderByLote_DataValidadeAsc(medicamento, BigDecimal.ZERO);
    }
}