package com.diego.estoquefarma.service;

import com.diego.estoquefarma.model.Lote;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.repository.LoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class LoteService {

    @Autowired private LoteRepository loteRepository;

    public Lote procurarOuCriarLote(Medicamento medicamento, String numeroLote, LocalDate dataValidade) {
        return loteRepository.findByMedicamentoAndNumeroLote(medicamento, numeroLote)
                .orElseGet(() -> {
                    Lote novoLote = new Lote();
                    novoLote.setMedicamento(medicamento);
                    novoLote.setNumeroLote(numeroLote);
                    novoLote.setDataValidade(dataValidade);
                    return loteRepository.save(novoLote);
                });
    }
}