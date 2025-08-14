package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.MedicamentoDTO;
import com.diego.estoquefarma.model.Medicamento;
import com.diego.estoquefarma.repository.MedicamentoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    public List<Medicamento> listarMedicamentosAtivos() {
        return medicamentoRepository.findAllByAtivoTrue();
    }

    public Medicamento criar(MedicamentoDTO dto) {
        Medicamento med = new Medicamento();
        med.setNome(dto.nome());
        med.setDescricaoDetalhada(dto.descricaoDetalhada());
        med.setUnidadeMedida(dto.unidadeMedida());
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

    @Transactional
    public void inativarMedicamento(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado com o ID: " + id));

        medicamento.setAtivo(false);
        medicamentoRepository.save(medicamento);
    }

    @Transactional
    public void ativarMedicamento(Long id) {
        Medicamento medicamento = medicamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicamento não encontrado com o ID: " + id));

        medicamento.setAtivo(true);
        medicamentoRepository.save(medicamento);
    }

    public Medicamento atualizar(Long id, @Valid MedicamentoDTO dto) {
        Medicamento novoMedicamento =  buscarPorId(id);
        if(novoMedicamento == null) {
            throw new RuntimeException("Medicamento inexistente no banco de dados.");
        }
        novoMedicamento.setId(id);
        novoMedicamento.setNome(dto.nome());
        novoMedicamento.setDescricaoDetalhada(dto.descricaoDetalhada());
        novoMedicamento.setUnidadeMedida(dto.unidadeMedida());
        return medicamentoRepository.save(novoMedicamento);
    }

    public List<Medicamento> listarMedicamentosInativos() {
        return medicamentoRepository.findAllByAtivoFalse();
    }
}