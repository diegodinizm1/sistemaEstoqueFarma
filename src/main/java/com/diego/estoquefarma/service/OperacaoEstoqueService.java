package com.diego.estoquefarma.service;

import com.diego.estoquefarma.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class OperacaoEstoqueService {

    @Autowired private MedicamentoService medicamentoService;
    @Autowired private LoteService loteService;
    @Autowired private EstoqueService estoqueService;
    @Autowired private MovimentacaoService movimentacaoService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private SetorService setorService;


    @Transactional
    public void registrarEntrada(Long idMedicamento, String numeroLote, LocalDate dataValidade, BigDecimal quantidade, Long idUsuario) {
        Medicamento medicamento = medicamentoService.buscarPorId(idMedicamento);
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        Lote lote = loteService.procurarOuCriarLote(medicamento, numeroLote, dataValidade);
        estoqueService.incrementar(lote, quantidade);
        movimentacaoService.registrar(lote, usuario, TipoMovimentacao.ENTRADA, quantidade, null, "Entrada de novo lote.");
    }

    @Transactional
    public void registrarSaidaParaSetor(Long idMedicamento, Long idSetor, BigDecimal quantidadeRequerida, Long idUsuario) {
        Medicamento medicamento = medicamentoService.buscarPorId(idMedicamento);
        Setor setor = setorService.buscarPorId(idSetor);
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        List<Estoque> lotesDisponiveis = estoqueService.findLotesDisponiveisParaProduto(medicamento);

        BigDecimal quantidadePendente = quantidadeRequerida;

        for (Estoque estoqueDoLote : lotesDisponiveis) {
            BigDecimal quantidadeASubtrair = estoqueDoLote.getQuantidadeDisponivel().min(quantidadePendente);

            estoqueService.decrementar(estoqueDoLote.getLote(), quantidadeASubtrair);
            movimentacaoService.registrar(estoqueDoLote.getLote(), usuario, TipoMovimentacao.SAIDA_SETOR, quantidadeASubtrair, setor, null);

            quantidadePendente = quantidadePendente.subtract(quantidadeASubtrair);
            if (quantidadePendente.compareTo(BigDecimal.ZERO) == 0) break;
        }

        if (quantidadePendente.compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Estoque insuficiente para " + medicamento.getNome() + ". Faltam " + quantidadePendente + " unidades.");
        }
    }
}