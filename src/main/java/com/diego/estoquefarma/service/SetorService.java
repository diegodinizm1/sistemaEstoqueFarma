package com.diego.estoquefarma.service;

import com.diego.estoquefarma.dto.SetorDTO;
import com.diego.estoquefarma.model.Setor;
import com.diego.estoquefarma.repository.SetorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetorService {

    @Autowired
    private SetorRepository setorRepository;


    public Setor criarSetor(@Valid SetorDTO setor) {
        if(!setorRepository.existsByNomeIgnoreCase(setor.nome())) {
            Setor novoSetor = new Setor();
            novoSetor.setNome(setor.nome());
            return setorRepository.save(novoSetor);
        }
        throw new RuntimeException("Setor já existente");
    }

    public Setor buscarPorId(Long id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Setor não encontrado com ID: " + id));
    }

    public List<Setor> listarTodos() {
        return setorRepository.findAll();
    }

    public List<Setor> buscarPorNome(String nome) {
        return setorRepository.findAllByNome(nome);
    }

    public Setor atualizar(Long id, @Valid SetorDTO dto) {
        Setor setor = buscarPorId(id);
        if(setor != null) {
            setor.setNome(dto.nome());
            return setorRepository.save(setor);
        }
        throw new RuntimeException("Setor não existente");
    }

    public void deletar(Long id) {
        setorRepository.deleteById(id);
    }

    public List<Setor> listarPorOrdemAlfabetica() {
        return setorRepository.findAllByOrderByNomeAsc();
    }
}