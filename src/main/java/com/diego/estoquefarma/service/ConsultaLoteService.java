package com.diego.estoquefarma.service;

import com.diego.estoquefarma.model.Lote;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConsultaLoteService {

    @Autowired
    private LoteRepository loteRepository;

    @Autowired
    private MedicamentoService medicamentoService;


    @Transactional(readOnly = true)
    public List<Lote> listarPorProduto(Long produtoId) {
        Medicamento medicamento = medicamentoService.buscarPorId(produtoId);
        return loteRepository.findByMedicamento(medicamento);
    }
}