package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.AjusteContagemDTO;
import com.diego.estoquefarma.model.*;
import com.diego.estoquefarma.repository.EstoqueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;
    @Autowired private MovimentacaoService movimentacaoService;
    @Autowired private UsuarioService usuarioService;

    @Transactional
    public void ajustarContagem(AjusteContagemDTO dto) {
        Estoque estoque = estoqueRepository.findById(dto.idEstoque())
                .orElseThrow(() -> new RuntimeException("Item de estoque não encontrado."));

        BigDecimal quantidadeAntiga = estoque.getQuantidadeDisponivel();
        BigDecimal novaQuantidade = new BigDecimal(dto.novaQuantidade());

        estoque.setQuantidadeDisponivel(novaQuantidade);
        estoqueRepository.save(estoque);

        BigDecimal diferenca = novaQuantidade.subtract(quantidadeAntiga);
        TipoMovimentacao tipoMovimentacao;
        String observacao = dto.observacao();

        if (diferenca.compareTo(BigDecimal.ZERO) < 0) {
            tipoMovimentacao = TipoMovimentacao.AJUSTE_PERDA;
            observacao = "AJUSTE_PERDA: " + observacao;
        } else if (diferenca.compareTo(BigDecimal.ZERO) > 0) {
            tipoMovimentacao = TipoMovimentacao.AJUSTE_INVENTARIO;
            observacao = "AJUSTE_INVENTARIO: " + observacao;
        } else {
            return;
        }

        Usuario usuario = usuarioService.getUsuarioLogado();

        // Registra a movimentação de ajuste
        movimentacaoService.registrar(
                estoque.getLote(),
                usuario,
                tipoMovimentacao,
                diferenca.abs(),
                null,
                observacao
        );
    }

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
        return estoqueRepository.findByLote_MedicamentoAndQuantidadeDisponivelGreaterThanAndLoteDataValidadeGreaterThanEqualOrderByLoteDataValidadeAsc(
                medicamento,
                BigDecimal.ZERO,
                LocalDate.now()
        );
    }
}