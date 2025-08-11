package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.MedicamentoDTO;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.repository.MedicamentoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public Medicamento criar(MedicamentoDTO dto) {
        Medicamento med = new Medicamento();
        med.setNome(dto.nome());
        return medicamentoRepository.save(med);
    }

    public Medicamento buscarPorId(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado com ID: " + id));
    }

    public List<Medicamento> listarTodos() {
        return medicamentoRepository.findAll();
    }

    public List<Medicamento> buscarPorNome(String nome) {
        return medicamentoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public void deletar(Long id) {
        medicamentoRepository.deleteById(id);
    }

    public Medicamento atualizar(Long id, @Valid MedicamentoDTO dto) {
        Medicamento novoMedicamento =  buscarPorId(id);
        if(novoMedicamento == null) {
            throw new RuntimeException("Medicamento inexistente no banco de dados.");
        }
        novoMedicamento.setId(id);
        novoMedicamento.setNome(dto.nome());
        return medicamentoRepository.save(novoMedicamento);
    }
}