package com.diego.estoquefarma.controller.front;

import com.diego.estoquefarma.dto.EstoqueDTO;
import com.diego.estoquefarma.service.ConsultaEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@Controller
@RequestMapping("/view/estoque")
public class EstoqueViewController {

    @Autowired
    private ConsultaEstoqueService consultaEstoqueService;

    @GetMapping
    public String mostrarPaginaDeEstoque(Model model) {
        List<EstoqueDTO> listaEstoque = consultaEstoqueService.listarEstoqueAtual();
        model.addAttribute("itensDeEstoque", listaEstoque);
        return "estoque";
    }
}